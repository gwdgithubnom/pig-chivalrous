package org.gjgr.pig.chivalrous.db.sql;

import org.gjgr.pig.chivalrous.core.lang.ArrayCommand;
import org.gjgr.pig.chivalrous.core.lang.CollectionCommand;
import org.gjgr.pig.chivalrous.core.lang.ObjectCommand;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.gjgr.pig.chivalrous.core.log.Log;
import org.gjgr.pig.chivalrous.core.log.StaticLog;
import org.gjgr.pig.chivalrous.db.DbRuntimeException;
import org.gjgr.pig.chivalrous.db.DbUtil;
import org.gjgr.pig.chivalrous.db.Entity;
import org.gjgr.pig.chivalrous.db.dialect.DialectName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

/**
 * SQL构建器<br>
 * 首先拼接SQL语句，值使用 ? 占位<br>
 * 调用getParamValues()方法获得占位符对应的值
 *
 * @author Looly
 */
public class SqlBuilder {
    private static final Log log = StaticLog.get();

    private static boolean showSql;
    private static boolean formatSql;

    // --------------------------------------------------------------- Static methods start
    private final StringBuilder sql = new StringBuilder();
    /**
     * 占位符对应的值列表
     */
    private final List<Object> paramValues = new ArrayList<Object>();
    /**
     * 包装器
     */
    private Wrapper wrapper;
    // --------------------------------------------------------------- Static methods end

    // --------------------------------------------------------------- Enums start

    // --------------------------------------------------------------- Constructor start
    public SqlBuilder() {
    }
    // --------------------------------------------------------------- Enums end

    public SqlBuilder(Wrapper wrapper) {
        this.wrapper = wrapper;
    }

    /**
     * 创建SQL构建器
     *
     * @return SQL构建器
     */
    public static SqlBuilder create() {
        return new SqlBuilder();
    }

    /**
     * 创建SQL构建器
     *
     * @param wrapper 包装器
     * @return SQL构建器
     */
    public static SqlBuilder create(Wrapper wrapper) {
        return new SqlBuilder(wrapper);
    }

    /**
     * 设置全局配置：是否通过debug日志显示SQL
     *
     * @param isShowSql   是否显示SQL
     * @param isFormatSql 是否格式化显示的SQL
     */
    public static void setShowSql(boolean isShowSql, boolean isFormatSql) {
        showSql = isShowSql;
        formatSql = isFormatSql;
    }

    /**
     * 插入，使用默认的ANSI方言
     *
     * @param entity 实体
     * @return 自己
     */
    public SqlBuilder insert(Entity entity) {
        return this.insert(entity, DialectName.ANSI);
    }
    // --------------------------------------------------------------- Constructor end

    // --------------------------------------------------------------- Builder start

    /**
     * 插入
     *
     * @param entity      实体
     * @param dialectName 方言名
     * @return 自己
     */
    public SqlBuilder insert(Entity entity, DialectName dialectName) {
        // 验证
        DbUtil.validateEntity(entity);

        if (null != wrapper) {
            // 包装字段名
            entity = wrapper.wrap(entity);
        }

        final boolean isOracle = ObjectCommand.equal(dialectName, DialectName.ORACLE); // 对Oracle的特殊处理
        final StringBuilder fields = new StringBuilder();
        final StringBuilder placeHolder = new StringBuilder();

        boolean isFirst = true;
        String field;
        Object value;
        for (Entry<String, Object> entry : entity.entrySet()) {
            field = entry.getKey();
            value = entry.getValue();
            if (StringCommand.isNotBlank(field) && null != value) {
                // 只对值为非空的数据做插入操作
                if (isFirst) {
                    isFirst = false;
                } else {
                    // 非第一个参数，追加逗号
                    fields.append(", ");
                    placeHolder.append(", ");
                }

                fields.append(field);
                if (isOracle && value instanceof String
                        && StringCommand.endWithIgnoreCase((String) value, ".nextval")) {
                    // Oracle的特殊自增键，通过字段名.nextval获得下一个值
                    placeHolder.append(value);
                } else {
                    placeHolder.append("?");
                    paramValues.add(value);
                }
            }
        }
        sql.append("INSERT INTO ")
                .append(entity.getTableName()).append(" (").append(fields).append(") VALUES (")
                .append(placeHolder.toString()).append(")");

        return this;
    }

    /**
     * 删除
     *
     * @param tableName 表名
     * @return 自己
     */
    public SqlBuilder delete(String tableName) {
        if (StringCommand.isBlank(tableName)) {
            throw new DbRuntimeException("Table name is blank !");
        }

        if (null != wrapper) {
            // 包装表名
            tableName = wrapper.wrap(tableName);
        }

        sql.append("DELETE FROM ").append(tableName);

        return this;
    }

    /**
     * 更新
     *
     * @param entity 要更新的实体
     * @return 自己
     */
    public SqlBuilder update(Entity entity) {
        // 验证
        DbUtil.validateEntity(entity);

        if (null != wrapper) {
            // 包装字段名
            entity = wrapper.wrap(entity);
        }

        sql.append("UPDATE ").append(entity.getTableName()).append(" SET ");
        String key;
        for (Entry<String, Object> entry : entity.entrySet()) {
            key = entry.getKey();
            if (StringCommand.isNotBlank(key)) {
                if (paramValues.size() > 0) {
                    sql.append(", ");
                }
                sql.append(entry.getKey()).append(" = ? ");
                paramValues.add(entry.getValue());// 更新不对空做处理，因为存在清空字段的情况
            }
        }

        return this;
    }

    /**
     * 查询
     *
     * @param isDistinct 是否添加DISTINCT关键字（查询唯一结果）
     * @param fields     查询的字段
     * @return 自己
     */
    public SqlBuilder select(boolean isDistinct, String... fields) {
        return select(isDistinct, Arrays.asList(fields));
    }

    /**
     * 查询
     *
     * @param isDistinct 是否添加DISTINCT关键字（查询唯一结果）
     * @param fields     查询的字段
     * @return 自己
     */
    public SqlBuilder select(boolean isDistinct, Collection<String> fields) {
        sql.append("SELECT ");
        if (isDistinct) {
            sql.append("DISTINCT ");
        }

        if (CollectionCommand.isEmpty(fields)) {
            sql.append("*");
        } else {
            if (null != wrapper) {
                // 包装字段名
                fields = wrapper.wrap(fields);
            }
            sql.append(CollectionCommand.join(fields, StringCommand.COMMA));
        }

        return this;
    }

    /**
     * 查询（非Distinct）
     *
     * @param fields 查询的字段
     * @return 自己
     */
    public SqlBuilder select(String... fields) {
        return select(false, fields);
    }

    /**
     * 查询（非Distinct）
     *
     * @param fields 查询的字段
     * @return 自己
     */
    public SqlBuilder select(Collection<String> fields) {
        return select(false, fields);
    }

    /**
     * 添加 from语句
     *
     * @param tableNames 表名列表（多个表名用于多表查询）
     * @return 自己
     */
    public SqlBuilder from(String... tableNames) {
        if (ArrayCommand.isEmpty(tableNames) || StringCommand.hasBlank(tableNames)) {
            throw new DbRuntimeException("Table name is blank in table names !");
        }

        if (null != wrapper) {
            // 包装表名
            tableNames = wrapper.wrap(tableNames);
        }

        sql.append(" FROM ").append(ArrayCommand.join(tableNames, StringCommand.COMMA));

        return this;
    }

    /**
     * 添加Where语句<br>
     * 只支持单一的逻辑运算符（例如多个条件之间）
     *
     * @param logicalOperator 逻辑运算符
     * @param conditions      条件，当条件为空时，只添加WHERE关键字
     * @return 自己
     */
    public SqlBuilder where(LogicalOperator logicalOperator, Condition... conditions) {
        if (ArrayCommand.isNotEmpty(conditions)) {
            if (null != wrapper) {
                // 包装字段名
                conditions = wrapper.wrap(conditions);
            }
            where(buildCondition(logicalOperator, conditions));
        }

        return this;
    }

    /**
     * 添加Where语句<br>
     *
     * @param where WHERE语句之后跟的条件语句字符串
     * @return 自己
     */
    public SqlBuilder where(String where) {
        if (StringCommand.isNotBlank(where)) {
            sql.append(" WHERE ").append(where);
        }
        return this;
    }

    /**
     * 多值选择
     *
     * @param field  字段名
     * @param values 值列表
     * @return 自身
     */
    @SuppressWarnings("unchecked")
    public <T> SqlBuilder in(String field, T... values) {
        sql.append(wrapper.wrap(field)).append(" IN ").append("(")
                .append(ArrayCommand.join(values, StringCommand.COMMA))
                .append(")");
        return this;
    }

    /**
     * 分组
     *
     * @param fields 字段
     * @return 自己
     */
    public SqlBuilder groupBy(String... fields) {
        if (ArrayCommand.isNotEmpty(fields)) {
            if (null != wrapper) {
                // 包装字段名
                fields = wrapper.wrap(fields);
            }

            sql.append(" GROUP BY ").append(ArrayCommand.join(fields, StringCommand.COMMA));
        }

        return this;
    }

    /**
     * 添加Having语句
     *
     * @param logicalOperator 逻辑运算符
     * @param conditions      条件
     * @return 自己
     */
    public SqlBuilder having(LogicalOperator logicalOperator, Condition... conditions) {
        if (ArrayCommand.isNotEmpty(conditions)) {
            if (null != wrapper) {
                // 包装字段名
                conditions = wrapper.wrap(conditions);
            }
            having(buildCondition(logicalOperator, conditions));
        }

        return this;
    }

    /**
     * 添加Having语句
     *
     * @param having 条件语句
     * @return 自己
     */
    public SqlBuilder having(String having) {
        if (StringCommand.isNotBlank(having)) {
            sql.append(" HAVING ").append(having);
        }
        return this;
    }

    /**
     * 排序
     *
     * @param orders 排序对象
     * @return 自己
     */
    public SqlBuilder orderBy(Order... orders) {
        if (ArrayCommand.isEmpty(orders)) {
            return this;
        }

        sql.append(" ORDER BY ");
        String field = null;
        boolean isFirst = true;
        for (Order order : orders) {
            if (null != wrapper) {
                // 包装字段名
                field = wrapper.wrap(order.getField());
            }
            if (StringCommand.isBlank(field)) {
                continue;
            }

            // 只有在非第一项前添加逗号
            if (isFirst) {
                isFirst = false;
            } else {
                sql.append(StringCommand.COMMA);
            }
            sql.append(field);
            final Direction direction = order.getDirection();
            if (null != direction) {
                sql.append(StringCommand.SPACE).append(direction);
            }
        }
        return this;
    }

    /**
     * 多表关联
     *
     * @param tableName 被关联的表名
     * @param join      内联方式
     * @return 自己
     */
    public SqlBuilder join(String tableName, Join join) {
        if (StringCommand.isBlank(tableName)) {
            throw new DbRuntimeException("Table name is blank !");
        }

        if (null != join) {
            sql.append(StringCommand.SPACE).append(join).append(" JOIN ");
            if (null != wrapper) {
                // 包装表名
                tableName = wrapper.wrap(tableName);
            }
            sql.append(tableName);
        }
        return this;
    }

    /**
     * 配合JOIN的 ON语句，多表关联的条件语句<br>
     * 只支持单一的逻辑运算符（例如多个条件之间）
     *
     * @param logicalOperator 逻辑运算符
     * @param conditions      条件
     * @return 自己
     */
    public SqlBuilder on(LogicalOperator logicalOperator, Condition... conditions) {
        if (ArrayCommand.isNotEmpty(conditions)) {
            if (null != wrapper) {
                // 包装字段名
                conditions = wrapper.wrap(conditions);
            }
            on(buildCondition(logicalOperator, conditions));
        }

        return this;
    }

    /**
     * 配合JOIN的 ON语句，多表关联的条件语句<br>
     * 只支持单一的逻辑运算符（例如多个条件之间）
     *
     * @param on 条件
     * @return 自己
     */
    public SqlBuilder on(String on) {
        if (StringCommand.isNotBlank(on)) {
            this.sql.append(" ON ").append(on);
        }
        return this;
    }

    /**
     * 追加SQL其它部分
     *
     * @param sqlPart SQL其它部分
     * @return 自己
     */
    public SqlBuilder append(Object sqlPart) {
        if (null != sqlPart) {
            this.sql.append(sqlPart);
        }

        return this;
    }

    /**
     * 构建查询SQL
     *
     * @param query {@link Query}
     * @return this
     */
    public SqlBuilder query(Query query) {
        return this
                .select(query.getFields())
                .from(query.getTableNames())
                .where(LogicalOperator.AND, query.getWhere());
    }

    /**
     * 获得占位符对应的值列表<br>
     *
     * @return 占位符对应的值列表
     */
    public List<Object> getParamValues() {
        return paramValues;
    }
    // --------------------------------------------------------------- Builder end

    /**
     * 获得占位符对应的值列表<br>
     *
     * @return 占位符对应的值列表
     */
    public Object[] getParamValueArray() {
        return paramValues.toArray(new Object[paramValues.size()]);
    }

    /**
     * 构建
     *
     * @return 构建好的SQL语句
     */
    public String build() {
        return this.build(showSql);
    }

    /**
     * 构建
     *
     * @param isShowDebugSql 显示SQL的debug日志
     * @return 构建好的SQL语句
     */
    public String build(boolean isShowDebugSql) {
        final String sqlStr = this.sql.toString().trim();
        if (isShowDebugSql) {
            log.debug("\n{}", formatSql ? SqlFormatter.format(sqlStr) : sqlStr);
        }
        return sqlStr;
    }

    @Override
    public String toString() {
        return this.build();
    }

    /**
     * 构建组合条件
     *
     * @param logicalOperator 逻辑运算符
     * @param conditions      条件对象
     * @return 构建后的SQL语句条件部分
     */
    private String buildCondition(LogicalOperator logicalOperator, Condition... conditions) {
        if (ArrayCommand.isEmpty(conditions)) {
            return StringCommand.EMPTY;
        }
        if (null == logicalOperator) {
            logicalOperator = LogicalOperator.AND;
        }

        final StringBuilder conditionStr = new StringBuilder();
        boolean isFirst = true;
        for (Condition condition : conditions) {
            // 添加逻辑运算符
            if (isFirst) {
                isFirst = false;
            } else {
                conditionStr.append(StringCommand.SPACE).append(logicalOperator).append(StringCommand.SPACE);
            }

            // 添加条件表达式
            conditionStr.append(condition.getField()).append(StringCommand.SPACE).append(condition.getOperator());

            if (condition.isPlaceHolder()) {
                // 使用条件表达式占位符
                conditionStr.append(" ?");
                paramValues.add(condition.getValue());
            } else {
                // 直接使用条件值
                conditionStr.append(condition.getValue());
            }
        }

        return conditionStr.toString();
    }

    // --------------------------------------------------------------- private method start

    /**
     * SQL中多表关联用的关键字
     *
     * @author Looly
     */
    public static enum Join {
        /**
         * 如果表中有至少一个匹配，则返回行
         */
        INNER,
        /**
         * 即使右表中没有匹配，也从左表返回所有的行
         */
        LEFT,
        /**
         * 即使左表中没有匹配，也从右表返回所有的行
         */
        RIGHT,
        /**
         * 只要其中一个表中存在匹配，就返回行
         */
        FULL
    }
    // --------------------------------------------------------------- private method end
}
