package org.gjgr.pig.chivalrous.core.lang;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.lang.model.type.PrimitiveType;
import org.gjgr.pig.chivalrous.log.SystemLogger;

/**
 * @Author gwd
 * @Time 12-04-2019  Wednesday
 * @Description: gjgrparent:
 * @Target:
 * @More:
 */
public class ObjectWrapper<T> extends BaseObjectWrapper<T> {

    private Class<T> clazz;

    public ObjectWrapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    public ObjectWrapper() {
        Type genType = getClass().getGenericSuperclass();
        try {
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            clazz = (Class<T>) params[0];
        } catch (Exception e) {
            throw new RuntimeException("no support outer class T, " + e.getMessage());
        }
    }

    /**
     * return not list, map, set to null
     *
     * @param t
     * @return
     */
    public T wrapperWithEmpty(T t) {
        if (t == null) {
            if (clazz.isAssignableFrom(List.class)) {
                return (T) new ArrayList(0);
            } else if (clazz.isAssignableFrom(Map.class)) {
                return (T) new HashMap(0);
            } else if (clazz.isAssignableFrom(Set.class)) {
                return (T) new HashSet(0);
            } else {
                return null;
            }
        } else {
            return t;
        }
    }

    /**
     * return the list and map, set count with 0 to null
     *
     * @param t
     * @return
     */
    public T wrapperWithNull(T t) {
        if (t != null) {
            if (clazz.isAssignableFrom(List.class)) {
                List tt = (List) t;
                if (tt.size() == 0) {
                    return null;
                } else {
                    return t;
                }
            } else if (clazz.isAssignableFrom(Map.class)) {
                Map tt = (Map) t;
                if (tt.size() == 0) {
                    return null;
                } else {
                    return t;
                }
            } else if (clazz.isAssignableFrom(Set.class)) {
                Set tt = (Set) t;
                if (tt.size() == 0) {
                    return null;
                } else {
                    return t;
                }
            } else {
                return t;
            }
        } else {
            return t;
        }
    }

    /**
     * wrapper the entity
     *
     * @param t
     * @return would instance a target object
     */
    public T wrapperNotNull(T t) {
        if (t == null) {
            try {
                if (clazz.isAssignableFrom(List.class)) {
                    return (T) new ArrayList(0);
                } else if (clazz.isAssignableFrom(Map.class)) {
                    return (T) new HashMap(0);
                } else if (clazz.isAssignableFrom(Set.class)) {
                    return (T) new HashSet(0);
                } else {
                    return clazz.newInstance();
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
                throw new RuntimeException("entity Class init default value failed " + clazz);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException("entity Class init default value failed " + clazz);
            }
        } else {
            return t;
        }
    }

    @Override
    public Class<T> getTClass() {
        Class<T> tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }

}
