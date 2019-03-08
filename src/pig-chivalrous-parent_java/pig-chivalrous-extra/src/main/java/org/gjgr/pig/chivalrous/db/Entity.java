package org.gjgr.pig.chivalrous.db;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gjgr.pig.chivalrous.core.io.IoCommand;
import org.gjgr.pig.chivalrous.core.lang.ArrayCommand;
import org.gjgr.pig.chivalrous.core.lang.ClassCommand;
import org.gjgr.pig.chivalrous.core.lang.CollectionCommand;
import org.gjgr.pig.chivalrous.core.lang.Dict;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.gjgr.pig.chivalrous.core.nio.CharsetCommand;

/**
 * 数据实体对象<br>
 * 数据实体类充当两个角色：<br>
 * 1. 数据的载体，一个Entity对应数据库中的一个row<br>
 * 2. SQL条件，Entity中的每一个字段对应一个条件，字段值对应条件的值
 *
 * @author loolly
 */
public class Entity extends Dict {
    private static final long serialVersionUID = -1951012511464327448L;

    // --------------------------------------------------------------- Static method start
    /* 表名 */
    private String tableName;
    /* 字段名列表，用于限制加入的字段的值 */
    private Set<String> fieldNames;

    // --------------------------------------------------------------- Constructor start
    public Entity() {
    }
    // --------------------------------------------------------------- Static method end

    /**
     * 构造
     *
     * @param tableName 数据表名
     */

    public Entity(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 创建Entity
     *
     * @return Entity
     */
    public static Entity create() {
        return new Entity();
    }

    /**
     * 创建Entity
     *
     * @param tableName 表名
     * @return Entity
     */
    public static Entity create(String tableName) {
        return new Entity(tableName);
    }

    /**
     * 将PO对象转为Entity
     *
     * @param <T>
     * @param bean Bean对象
     * @return Entity
     */
    public static <T> Entity parse(T bean) {
        return create(null).parseBean(bean);
    }
    // --------------------------------------------------------------- Constructor end

    // --------------------------------------------------------------- Getters and Setters start

    /**
     * @return 获得表名
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 设置表名
     *
     * @param tableName 表名
     * @return 本身
     */
    public Entity setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    /**
     * @return 字段集合
     */
    public Set<String> getFieldNames() {
        return this.fieldNames;
    }

    /**
     * 设置字段列表
     *
     * @param fieldNames 字段列表
     * @return 自身
     */
    public Entity setFieldNames(String...fieldNames) {
        if (ArrayCommand.isNotEmpty(fieldNames)) {
            this.fieldNames = CollectionCommand.newHashSet(fieldNames);
        }
        return this;
    }

    /**
     * 设置字段列表
     *
     * @param fieldNames 字段列表
     * @return 自身
     */
    public Entity setFieldNames(List<String> fieldNames) {
        if (CollectionCommand.isNotEmpty(fieldNames)) {
            this.fieldNames = new HashSet<String>(fieldNames);
        }
        return this;
    }

    /**
     * 添加字段列表
     *
     * @param fieldNames 字段列表
     * @return 自身
     */
    public Entity addFieldNames(String...fieldNames) {
        if (ArrayCommand.isNotEmpty(fieldNames)) {
            if (null == this.fieldNames) {
                return setFieldNames(fieldNames);
            } else {
                for (String fieldName : fieldNames) {
                    this.fieldNames.add(fieldName);
                }
            }
        }
        return this;
    }

    // --------------------------------------------------------------- Getters and Setters end

    /**
     * 将值对象转换为Entity<br>
     * 类名会被当作表名，小写第一个字母
     *
     * @param <T>
     * @param bean Bean对象
     * @return 自己
     */
    @Override
    public <T> Entity parseBean(T bean) {
        String tableName = bean.getClass().getSimpleName();
        tableName = StringCommand.lowerFirst(tableName);
        this.setTableName(tableName);

        return (Entity) super.parseBean(bean);
    }

    // -------------------------------------------------------------------- Put and Set start

    @Override
    public Entity set(String field, Object value) {
        return (Entity) super.set(field, value);
    }

    @Override
    public Entity setIgnoreNull(String field, Object value) {
        return (Entity) super.setIgnoreNull(field, value);
    }

    @Override
    public String getStr(String field) {
        final Object obj = get(field);
        if (obj instanceof Clob) {
            Clob clob = (Clob) obj;
            Reader reader = null;
            try {
                reader = clob.getCharacterStream();
                return IoCommand.read(reader);
            } catch (SQLException | IOException e) {
                throw new DbRuntimeException(e);
            } finally {
                IoCommand.close(reader);
            }
        } else if (obj instanceof RowId) {
            final RowId rowId = (RowId) obj;
            return StringCommand.str(rowId.getBytes(), CharsetCommand.UTF_8);
        }
        return super.getStr(field);
    }
    // -------------------------------------------------------------------- Put and Set end

    // -------------------------------------------------------------------- Get start

    @Override
    public Date getDate(String field) {
        Object obj = get(field);
        Date result = null;
        if (null != obj) {
            try {
                result = (Date) obj;
            } catch (Exception e) {
                // try oracle.sql.TIMESTAMP
                result = ClassCommand.invoke(obj, "dateValue", new Object[] {});
            }
        }
        return result;
    }

    @Override
    public Time getTime(String field) {
        Object obj = get(field);
        Time result = null;
        if (null != obj) {
            try {
                result = (Time) obj;
            } catch (Exception e) {
                // try oracle.sql.TIMESTAMP
                result = ClassCommand.invoke(obj, "timeValue", new Object[] {});
            }
        }
        return result;
    }

    @Override
    public Timestamp getTimestamp(String field) {
        Object obj = get(field);
        Timestamp result = null;
        if (null != obj) {
            try {
                result = (Timestamp) obj;
            } catch (Exception e) {
                // try oracle.sql.TIMESTAMP
                result = ClassCommand.invoke(obj, "timestampValue", new Object[] {});
            }
        }
        return result;
    }

    // -------------------------------------------------------------------- 特殊方法 start
    @Override
    public Entity clone() {
        return (Entity) super.clone();
    }

    /**
     * PUT方法做了过滤限制，如果此实体限制了属性名，则忽略限制名列表外的字段名
     *
     * @param key 名
     * @param value 值
     */
    @Override
    public Object put(String key, Object value) {
        if (CollectionCommand.isEmpty(fieldNames) || fieldNames.contains(key)) {
            super.put(key, value);
        }
        return null;
    }

    /**
     * 获得Clob类型结果
     *
     * @param field 参数
     * @return Clob
     */
    public Clob getClob(String field) {
        return get(field, null);
    }

    /**
     * 获得rowid
     *
     * @return RowId
     */
    public RowId getRowId() {
        return getRowId("ROWID");
    }

    // -------------------------------------------------------------------- Get end

    /**
     * 获得rowid
     *
     * @param field rowid属性名
     * @return RowId
     */
    public RowId getRowId(String field) {
        Object obj = this.get(field);
        if (null == obj) {
            return null;
        }
        if (obj instanceof RowId) {
            return (RowId) obj;
        }
        throw new DbRuntimeException("Value of field [{}] is not a rowid!", field);
    }
    // -------------------------------------------------------------------- 特殊方法 end

    @Override
    public String toString() {
        return "Entity {tableName=" + tableName + ", fieldNames=" + fieldNames + ", fields=" + super.toString() + "}";
    }
}
