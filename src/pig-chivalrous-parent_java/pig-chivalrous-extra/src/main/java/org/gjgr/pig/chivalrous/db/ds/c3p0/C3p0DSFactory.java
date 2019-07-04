package org.gjgr.pig.chivalrous.db.ds.c3p0;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.gjgr.pig.chivalrous.core.lang.CollectionCommand;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.gjgr.pig.chivalrous.core.setting.Setting;
import org.gjgr.pig.chivalrous.db.DbRuntimeException;
import org.gjgr.pig.chivalrous.db.DbUtil;
import org.gjgr.pig.chivalrous.db.ds.DSFactory;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Druid数据源工厂类
 *
 * @author Looly
 */
public class C3p0DSFactory extends DSFactory {

    public static final String DS_NAME = "C3P0";

    /**
     * 数据源池
     */
    private Map<String, ComboPooledDataSource> dsMap;

    public C3p0DSFactory() {
        this(null);
    }

    public C3p0DSFactory(Setting setting) {
        super(DS_NAME, setting);
        checkCPExist(ComboPooledDataSource.class);
        this.dsMap = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized DataSource getDataSource(String group) {
        if (group == null) {
            group = StringCommand.EMPTY;
        }

        // 如果已经存在已有数据源（连接池）直接返回
        final ComboPooledDataSource existedDataSource = dsMap.get(group);
        if (existedDataSource != null) {
            return existedDataSource;
        }

        ComboPooledDataSource ds = createDataSource(group);
        // 添加到数据源池中，以备下次使用
        dsMap.put(group, ds);
        return ds;
    }

    @Override
    public void close(String group) {
        if (group == null) {
            group = StringCommand.EMPTY;
        }

        ComboPooledDataSource ds = dsMap.get(group);
        if (ds != null) {
            ds.close();
            dsMap.remove(group);
        }
    }

    @Override
    public void destroy() {
        if (CollectionCommand.isNotEmpty(dsMap)) {
            Collection<ComboPooledDataSource> values = dsMap.values();
            for (ComboPooledDataSource ds : values) {
                ds.close();
            }
            dsMap.clear();
        }
    }

    /**
     * 创建数据源
     *
     * @param group 分组
     * @return C3P0数据源 {@link ComboPooledDataSource}
     */
    private ComboPooledDataSource createDataSource(String group) {
        final Setting config = setting.getSetting(group);
        if (CollectionCommand.isEmpty(config)) {
            throw new DbRuntimeException("No C3P0 config for group: [{}]", group);
        }

        final ComboPooledDataSource ds = new ComboPooledDataSource();

        // 基本信息
        ds.setJdbcUrl(getAndRemoveProperty(config, "url", "jdbcUrl"));
        ds.setUser(getAndRemoveProperty(config, "username", "user"));
        ds.setPassword(getAndRemoveProperty(config, "password", "pass"));
        final String driver = getAndRemoveProperty(config, "driver", "driverClassName");
        try {
            if (StringCommand.isNotBlank(driver)) {
                ds.setDriverClass(driver);
            } else {
                ds.setDriverClass(DbUtil.identifyDriver(ds.getJdbcUrl()));
            }
        } catch (Exception e) {
            throw new DbRuntimeException(e);
        }

        config.toBean(ds);// 注入属性

        return ds;
    }

    /**
     * 获得指定KEY对应的值，key1和key2为属性的两个名字，可以互作别名
     *
     * @param setting 属性
     * @param key1    属性名
     * @param key2    备用属性名
     * @return 值
     */
    private String getAndRemoveProperty(Setting setting, String key1, String key2) {
        String value = (String) setting.remove(key1);
        if (StringCommand.isBlank(value)) {
            value = (String) setting.remove(key2);
        }
        return value;
    }
}
