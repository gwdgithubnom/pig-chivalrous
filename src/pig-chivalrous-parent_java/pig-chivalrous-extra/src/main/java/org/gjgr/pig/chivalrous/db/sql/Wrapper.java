package org.gjgr.pig.chivalrous.db.sql;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map.Entry;

import org.gjgr.pig.chivalrous.core.lang.ArrayCommand;
import org.gjgr.pig.chivalrous.core.lang.CollectionCommand;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.gjgr.pig.chivalrous.db.Entity;

/**
 * 包装器<br>
 * 主要用于字段名的包装（在字段名的前后加字符，例如反引号来避免与数据库的关键字冲突）
 *
 * @author Looly
 */
public class Wrapper {

    /**
     * 前置包装符号
     */
    private Character preWrapQuote;
    /**
     * 后置包装符号
     */
    private Character sufWrapQuote;

    public Wrapper() {
    }

    /**
     * 构造
     *
     * @param wrapQuote 单包装字符
     */
    public Wrapper(Character wrapQuote) {
        this.preWrapQuote = wrapQuote;
        this.sufWrapQuote = wrapQuote;
    }

    /**
     * 包装符号
     *
     * @param preWrapQuote 前置包装符号
     * @param sufWrapQuote 后置包装符号
     */
    public Wrapper(Character preWrapQuote, Character sufWrapQuote) {
        this.preWrapQuote = preWrapQuote;
        this.sufWrapQuote = sufWrapQuote;
    }

    // --------------------------------------------------------------- Getters and Setters start

    /**
     * @return 前置包装符号
     */
    public char getPreWrapQuote() {
        return preWrapQuote;
    }

    /**
     * 设置前置包装的符号
     *
     * @param preWrapQuote 前置包装符号
     */
    public void setPreWrapQuote(Character preWrapQuote) {
        this.preWrapQuote = preWrapQuote;
    }

    /**
     * @return 后置包装符号
     */
    public char getSufWrapQuote() {
        return sufWrapQuote;
    }

    /**
     * 设置后置包装的符号
     *
     * @param sufWrapQuote 后置包装符号
     */
    public void setSufWrapQuote(Character sufWrapQuote) {
        this.sufWrapQuote = sufWrapQuote;
    }
    // --------------------------------------------------------------- Getters and Setters end

    /**
     * 包装字段名<br>
     * 有时字段与SQL的某些关键字冲突，导致SQL出错，因此需要将字段名用单引号或者反引号包装起来，避免冲突
     *
     * @param field 字段名
     * @return 包装后的字段名
     */
    public String wrap(String field) {
        if (preWrapQuote == null || sufWrapQuote == null || StringCommand.isBlank(field)) {
            return field;
        }

        // 如果已经包含包装的引号，返回原字符
        if (StringCommand.isSurround(field, preWrapQuote, sufWrapQuote)) {
            return field;
        }

        // 如果字段中包含通配符或者括号（字段通配符或者函数），不做包装
        if (field.contains("*") || field.contains("(")) {
            return field;
        }

        return StringCommand.format("{}{}{}", preWrapQuote, field, sufWrapQuote);
    }

    /**
     * 包装字段名<br>
     * 有时字段与SQL的某些关键字冲突，导致SQL出错，因此需要将字段名用单引号或者反引号包装起来，避免冲突
     *
     * @param fields 字段名
     * @return 包装后的字段名
     */
    public String[] wrap(String...fields) {
        if (ArrayCommand.isEmpty(fields)) {
            return fields;
        }

        String[] wrappedFields = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            wrappedFields[i] = wrap(fields[i]);
        }

        return wrappedFields;
    }

    /**
     * 包装字段名<br>
     * 有时字段与SQL的某些关键字冲突，导致SQL出错，因此需要将字段名用单引号或者反引号包装起来，避免冲突
     *
     * @param fields 字段名
     * @return 包装后的字段名
     */
    public Collection<String> wrap(Collection<String> fields) {
        if (CollectionCommand.isEmpty(fields)) {
            return fields;
        }

        return Arrays.asList(wrap(fields.toArray(new String[fields.size()])));
    }

    /**
     * 包装字段名<br>
     * 有时字段与SQL的某些关键字冲突，导致SQL出错，因此需要将字段名用单引号或者反引号包装起来，避免冲突
     *
     * @param entity 被包装的实体
     * @return 包装后的字段名
     */
    public Entity wrap(Entity entity) {
        if (null == entity) {
            return null;
        }

        final Entity wrapedEntity = new Entity();

        // wrap table name
        wrapedEntity.setTableName(wrap(entity.getTableName()));

        // wrap fields
        for (Entry<String, Object> entry : entity.entrySet()) {
            wrapedEntity.set(wrap(entry.getKey()), entry.getValue());
        }

        return wrapedEntity;
    }

    /**
     * 包装字段名<br>
     * 有时字段与SQL的某些关键字冲突，导致SQL出错，因此需要将字段名用单引号或者反引号包装起来，避免冲突
     *
     * @param conditions 被包装的实体
     * @return 包装后的字段名
     */
    public Condition[] wrap(Condition...conditions) {
        final Condition[] clonedConditions = new Condition[conditions.length];
        if (ArrayCommand.isNotEmpty(conditions)) {
            Condition clonedCondition;
            for (int i = 0; i < conditions.length; i++) {
                clonedCondition = conditions[i].clone();
                clonedCondition.setField(wrap(clonedCondition.getField()));
                clonedConditions[i] = clonedCondition;
            }
        }

        return clonedConditions;
    }
}
