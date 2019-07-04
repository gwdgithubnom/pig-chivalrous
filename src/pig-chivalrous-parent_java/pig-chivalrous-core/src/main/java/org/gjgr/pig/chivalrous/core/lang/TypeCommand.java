package org.gjgr.pig.chivalrous.core.lang;

import org.apache.commons.lang3.Validate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author gwd
 * @Time 10-29-2018 Monday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public class TypeCommand {

    /**
     * Try convert {@link Number} value to given type. <br>
     * If the value is not a {@link Number}, or it's class equals the given returnType, the original value will be
     * returned.
     *
     * @param value
     * @param returnType
     * @return
     */
    public Object tryConvertNumber(Object value, Class<?> returnType) {
        Validate.notNull(value, "value is null");
        if (!(value instanceof Number) || value.getClass().equals(returnType)) {
            return value;
        }
        Number number = (Number) value;
        if (returnType == int.class || returnType == Integer.class) {
            return Integer.valueOf(number.intValue());
        }
        if (returnType == long.class || returnType == Long.class) {
            return Long.valueOf(number.longValue());
        }
        if (returnType == short.class || returnType == Short.class) {
            return Short.valueOf(number.shortValue());
        }
        if (returnType == double.class || returnType == Double.class) {
            return Double.valueOf(number.doubleValue());
        }
        if (returnType == float.class || returnType == Float.class) {
            return Float.valueOf(number.floatValue());
        }
        return value;
    }

    /**
     * Get the default value of the given return type.
     *
     * @param returnType
     * @return
     */
    public Object getDefaultReturn(Class<?> returnType) {
        if (List.class.isAssignableFrom(returnType)) {
            return Collections.EMPTY_LIST;
        }
        if (Set.class.isAssignableFrom(returnType)) {
            return Collections.EMPTY_SET;
        }
        if (Map.class.isAssignableFrom(returnType)) {
            return Collections.EMPTY_MAP;
        }
        if (returnType == int.class) {
            return Integer.valueOf(0);
        }
        if (returnType == long.class) {
            return Long.valueOf(0L);
        }
        if (returnType == short.class) {
            return Short.valueOf((short) 0);
        }
        if (returnType == double.class) {
            return Double.valueOf(0.0);
        }
        if (returnType == Float.class) {
            return Float.valueOf(0.0F);
        }
        return null;
    }
}
