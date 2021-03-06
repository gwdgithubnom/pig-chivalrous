package org.gjgr.pig.chivalrous.db.ds.simple;

import org.gjgr.pig.chivalrous.core.io.IoCommand;
import org.gjgr.pig.chivalrous.core.lang.CollectionCommand;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.gjgr.pig.chivalrous.core.setting.Setting;
import org.gjgr.pig.chivalrous.db.ds.DSFactory;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简单数据源工厂类
 *
 * @author Looly
 */
public class SimpleDSFactory extends DSFactory {

    public static final String DS_NAME = "Hutool-Simple-DataSource";

    /**
     * 数据源池
     */
    private Map<String, SimpleDataSource> dsMap;

    public SimpleDSFactory() {
        this(null);
    }

    public SimpleDSFactory(Setting setting) {
        super(DS_NAME, setting);
        this.dsMap = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized DataSource getDataSource(String group) {
        // 如果已经存在已有数据源（连接池）直接返回
        final SimpleDataSource existedDataSource = dsMap.get(group);
        if (existedDataSource != null) {
            return existedDataSource;
        }

        final SimpleDataSource ds = createDataSource(group);
        // 添加到数据源池中，以备下次使用
        dsMap.put(group, ds);
        return ds;
    }

    @Override
    public void close(String group) {
        if (group == null) {
            group = StringCommand.EMPTY;
        }

        SimpleDataSource ds = dsMap.get(group);
        if (ds != null) {
            IoCommand.close(ds);
            dsMap.remove(group);
        }
    }

    @Override
    public void destroy() {
        if (CollectionCommand.isNotEmpty(dsMap)) {
            Collection<SimpleDataSource> values = dsMap.values();
            for (SimpleDataSource ds : values) {
                IoCommand.close(ds);
            }
            dsMap.clear();
        }
    }

    /**
     * 创建数据源
     *
     * @param group 分组
     * @return 简单数据源 {@link SimpleDataSource}
     */
    private SimpleDataSource createDataSource(String group) {
        final SimpleDataSource ds = new SimpleDataSource(setting, group);
        return ds;
    }
}
