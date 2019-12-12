package org.gjgr.pig.chivalrous.lang;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.junit.Test;

/**
 * @Author gwd
 * @Time 12-05-2019  Thursday
 * @Description: gjgrparent:
 * @Target:
 * @More:
 */
public class TypeDebug {

    @Test
    public void test() {
        check(List.class);
        check(Queue.class);
        check(Integer.class);
        check(1);
        check(2l);
        check(HashMap.class);
        check(Debug.class);
    }

    public void check(Object o) {
        check(o.getClass());
    }

    public void check(Class clazz) {
        System.out.println("#############################" + clazz.getName());
        System.out.println("java.lang.reflect.Type "+clazz.isAssignableFrom(Type.class));
        System.out.println("java.lang.reflect.ParameterizedType "+clazz.isAssignableFrom(ParameterizedType.class));
        System.out.println("java.lang.reflect.GenericArrayType "+clazz.isAssignableFrom(GenericArrayType.class));
        System.out.println("java.lang.reflect.WildcardType "+clazz.isAssignableFrom(WildcardType.class));
        System.out.println("collection  "+Collection.class.isAssignableFrom(clazz));
        System.out.println("map  "+ Map.class.isAssignableFrom(clazz));
        System.out.println("number "+Number.class.isAssignableFrom(clazz));


        System.out.println("isPrimitive "+clazz.isPrimitive());
        System.out.println("isArray "+clazz.isArray());
        System.out.println("isAnnotation"+clazz.isAnnotation());
        System.out.println("isEnum "+clazz.isEnum());
        System.out.println("isInterface "+clazz.isInterface());
        System.out.println("isLocalClass "+clazz.isLocalClass());
        System.out.println("isMemberClass "+clazz.isMemberClass());
        System.out.println("isSynthetic "+clazz.isSynthetic());
        System.out.println("isAnonymousClass "+clazz.isAnonymousClass());
        System.out.println("isSynthetic "+clazz.isSynthetic());
        System.out.println("#################################################");

    }
}
