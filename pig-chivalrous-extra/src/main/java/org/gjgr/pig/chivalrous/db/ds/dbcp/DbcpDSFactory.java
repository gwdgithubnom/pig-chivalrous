/*
 * package org.gjgr.pig.chivalrous.db.ds.dbcp;
 *
 * import org.apache.commons.dbcp2.BasicDataSource; import org.gjgr.pig.chivalrous.core.io.IoUtil; import
 * org.gjgr.pig.chivalrous.core.setting.Setting; import org.gjgr.pig.chivalrous.core.util.CollectionCommand; import
 * org.gjgr.pig.chivalrous.core.util.StringCommand; import org.gjgr.pig.chivalrous.db.DbRuntimeException; import
 * org.gjgr.pig.chivalrous.db.DbUtil; import org.gjgr.pig.chivalrous.db.ds.DSFactory;
 *
 *
 * import javax.sql.DataSource; import java.util.Collection; import java.util.Map; import
 * java.util.concurrent.ConcurrentHashMap;
 *
 */
/**
 * DBCP2数据源工厂类
 *
 * @author Looly
 * <p>
 * 数据源池
 * <p>
 * 创建数据源
 * @param group 分组
 * @return Dbcp数据源 {@link BasicDataSource}
 * <p>
 * 获得指定KEY对应的值，key1和key2为属性的两个名字，可以互作别名
 * @param setting 属性
 * @param key1 属性名
 * @param key2 备用属性名
 * @return 值
 * <p>
 * 数据源池
 * <p>
 * 创建数据源
 * @param group 分组
 * @return Dbcp数据源 {@link BasicDataSource}
 * <p>
 * 获得指定KEY对应的值，key1和key2为属性的两个名字，可以互作别名
 * @param setting 属性
 * @param key1 属性名
 * @param key2 备用属性名
 * @return 值
 * <p>
 * 数据源池
 * <p>
 * 创建数据源
 * @param group 分组
 * @return Dbcp数据源 {@link BasicDataSource}
 * <p>
 * 获得指定KEY对应的值，key1和key2为属性的两个名字，可以互作别名
 * @param setting 属性
 * @param key1 属性名
 * @param key2 备用属性名
 * @return 值
 * <p>
 * 数据源池
 * <p>
 * 创建数据源
 * @param group 分组
 * @return Dbcp数据源 {@link BasicDataSource}
 * <p>
 * 获得指定KEY对应的值，key1和key2为属性的两个名字，可以互作别名
 * @param setting 属性
 * @param key1 属性名
 * @param key2 备用属性名
 * @return 值
 * <p>
 * 数据源池
 * <p>
 * 创建数据源
 * @param group 分组
 * @return Dbcp数据源 {@link BasicDataSource}
 * <p>
 * 获得指定KEY对应的值，key1和key2为属性的两个名字，可以互作别名
 * @param setting 属性
 * @param key1 属性名
 * @param key2 备用属性名
 * @return 值
 * <p>
 * 数据源池
 * <p>
 * 创建数据源
 * @param group 分组
 * @return Dbcp数据源 {@link BasicDataSource}
 * <p>
 * 获得指定KEY对应的值，key1和key2为属性的两个名字，可以互作别名
 * @param setting 属性
 * @param key1 属性名
 * @param key2 备用属性名
 * @return 值
 * <p>
 * 数据源池
 * <p>
 * 创建数据源
 * @param group 分组
 * @return Dbcp数据源 {@link BasicDataSource}
 * <p>
 * 获得指定KEY对应的值，key1和key2为属性的两个名字，可以互作别名
 * @param setting 属性
 * @param key1 属性名
 * @param key2 备用属性名
 * @return 值
 * <p>
 * 数据源池
 * <p>
 * 创建数据源
 * @param group 分组
 * @return Dbcp数据源 {@link BasicDataSource}
 * <p>
 * 获得指定KEY对应的值，key1和key2为属性的两个名字，可以互作别名
 * @param setting 属性
 * @param key1 属性名
 * @param key2 备用属性名
 * @return 值
 * <p>
 * 数据源池
 * <p>
 * 创建数据源
 * @param group 分组
 * @return Dbcp数据源 {@link BasicDataSource}
 * <p>
 * 获得指定KEY对应的值，key1和key2为属性的两个名字，可以互作别名
 * @param setting 属性
 * @param key1 属性名
 * @param key2 备用属性名
 * @return 值
 * <p>
 * 数据源池
 * <p>
 * 创建数据源
 * @param group 分组
 * @return Dbcp数据源 {@link BasicDataSource}
 * <p>
 * 获得指定KEY对应的值，key1和key2为属性的两个名字，可以互作别名
 * @param setting 属性
 * @param key1 属性名
 * @param key2 备用属性名
 * @return 值
 *//*
 *
 * public class DbcpDSFactory extends DSFactory {
 *
 * public static final String DS_NAME = "Common-DBCP2";
 *
 */
/**
 * 数据源池
 *//*
 *
 * private Map<String, BasicDataSource> dsMap;
 *
 * public DbcpDSFactory() { this(null); }
 *
 * public DbcpDSFactory(Setting setting) { super(DS_NAME, setting); checkCPExist(BasicDataSource.class); this.dsMap =
 * new ConcurrentHashMap<>(); }
 *
 * @Override synchronized public DataSource getDataSource(String group) { if (group == null) { group =
 * StringCommand.EMPTY; }
 *
 * // 如果已经存在已有数据源（连接池）直接返回 final BasicDataSource existedDataSource = dsMap.get(group); if (existedDataSource != null)
 * { return existedDataSource; }
 *
 * BasicDataSource ds = createDataSource(group); // 添加到数据源池中，以备下次使用 dsMap.put(group, ds); return ds; }
 *
 * @Override public void close(String group) { if (group == null) { group = StringCommand.EMPTY; }
 *
 * BasicDataSource ds = dsMap.get(group); if (ds != null) { IoUtil.close(ds); dsMap.remove(group); } }
 *
 * @Override public void destroy() { if (CollectionCommand.isNotEmpty(dsMap)) { Collection<BasicDataSource> values =
 * dsMap.values(); for (BasicDataSource ds : values) { IoUtil.close(ds); } dsMap.clear(); } }
 *
 */
/**
 * 创建数据源
 *
 * @param group 分组
 * @return Dbcp数据源 {@link BasicDataSource}
 *//*
 *
 * private BasicDataSource createDataSource(String group) { final Setting config = setting.getSetting(group); if
 * (CollectionCommand.isEmpty(config)) { throw new DbRuntimeException("No DBCP config for group: [{}]", group); }
 *
 * final BasicDataSource ds = new BasicDataSource();
 *
 * //基本信息 ds.setUrl(getAndRemoveProperty(config, "url", "jdbcUrl")); ds.setUsername(getAndRemoveProperty(config,
 * "username", "user")); ds.setPassword(getAndRemoveProperty(config, "password", "pass")); final String driver =
 * getAndRemoveProperty(config, "driver", "driverClassName"); if (StringCommand.isNotBlank(driver)) {
 * ds.setDriverClassName(driver); } else { ds.setDriverClassName(DbUtil.identifyDriver(ds.getUrl())); }
 *
 * config.bean(ds);//注入属性 return ds; }
 *
 */
/**
 * 获得指定KEY对应的值，key1和key2为属性的两个名字，可以互作别名
 *
 * @param setting 属性
 * @param key1 属性名
 * @param key2 备用属性名
 * @return 值
 *//*
 *
 * private String getAndRemoveProperty(Setting setting, String key1, String key2) { String value = (String)
 * setting.remove(key1); if (StringCommand.isBlank(value)) { value = (String) setting.remove(key2); } return value; }
 * }
 */
