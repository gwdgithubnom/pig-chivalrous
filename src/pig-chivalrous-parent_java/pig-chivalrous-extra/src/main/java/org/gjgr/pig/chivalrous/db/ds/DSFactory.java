package org.gjgr.pig.chivalrous.db.ds;

import org.gjgr.pig.chivalrous.core.log.Log;
import org.gjgr.pig.chivalrous.core.log.LogFactory;
import org.gjgr.pig.chivalrous.core.setting.Setting;
import org.gjgr.pig.chivalrous.core.util.StrUtil;
import org.gjgr.pig.chivalrous.db.ds.c3p0.C3p0DSFactory;
import org.gjgr.pig.chivalrous.db.ds.druid.DruidDSFactory;
import org.gjgr.pig.chivalrous.db.ds.hikari.HikariDSFactory;
import org.gjgr.pig.chivalrous.db.ds.pooled.PooledDSFactory;
import org.gjgr.pig.chivalrous.db.ds.tomcat.TomcatDSFactory;

import javax.sql.DataSource;

/**
 * 数据源工厂类
 *
 * @author Looly
 */
public abstract class DSFactory {
    protected static final String DEFAULT_DB_SETTING_PATH = "config/db.setting";
    private static final Log log = LogFactory.get();
    private static final Object lock = new Object();
    private static DSFactory currentDSFactory;

    //------------------------------------------------------------------------- Static start
    //JVM关闭是关闭所有连接池
    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (null != currentDSFactory) {
                    currentDSFactory.destroy();
                    log.debug("DataSource: [{}] destroyed.", currentDSFactory.dataSourceName);
                }
            }
        });
    }

    /**
     * 数据库连接配置文件
     */
    protected Setting setting;
    /**
     * 数据源名
     */
    private String dataSourceName;

    /**
     * 构造
     *
     * @param dataSourceName 数据源名称
     * @param setting        数据库连接配置
     */
    public DSFactory(String dataSourceName, Setting setting) {
        this.dataSourceName = dataSourceName;
        if (null == setting) {
            setting = new Setting(DEFAULT_DB_SETTING_PATH, true);
        }
        this.setting = setting;
    }

    /**
     * 获得数据源<br>
     * 使用默认配置文件的无分组配置
     *
     * @return 数据源
     */
    public static DataSource get() {
        return get(null);
    }

    /**
     * 获得数据源
     *
     * @param group 配置文件中对应的分组
     * @return 数据源
     */
    public static DataSource get(String group) {
        return get(null, group);
    }

    /**
     * 获得数据源
     *
     * @param dbSetting 数据库配置文件，如果为<code>null</code>，查找默认的配置文件
     * @param group     配置文件中对应的分组
     * @return 数据源
     */
    public static DataSource get(Setting dbSetting, String group) {
        return getCurrentDSFactory(dbSetting).getDataSource(group);
    }

    /**
     * @param setting 数据源配置文件
     * @return 当前使用的数据源工厂
     */
    public static DSFactory getCurrentDSFactory(Setting setting) {
        if (null == currentDSFactory) {
            synchronized (lock) {
                if (null == currentDSFactory) {
                    currentDSFactory = detectDSFactory(setting);
                }
            }
        }
        return currentDSFactory;
    }

    /**
     * @param dsFactory 数据源工厂
     * @return 自定义的数据源工厂
     */
    synchronized public static DSFactory setCurrentDSFactory(DSFactory dsFactory) {
        if (null != currentDSFactory) {
            if (currentDSFactory.equals(dsFactory)) {
                return currentDSFactory;//数据源不变时返回原数据源
            }
            //自定义数据源工厂前关闭之前的数据源
            currentDSFactory.destroy();
        }

        log.debug("Custom use [{}] datasource.", dsFactory.dataSourceName);
        currentDSFactory = dsFactory;
        return currentDSFactory;
    }

    /**
     * 决定数据源实现工厂<br>
     * 连接池优先级：Hikari > Druid > Tomcat > Dbcp > C3p0 > Hutool Pooled
     *
     * @return 日志实现类
     */
    private static DSFactory detectDSFactory(Setting setting) {
        DSFactory dsFactory;
        try {
            dsFactory = new HikariDSFactory(setting);
        } catch (NoClassDefFoundError e1) {
            try {
                dsFactory = new DruidDSFactory(setting);
            } catch (NoClassDefFoundError e2) {
                try {
                    dsFactory = new TomcatDSFactory(setting);
                } catch (NoClassDefFoundError e3) {
                    try {
                        dsFactory = new C3p0DSFactory(setting);
                    } catch (NoClassDefFoundError e5) {
                        //默认使用Hutool实现的简易连接池
                        dsFactory = new PooledDSFactory(setting);
                    }
                }
            }
        }
        log.debug("Use [{}] DataSource As Default", dsFactory.dataSourceName);
        return dsFactory;
    }

    /**
     * 获得默认数据源
     *
     * @return 数据源
     */
    public DataSource getDataSource() {
        return getDataSource(StrUtil.EMPTY);
    }

    /**
     * 获得分组对应数据源
     *
     * @param group 分组名
     * @return 数据源
     */
    public abstract DataSource getDataSource(String group);

    /**
     * 关闭默认数据源（空组）
     */
    public void close() {
        close(StrUtil.EMPTY);
    }

    /**
     * 关闭对应数据源
     *
     * @param group
     */
    public abstract void close(String group);

    /**
     * 销毁工厂类，关闭所有数据源
     */
    public abstract void destroy();

    /**
     * 检查连接池（Connection Pool）实现是否存在<br>
     * 此方法仅用于检查所提供的DataSource类是否存在，当传入的DataSource类不存在时抛出ClassNotFoundException<br>
     * 此方法的作用是在detectDSFactory方法自动检测所用连接池时，如果实现类不存在，调用此方法会自动抛出异常，从而切换到下一种连接池的检测。
     *
     * @param dsClass DataSource子类
     */
    protected void checkCPExist(Class<? extends DataSource> dsClass) {
        //Do nothing only use datasource class for check exist or not.
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataSourceName == null) ? 0 : dataSourceName.hashCode());
        result = prime * result + ((setting == null) ? 0 : setting.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DSFactory other = (DSFactory) obj;
        if (dataSourceName == null) {
            if (other.dataSourceName != null) {
                return false;
            }
        } else if (!dataSourceName.equals(other.dataSourceName)) {
            return false;
        }
        if (setting == null) {
            if (other.setting != null) {
                return false;
            }
        } else if (!setting.equals(other.setting)) {
            return false;
        }
        return true;
    }
    //------------------------------------------------------------------------- Static end
}
