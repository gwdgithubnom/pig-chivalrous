package org.gjgr.pig.chivalrous.lang;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import org.junit.Test;

/**
 * @Author gwd
 * @Time 12-04-2019  Wednesday
 * @Description: gjgrparent:
 * @Target:
 * @More:
 */
public class TestClass<T> {

    Debug debug = new Debug();

    public static void main(String s[]) {
        (new TestClass<Integer>() {
        }).test(); // class java.lang.String
    }

    public Class<T> getTClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    @Test
    public void test() {
        testMethod(1,2+"");
    }



    public void testMethod(int j, String a) {
        call(j,a);
    }

    public void call(Object... objects) {
        StackTraceElement s = Thread.currentThread().getStackTrace()[2];
        try {
           Method method = debug.getClass().getMethod(s.getMethodName(), extractClass(objects));
            try {
               Object object =  method.invoke(debug,objects);
                System.out.println(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public Class<?>[] extractClass(Object... objects) {
        if (objects != null) {
            Class<?>[] classes = new Class[objects.length];
            for (int i = 0; i < objects.length; i++) {
                classes[i] = objects[i].getClass();
            }
            return classes;
        } else {
            return null;
        }
    }

}
