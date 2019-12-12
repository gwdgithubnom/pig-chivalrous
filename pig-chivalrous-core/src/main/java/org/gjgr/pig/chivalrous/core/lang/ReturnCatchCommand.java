package org.gjgr.pig.chivalrous.core.lang;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import org.gjgr.pig.chivalrous.log.SystemLogger;

/**
 * @Author gwd
 * @Time 12-04-2019  Wednesday
 * @Description: gjgrparent:
 * @Target:
 * @More:
 */
public class ReturnCatchCommand {
    public static <T> T returnCatchWithEmpty(Integer traceIndex, ObjectWrapper<T> objectWrapper, T t, Object... object) {
        long l = System.nanoTime();
        StackTraceElement s = Thread.currentThread().getStackTrace()[traceIndex];
        SystemLogger.info("in function {} about {}, params info {} ", s, l,
                Arrays.toString(object));
        t = objectWrapper.wrapperWithEmpty(t);
        SystemLogger.info("in function {} about {}, result info {}", s, l, t);
        return t;
    }

    public static <T> T returnCatchWithEmpty(ObjectWrapper<T> objectWrapper, T t, Object... objects) {
        return returnCatchWithEmpty(2, objectWrapper, t, objects);

    }

    public static <T> T returnCatchWithNull(ObjectWrapper<T> objectWrapper, T t, Object... objects) {
        return returnCatchWithNull(2, objectWrapper, t, objects);
    }

    public static <T> T returnCatchWithNull(Integer traceIndex, ObjectWrapper<T> objectWrapper, T t, Object... object) {
        long l = System.nanoTime();
        StackTraceElement s = Thread.currentThread().getStackTrace()[traceIndex];
        SystemLogger.info("in function {} about {}, params info {} ", s, l,
                Arrays.toString(object));
        t = objectWrapper.wrapperWithNull(t);
        SystemLogger.info("in function {} about {}, result info {}", s, l, t);
        return t;
    }

    public static <T> T returnCatchNotNull(ObjectWrapper<T> objectWrapper, T t, Object... objects) {
        return returnCatchNotNull(2, objectWrapper, t, objects);
    }

    public static <T> T returnCatchNotNull(Integer traceIndex, ObjectWrapper<T> objectWrapper, T t, Object... object) {
        long l = System.nanoTime();
        StackTraceElement s = Thread.currentThread().getStackTrace()[traceIndex];
        SystemLogger.info("in function {} about {}, params info {} ", s, l,
                Arrays.toString(object));
        t = objectWrapper.wrapperNotNull(t);
        SystemLogger.info("in function {} about {}, result info {}", s, l, t);
        return t;
    }

    public static <T> T autoSwithReturnWithDefualt(Object object, ObjectWrapper<T> objectWrapper, Object... objects) {
        return autoSwithReturnWithDefualt(2, object, objectWrapper, objects);
    }

    public static <T> T autoSwithReturnWithDefualt(Integer traceIndex, Object object, ObjectWrapper<T> objectWrapper, Object... objects) {
        StackTraceElement s = Thread.currentThread().getStackTrace()[2];
        try {
            Method method = object.getClass().getMethod(s.getMethodName(), ClassCommand.getClasses(objects));
            try {
                T t = (T) method.invoke(object, objects);
                if (t != null) {
                    if (Map.class.isAssignableFrom(t.getClass())) {
                        return returnCatchWithEmpty(traceIndex, objectWrapper, t, objects);
                    } else if (Collection.class.isAssignableFrom(t.getClass())) {
                        return returnCatchWithEmpty(traceIndex, objectWrapper, t, objects);
                    } else if (Number.class.isAssignableFrom(t.getClass())) {
                        return returnCatchNotNull(traceIndex, objectWrapper, t, objects);
                    } else {
                        return returnCatchWithNull(traceIndex, objectWrapper, t, objects);
                    }
//                    if (t.getClass().isAssignableFrom(ParameterizedType.class)) {
//                        return returnCatchWithEmpty(traceIndex, objectWrapper, t, objects);
//                    } else if (t.getClass().isAssignableFrom(GenericArrayType.class)) {
//                        return returnCatchWithEmpty(traceIndex, objectWrapper, t, objects);
//                    } else if (t.getClass().isAssignableFrom(PrimitiveType.class)) {
//                        return returnCatchNotNull(traceIndex, objectWrapper, t, objects);
//                    } else if (t.getClass().isAssignableFrom(String.class)) {
//                        return returnCatchNotNull(traceIndex, objectWrapper, t, objects);
//                    } else {
//                        return returnCatchWithNull(traceIndex, objectWrapper, t, objects);
//                    }
//                    throw new RuntimeException("not support the target class switch {}" + t.getClass());
                } else {
                    return null;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException("not support the target class switch {}" + Arrays.toString(e.getStackTrace()) + " " + e.getMessage());
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException("not support the target class switch {}" + Arrays.toString(e.getStackTrace()) + " " + e.getMessage());
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException("not support the target class switch {}" + Arrays.toString(e.getStackTrace()) + " " + e.getMessage());
        }
    }
}
