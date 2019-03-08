package org.gjgr.pig.chivalrous.db.ds.jndi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.gjgr.pig.chivalrous.core.setting.Setting;
import org.gjgr.pig.chivalrous.db.DbRuntimeException;
import org.gjgr.pig.chivalrous.db.DbUtil;
import org.gjgr.pig.chivalrous.db.ds.DSFactory;

/**
 * JNDI数据源工厂类<br>
 * Setting配置样例：<br>
 * ---------------------<br>
 * [group]<br>
 * jndi = jdbc/TestDB<br>
 * ---------------------<br>
 *
 * @author Looly
 */
public class JndiDSFactory extends DSFactory {

    public static final String DS_NAME = "JNDI DataSource";

    private Setting setting;
    /**
     * 数据源池
     */
    private Map<String, DataSource> dsMap;

    public JndiDSFactory() {
        this(null);
    }

    public JndiDSFactory(Setting setting) {
        super(DS_NAME, setting);
        this.dsMap = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized DataSource getDataSource(String group) {
        if (group == null) {
            group = StringCommand.EMPTY;
        }

        // 如果已经存在已有数据源（连接池）直接返回
        final DataSource existedDataSource = dsMap.get(group);
        if (existedDataSource != null) {
            return existedDataSource;
        }

        final DataSource ds = createDataSource(group);
        // 添加到数据源池中，以备下次使用
        dsMap.put(group, ds);
        return ds;
    }

    @Override
    public void close(String group) {
        // JNDI Datasource not support close method
    }

    @Override
    public void destroy() {
        // JNDI Datasource not support destroy method
    }

    /**
     * 创建数据源
     *
     * @param group JNDI名
     * @return 数据源 {@link DataSource}
     */
    private DataSource createDataSource(String group) {
        if (group == null) {
            group = StringCommand.EMPTY;
        }

        String jndiName = setting.getByGroup("jndi", group);
        if (StringCommand.isEmpty(jndiName)) {
            throw new DbRuntimeException("No setting name [jndi] for group [{}]", group);
        }
        DataSource ds = DbUtil.getJndiDs(jndiName);

        return ds;
    }
}
