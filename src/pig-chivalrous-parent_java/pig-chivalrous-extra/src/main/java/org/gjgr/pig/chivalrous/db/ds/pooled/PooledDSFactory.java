package org.gjgr.pig.chivalrous.db.ds.pooled;

import org.gjgr.pig.chivalrous.core.io.IoCommand;
import org.gjgr.pig.chivalrous.core.lang.CollectionCommand;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.gjgr.pig.chivalrous.core.setting.Setting;
import org.gjgr.pig.chivalrous.db.DbRuntimeException;
import org.gjgr.pig.chivalrous.db.ds.DSFactory;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 池化数据源工厂类
 *
 * @author Looly
 */
public class PooledDSFactory extends DSFactory {

    public static final String DS_NAME = "Hutool-Pooled-DataSource";

    /**
     * 数据源池
     */
    private Map<String, PooledDataSource> dsMap;

    public PooledDSFactory() {
        this(null);
    }

    public PooledDSFactory(Setting setting) {
        super(DS_NAME, setting);
        this.dsMap = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized DataSource getDataSource(String group) {
        if (group == null) {
            group = StringCommand.EMPTY;
        }

        // 如果已经存在已有数据源（连接池）直接返回
        final PooledDataSource existedDataSource = dsMap.get(group);
        if (existedDataSource != null) {
            return existedDataSource;
        }

        final PooledDataSource ds = createDataSource(group);
        // 添加到数据源池中，以备下次使用
        dsMap.put(group, ds);
        return ds;
    }

    @Override
    public void close(String group) {
        if (group == null) {
            group = StringCommand.EMPTY;
        }

        PooledDataSource ds = dsMap.get(group);
        if (ds != null) {
            IoCommand.close(ds);
            dsMap.remove(group);
        }
    }

    @Override
    public void destroy() {
        if (CollectionCommand.isNotEmpty(dsMap)) {
            Collection<PooledDataSource> values = dsMap.values();
            for (PooledDataSource ds : values) {
                IoCommand.close(ds);
            }
            dsMap.clear();
        }
    }

    /**
     * 创建数据源
     *
     * @param group 分组
     * @return 池化数据源 {@link PooledDataSource}
     */
    private PooledDataSource createDataSource(String group) {
        if (group == null) {
            group = StringCommand.EMPTY;
        }

        Setting config = setting.getSetting(group);
        if (null == config || config.isEmpty()) {
            throw new DbRuntimeException("No PooledDataSource config for group: [{}]", group);
        }
        final PooledDataSource ds = new PooledDataSource(new DbSetting(config).getDbConfig(null));
        return ds;
    }
}
