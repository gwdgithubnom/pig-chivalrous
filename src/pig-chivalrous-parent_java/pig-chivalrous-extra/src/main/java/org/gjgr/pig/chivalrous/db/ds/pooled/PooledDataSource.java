package org.gjgr.pig.chivalrous.db.ds.pooled;

import org.gjgr.pig.chivalrous.core.io.IoCommand;
import org.gjgr.pig.chivalrous.core.lang.CollectionCommand;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.gjgr.pig.chivalrous.core.util.ThreadUtil;
import org.gjgr.pig.chivalrous.db.DbRuntimeException;
import org.gjgr.pig.chivalrous.db.ds.simple.AbstractDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 池化数据源
 *
 * @author Looly
 */
public class PooledDataSource extends AbstractDataSource {

    private Queue<PooledConnection> freePool;
    private int activeCount; // 活跃连接数

    private DbConfig config;

    /**
     * 构造，读取默认的配置文件和默认分组
     */
    public PooledDataSource() {
        this(StringCommand.EMPTY);
    }

    /**
     * 构造，读取默认的配置文件
     *
     * @param group 分组
     */
    public PooledDataSource(String group) {
        this(new DbSetting(), group);
    }

    // -------------------------------------------------------------------- Constructor start

    /**
     * 构造
     *
     * @param setting 数据库配置文件对象
     * @param group   分组
     */
    public PooledDataSource(DbSetting setting, String group) {
        this(setting.getDbConfig(group));
    }

    /**
     * 构造
     *
     * @param config 数据库配置
     */
    public PooledDataSource(DbConfig config) {
        this.config = config;
        freePool = new LinkedList<PooledConnection>();
        int initialSize = config.getInitialSize();
        try {
            while (initialSize-- > 0) {
                freePool.offer(newConnection());
            }
        } catch (SQLException e) {
            throw new DbRuntimeException(e);
        }
    }

    /**
     * 获得一个数据源
     *
     * @param group 数据源分组
     */
    public static synchronized PooledDataSource getDataSource(String group) {
        return new PooledDataSource(group);
    }

    /**
     * 获得一个数据源
     */
    public static synchronized PooledDataSource getDataSource() {
        return new PooledDataSource();
    }
    // -------------------------------------------------------------------- Constructor start

    /**
     * 从数据库连接池中获取数据库连接对象
     */
    @Override
    public synchronized Connection getConnection() throws SQLException {
        return getConnection(config.getMaxWait());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        throw new SQLException("Pooled DataSource is not allow to get special Connection!");
    }

    /**
     * 释放连接，连接会被返回给连接池
     *
     * @param conn 连接
     * @return 释放成功与否
     */
    protected synchronized boolean free(PooledConnection conn) {
        activeCount--;
        return freePool.offer(conn);
    }

    /**
     * 创建新连接
     *
     * @return 新连接
     * @throws SQLException
     */
    public PooledConnection newConnection() throws SQLException {
        return new PooledConnection(this);
    }

    public DbConfig getConfig() {
        return config;
    }

    /**
     * 获取连接对象
     *
     * @param wait 当池中无连接等待的毫秒数
     * @return 连接对象
     * @throws SQLException
     */
    public PooledConnection getConnection(long wait) throws SQLException {
        try {
            return getConnectionDirect();
        } catch (Exception e) {
            ThreadUtil.sleep(wait);
        }
        return getConnectionDirect();
    }

    @Override
    public synchronized void close() throws IOException {
        if (CollectionCommand.isNotEmpty(this.freePool)) {
            for (PooledConnection pooledConnection : freePool) {
                pooledConnection.release();
                this.freePool.clear();
                this.freePool = null;
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        IoCommand.close(this);
    }

    /**
     * 直接从连接池中获取连接，如果池中无连接直接抛出异常
     *
     * @return PooledConnection
     * @throws SQLException
     */
    private PooledConnection getConnectionDirect() throws SQLException {
        if (null == freePool) {
            throw new SQLException("PooledDataSource is closed!");
        }

        int maxActive = config.getMaxActive();
        if (maxActive <= 0 || maxActive < this.activeCount) {
            // 超过最大使用限制
            throw new SQLException("In used Connection is more than Max Active.");
        }

        PooledConnection conn = freePool.poll();
        if (null == conn) {
            conn = this.newConnection();
        }
        activeCount++;
        return conn.open();
    }

}
