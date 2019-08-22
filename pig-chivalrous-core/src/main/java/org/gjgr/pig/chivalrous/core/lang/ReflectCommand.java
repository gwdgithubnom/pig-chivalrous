package org.gjgr.pig.chivalrous.core.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Author gwd
 * @Time 10-29-2018 Monday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public class ReflectCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectCommand.class);

    private static final Class<?>[] EMPTY_CLASSES = new Class<?>[0];

    /**
     * Call a static public method.
     *
     * @param clazz
     * @param methodName
     * @param methodParams
     * @param args
     * @return
     * @throws Exception
     */
    public static Object callMethod(Class<?> clazz, String methodName, Class<?>[] methodParams, Object... args)
            throws Exception {
        return callMethod(clazz, null, methodName, methodParams, args);
    }

    /**
     * Call a public method.
     *
     * @param clazz
     * @param instance
     * @param methodName
     * @param methodParams
     * @param args
     * @return
     * @throws Exception
     */
    public static Object callMethod(Class<?> clazz, Object instance, String methodName, Class<?>[] methodParams,
                                    Object... args)
            throws Exception {
        Method method = clazz.getMethod(methodName, methodParams);
        return method.invoke(instance, args);
    }

    /**
     * Gets the {@link Constructor} having the given number of arguments, or <code>null</code> if cannot find a
     * appropriate one.
     *
     * @param clazz
     * @param argsCount
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> Constructor<T> getConstructorByArgsCount(Class<T> clazz, int argsCount) {
        Constructor<T>[] ctors = (Constructor<T>[]) clazz.getConstructors();
        for (Constructor<T> ctor : ctors) {
            if (ctor.getParameterTypes().length == argsCount) {
                return ctor;
            }
        }
        return null;
    }

    /**
     * Gets a {@link PropertyDescriptor} array of given class. If there is error, return an empty array.
     *
     * @param clazz
     * @return
     */
    public PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            LOGGER.warn("getPropertyDescriptors of {} failed. Error: ", clazz, e);
            return new PropertyDescriptor[0];
        }
        PropertyDescriptor[] result = beanInfo.getPropertyDescriptors();
        return result == null ? new PropertyDescriptor[0] : result;
    }

    /**
     * Create a new instance by class name.
     *
     * @param className
     * @return
     */
    public Object newInstance(String className) {
        try {
            return Class.forName(className).newInstance();
        } catch (Exception e) {
            throw new NoClassDefFoundError(className);
        }
    }

    /**
     * Create a new instance by class object
     */
    public <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new UnsupportedOperationException("Create instance failed for class: " + clazz, e);
        }
    }

    /**
     * Get the actual classes of a generic type.
     *
     * @param genericType
     * @return
     */
    public Class<?>[] getActualClasses(Type genericType) {
        if (genericType instanceof ParameterizedType) {
            Type[] actualTypes = ((ParameterizedType) genericType).getActualTypeArguments();
            Class<?>[] actualClasses = new Class<?>[actualTypes.length];

            for (int i = 0; i < actualTypes.length; i++) {
                Type actualType = actualTypes[i];
                if (actualType instanceof Class<?>) {
                    actualClasses[i] = (Class<?>) actualType;
                } else if (actualType instanceof GenericArrayType) {
                    Type componentType = ((GenericArrayType) actualType).getGenericComponentType();
                    actualClasses[i] = Array.newInstance((Class<?>) componentType, 0).getClass();
                }
            }

            return actualClasses;
        }
        return EMPTY_CLASSES;
    }

}
