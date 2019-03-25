package org.gjgr.pig.chivalrous.core.lang;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.gjgr.pig.chivalrous.core.exceptions.ThrowingFunction;
import org.gjgr.pig.chivalrous.core.exceptions.UtilException;
import org.gjgr.pig.chivalrous.core.io.IoCommand;
import org.gjgr.pig.chivalrous.core.io.file.FileCommand;
import org.gjgr.pig.chivalrous.core.io.stream.FastByteArrayOutputStream;
import org.gjgr.pig.chivalrous.core.io.stream.StreamCommand;
import org.gjgr.pig.chivalrous.core.util.TypeConverter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @Author gwd
 * @Time 11-27-2018 Tuesday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public final class ObjectCommand {
    private static final int INITIAL_HASH = 7;
    private static final int MULTIPLIER = 31;

    private static final String EMPTY_STRING = "";
    private static final String NULL_STRING = "null";
    private static final String ARRAY_START = "{";
    private static final String ARRAY_END = "}";
    private static final String EMPTY_ARRAY = ARRAY_START + ARRAY_END;
    private static final String ARRAY_ELEMENT_SEPARATOR = ", ";

    private static final String DEFAULT_DELIMITER = ",";

    /**
     * Utility classes should not have a public constructor.
     */
    private ObjectCommand() {
    }

    /**
     * A helper method for comparing objects for equality in which it uses type coercion to coerce types between the
     * left and right values. This allows you test for equality for example with a String and Integer type as Camel will
     * be able to coerce the types.
     */
    public static boolean typeCoerceEquals(TypeConverter converter, Object leftValue, Object rightValue) {
        return typeCoerceEquals(converter, leftValue, rightValue, false);
    }

    /**
     * A helper method for comparing objects for equality in which it uses type coercion to coerce types between the
     * left and right values. This allows you test for equality for example with a String and Integer type as Camel will
     * be able to coerce the types.
     */
    public static boolean typeCoerceEquals(TypeConverter converter, Object leftValue, Object rightValue,
            boolean ignoreCase) {
        // sanity check
        if (leftValue == null && rightValue == null) {
            // they are equal
            return true;
        } else if (leftValue == null || rightValue == null) {
            // only one of them is null so they are not equal
            return false;
        }

        // try without type coerce
        boolean answer = equal(leftValue, rightValue, ignoreCase);
        if (answer) {
            return true;
        }

        // are they same type, if so return false as the equals returned false
        if (leftValue.getClass().isInstance(rightValue)) {
            return false;
        }

        // convert left to right
        Object value = converter.tryConvertTo(rightValue.getClass(), leftValue);
        answer = equal(value, rightValue, ignoreCase);
        if (answer) {
            return true;
        }

        // convert right to left
        value = converter.tryConvertTo(leftValue.getClass(), rightValue);
        answer = equal(leftValue, value, ignoreCase);
        return answer;
    }

    /**
     * A helper method for comparing objects for inequality in which it uses type coercion to coerce types between the
     * left and right values. This allows you test for inequality for example with a String and Integer type as Camel
     * will be able to coerce the types.
     */
    public static boolean typeCoerceNotEquals(TypeConverter converter, Object leftValue, Object rightValue) {
        return !typeCoerceEquals(converter, leftValue, rightValue);
    }

    /**
     * A helper method for comparing objects ordering in which it uses type coercion to coerce types between the left
     * and right values. This allows you test for ordering for example with a String and Integer type as Camel will be
     * able to coerce the types.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static int typeCoerceCompare(TypeConverter converter, Object leftValue, Object rightValue) {

        // if both values is numeric then compare using numeric
        Long leftNum = converter.tryConvertTo(Long.class, leftValue);
        Long rightNum = converter.tryConvertTo(Long.class, rightValue);
        if (leftNum != null && rightNum != null) {
            return leftNum.compareTo(rightNum);
        }

        // also try with floating point numbers
        Double leftDouble = converter.tryConvertTo(Double.class, leftValue);
        Double rightDouble = converter.tryConvertTo(Double.class, rightValue);
        if (leftDouble != null && rightDouble != null) {
            return leftDouble.compareTo(rightDouble);
        }

        // prefer to NOT coerce to String so use the type which is not String
        // for example if we are comparing String vs Integer then prefer to coerce to Integer
        // as all types can be converted to String which does not work well for comparison
        // as eg "10" < 6 would return true, where as 10 < 6 will return false.
        // if they are both String then it doesn't matter
        if (rightValue instanceof String && (!(leftValue instanceof String))) {
            // if right is String and left is not then flip order (remember to * -1 the result then)
            return typeCoerceCompare(converter, rightValue, leftValue) * -1;
        }

        // prefer to coerce to the right hand side at first
        if (rightValue instanceof Comparable) {
            Object value = converter.tryConvertTo(rightValue.getClass(), leftValue);
            if (value != null) {
                return ((Comparable) rightValue).compareTo(value) * -1;
            }
        }

        // then fallback to the left hand side
        if (leftValue instanceof Comparable) {
            Object value = converter.tryConvertTo(leftValue.getClass(), rightValue);
            if (value != null) {
                return ((Comparable) leftValue).compareTo(value);
            }
        }

        // use regular compare
        return compare(leftValue, rightValue);
    }

    /**
     * A helper method for comparing objects for equality while handling nulls 比较两个对象是否相等。<br>
     * 相同的条件有两个，满足其一即可：<br>
     * <ol>
     * <li>obj1 == null && obj2 == null</li>
     * <li>obj1.equals(obj2)</li>
     * </ol>
     * 1. obj1 == null && obj2 == null 2. obj1.equals(obj2)
     *
     * @param a 对象1
     * @param b 对象2
     * @return 是否相等
     */
    public static boolean equal(Object a, Object b) {
        return equal(a, b, false);
    }

    /**
     * A helper method for comparing objects for equality while handling case insensitivity
     */
    public static boolean equalIgnoreCase(Object a, Object b) {
        return equal(a, b, true);
    }

    /**
     * A helper method for comparing objects for equality while handling nulls
     */
    public static boolean equal(final Object a, final Object b, final boolean ignoreCase) {
        if (a == b) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        if (ignoreCase) {
            if (a instanceof String && b instanceof String) {
                return ((String) a).equalsIgnoreCase((String) b);
            }
        }

        if (a.getClass().isArray() && b.getClass().isArray()) {
            // uses array based equals
            return Objects.deepEquals(a, b);
        } else {
            // use regular equals
            return a.equals(b);
        }
    }

    /**
     * A helper method for comparing byte arrays for equality while handling nulls
     */
    public static boolean equalByteArray(byte[] a, byte[] b) {
        return Arrays.equals(a, b);
    }

    /**
     * Returns true if the given object is equal to any of the expected value
     */
    public static boolean isEqualToAny(Object object, Object...values) {
        for (Object value : values) {
            if (equal(object, value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * A helper method for performing an ordered comparison on the objects handling nulls and objects which do not
     * handle sorting gracefully
     */
    public static int compare(Object a, Object b) {
        return compare(a, b, false);
    }

    /**
     * A helper method for performing an ordered comparison on the objects handling nulls and objects which do not
     * handle sorting gracefully
     *
     * @param a the first object
     * @param b the second object
     * @param ignoreCase ignore case for string comparison
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static int compare(Object a, Object b, boolean ignoreCase) {
        if (a == b) {
            return 0;
        }
        if (a == null) {
            return -1;
        }
        if (b == null) {
            return 1;
        }
        if (a instanceof Ordered && b instanceof Ordered) {
            return ((Ordered) a).getOrder() - ((Ordered) b).getOrder();
        }
        if (ignoreCase && a instanceof String && b instanceof String) {
            return ((String) a).compareToIgnoreCase((String) b);
        }
        if (a instanceof Comparable) {
            Comparable comparable = (Comparable) a;
            return comparable.compareTo(b);
        }
        int answer = a.getClass().getName().compareTo(b.getClass().getName());
        if (answer == 0) {
            answer = a.hashCode() - b.hashCode();
        }
        return answer;
    }

    public static Boolean toBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return Boolean.valueOf((String) value);
        }
        if (value instanceof Integer) {
            return (Integer) value > 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        return null;
    }

    /**
     * Asserts whether the value is <b>not</b> <tt>null</tt>
     *
     * @param value the value to test
     * @param name the key that resolved the value
     * @return the passed {@code value} as is
     * @throws IllegalArgumentException is thrown if assertion fails
     */
    public static <T> T notNull(T value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(name + " must be specified");
        }

        return value;
    }

    /**
     * Asserts whether the value is <b>not</b> <tt>null</tt>
     *
     * @param value the value to test
     * @param on additional description to indicate where this problem occurred (appended as toString())
     * @param name the key that resolved the value
     * @return the passed {@code value} as is
     * @throws IllegalArgumentException is thrown if assertion fails
     */
    public static <T> T notNull(T value, String name, Object on) {
        if (on == null) {
            notNull(value, name);
        } else if (value == null) {
            throw new IllegalArgumentException(name + " must be specified on: " + on);
        }

        return value;
    }

    /**
     * Asserts whether the string is <b>not</b> empty.
     *
     * @param value the string to test
     * @param name the key that resolved the value
     * @return the passed {@code value} as is
     * @throws IllegalArgumentException is thrown if assertion fails
     * @deprecated use {@links StringCommand#notEmpty(String, String)} instead
     */
    @Deprecated
    public static String notEmpty(String value, String name) {
        return StringCommand.notEmpty(value, name);
    }

    /**
     * Asserts whether the string is <b>not</b> empty.
     *
     * @param value the string to test
     * @param on additional description to indicate where this problem occurred (appended as toString())
     * @param name the key that resolved the value
     * @return the passed {@code value} as is
     * @throws IllegalArgumentException is thrown if assertion fails
     * @deprecated use {@links StringCommand#notEmpty(String, String, Object)} instead
     */
    @Deprecated
    public static String notEmpty(String value, String name, Object on) {
        return StringCommand.notEmpty(value, name, on);
    }

    /**
     * Tests whether the value is <b>not</b> <tt>null</tt>, an empty string or an empty collection/map.
     *
     * @param value the value, if its a String it will be tested for text length as well
     * @return true if <b>not</b> empty
     */
    public static boolean isNotEmpty(Object value) {
        if (value == null) {
            return false;
        } else if (value instanceof String) {
            String text = (String) value;
            return text.trim().length() > 0;
        } else if (value instanceof Collection) {
            return !((Collection<?>) value).isEmpty();
        } else if (value instanceof Map) {
            return !((Map<?, ?>) value).isEmpty();
        } else {
            return true;
        }
    }

    /**
     * Returns the first non null object <tt>null</tt>.
     *
     * @param values the values
     * @return an Optional
     */
    public static Optional<Object> firstNotNull(Object...values) {
        for (Object value : values) {
            if (value != null) {
                return Optional.of(value);
            }
        }

        return Optional.empty();
    }

    /**
     * Tests whether the value is <tt>null</tt>, an empty string, an empty collection or a map
     *
     * @param value the value, if its a String it will be tested for text length as well
     * @param supplier the supplier, the supplier to be used to get a value if value is null
     */
    public static <T> T supplyIfEmpty(T value, Supplier<T> supplier) {
        ObjectCommand.notNull(supplier, "Supplier");
        if (isNotEmpty(value)) {
            return value;
        }

        return supplier.get();
    }

    /**
     * Tests whether the value is <b>not</b> <tt>null</tt>, an empty string, an empty collection or a map
     *
     * @param value the value, if its a String it will be tested for text length as well
     * @param consumer the consumer, the operation to be executed against value if not empty
     */
    public static <T> void ifNotEmpty(T value, Consumer<T> consumer) {
        if (isNotEmpty(value)) {
            consumer.accept(value);
        }
    }

    /**
     * Tests whether the value is <b>not</b> <tt>null</tt>, an empty string, an empty collection or a map and transform
     * it using the given function.
     *
     * @param value the value, if its a String it will be tested for text length as well
     * @param function the function to be executed against value if not empty
     */
    public static <I, R, T extends Throwable> Optional<R> applyIfNotEmpty(I value, ThrowingFunction<I, R, T> function)
            throws T {
        if (isNotEmpty(value)) {
            return Optional.ofNullable(function.apply(value));
        }

        return Optional.empty();
    }

    /**
     * Tests whether the value is <b>not</b> <tt>null</tt>, an empty string, an empty collection or a map and transform
     * it using the given function.
     *
     * @param value the value, if its a String it will be tested for text length as well
     * @param consumer the function to be executed against value if not empty
     * @param orElse the supplier to use to retrieve a result if the given value is empty
     */
    public static <I, R, T extends Throwable> R applyIfNotEmpty(I value, ThrowingFunction<I, R, T> consumer,
            Supplier<R> orElse) throws T {
        if (isNotEmpty(value)) {
            return consumer.apply(value);
        }

        return orElse.get();
    }

    /**
     * @deprecated use {@links StringCommand#splitOnCharacter(String, String, int)} instead
     */
    @Deprecated
    public static String[] splitOnCharacter(String value, String needle, int count) {
        return StringCommand.splitOnCharacter(value, needle, count);
    }

    /**
     * Removes any starting characters on the given text which match the given character
     *
     * @param text the string
     * @param ch the initial characters to remove
     * @return either the original string or the new substring
     * @deprecated use {@links StringCommand#removeStartingCharacters(String, char)} instead
     */
    @Deprecated
    public static String removeStartingCharacters(String text, char ch) {
        return StringCommand.removeStartingCharacters(text, ch);
    }

    /**
     * @deprecated use {@links StringCommand#capitalize(String)} instead
     */
    @Deprecated
    public static String capitalize(String text) {
        return StringCommand.capitalize(text);
    }

    /**
     * Returns the string after the given token
     *
     * @param text the text
     * @param after the token
     * @return the text after the token, or <tt>null</tt> if text does not contain the token
     * @deprecated use {@linksStringCommand#after(String, String)} instead
     */
    @Deprecated
    public static String after(String text, String after) {
        return StringCommand.after(text, after);
    }

    /**
     * Returns an object after the given token
     *
     * @param text the text
     * @param after the token
     * @param mapper a mapping function to convert the string after the token to type T
     * @return an Optional describing the result of applying a mapping function to the text after the token.
     * @deprecated use {@linksStringCommand#after(String, String, Function) StringCommand.after(String, String,
     *             Function&lt;String,T&gt;)} instead
     */
    @Deprecated
    public static <T> Optional<T> after(String text, String after, Function<String, T> mapper) {
        return StringCommand.after(text, after, mapper);
    }

    /**
     * Returns the string before the given token
     *
     * @param text the text
     * @param before the token
     * @return the text before the token, or <tt>null</tt> if text does not contain the token
     * @deprecated use {@linksStringCommand#before(String, String)} instead
     */
    @Deprecated
    public static String before(String text, String before) {
        return StringCommand.before(text, before);
    }

    /**
     * Returns an object before the given token
     *
     * @param text the text
     * @param before the token
     * @param mapper a mapping function to convert the string before the token to type T
     * @return an Optional describing the result of applying a mapping function to the text before the token.
     * @deprecated use {@linksStringCommand#before(String, String, Function) StringCommand.before(String, String,
     *             Function&lt;String,T&gt;)} instead
     */
    @Deprecated
    public static <T> Optional<T> before(String text, String before, Function<String, T> mapper) {
        return StringCommand.before(text, before, mapper);
    }

    /**
     * Returns the string between the given tokens
     *
     * @param text the text
     * @param after the before token
     * @param before the after token
     * @return the text between the tokens, or <tt>null</tt> if text does not contain the tokens
     * @deprecated use {@linksStringCommand#between(String, String, String)} instead
     */
    @Deprecated
    public static String between(String text, String after, String before) {
        return StringCommand.between(text, after, before);
    }

    /**
     * Returns an object between the given token
     *
     * @param text the text
     * @param after the before token
     * @param before the after token
     * @param mapper a mapping function to convert the string between the token to type T
     * @return an Optional describing the result of applying a mapping function to the text between the token.
     * @deprecated use {@linksStringCommand#between(String, String, String, Function) StringCommand.between(String,
     *             String, String, Function&lt;String,T&gt;)} instead
     */
    @Deprecated
    public static <T> Optional<T> between(String text, String after, String before, Function<String, T> mapper) {
        return StringCommand.between(text, after, before, mapper);
    }

    /**
     * Returns the string between the most outer pair of tokens
     * <p/>
     * The number of token pairs must be evenly, eg there must be same number of before and after tokens, otherwise
     * <tt>null</tt> is returned
     * <p/>
     * This implementation skips matching when the text is either single or double quoted. For example:
     * <tt>${body.matches("foo('bar')")</tt> Will not match the parenthesis from the quoted text.
     *
     * @param text the text
     * @param after the before token
     * @param before the after token
     * @return the text between the outer most tokens, or <tt>null</tt> if text does not contain the tokens
     * @deprecated use {@linksStringCommand#betweenOuterPair(String, char, char)} instead
     */
    @Deprecated
    public static String betweenOuterPair(String text, char before, char after) {
        return StringCommand.betweenOuterPair(text, before, after);
    }

    /**
     * Returns an object between the most outer pair of tokens
     *
     * @param text the text
     * @param after the before token
     * @param before the after token
     * @param mapper a mapping function to convert the string between the most outer pair of tokens to type T
     * @return an Optional describing the result of applying a mapping function to the text between the most outer pair
     *         of tokens.
     * @deprecated use {@linksStringCommand#betweenOuterPair(String, char, char, Function)
     *             StringCommand.betweenOuterPair(String, char, char, Function&lt;String,T&gt;)} instead
     */
    @Deprecated
    public static <T> Optional<T> betweenOuterPair(String text, char before, char after, Function<String, T> mapper) {
        return StringCommand.betweenOuterPair(text, before, after, mapper);
    }

    /**
     * Returns the predicate matching boolean on a {@linksList} result set where if the first element is a boolean its
     * value is used otherwise this method returns true if the collection is not empty
     *
     * @return <tt>true</tt> if the first element is a boolean and its value is true or if the list is non empty
     */
    public static boolean matches(List<?> list) {
        if (!list.isEmpty()) {
            Object value = list.get(0);
            if (value instanceof Boolean) {
                return (Boolean) value;
            } else {
                // lets assume non-empty results are true
                return true;
            }
        }
        return false;
    }

    /**
     * A helper method to access a system property, catching any security exceptions
     *
     * @param name the name of the system property required
     * @param defaultValue the default value to use if the property is not available or a security exception prevents
     *            access
     * @return the system property value or the default value if the property is not available or security does not
     *         allow its access
     */
    public static String getSystemProperty(String name, String defaultValue) {
        try {
            return System.getProperty(name, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * A helper method to access a boolean system property, catching any security exceptions
     *
     * @param name the name of the system property required
     * @param defaultValue the default value to use if the property is not available or a security exception prevents
     *            access
     * @return the boolean representation of the system property value or the default value if the property is not
     *         available or security does not allow its access
     */
    public static boolean getSystemProperty(String name, Boolean defaultValue) {
        String result = getSystemProperty(name, defaultValue.toString());
        return Boolean.parseBoolean(result);
    }

    /**
     * Returns the type name of the given type or null if the type variable is null
     */
    public static String name(Class<?> type) {
        return type != null ? type.getName() : null;
    }

    /**
     * Returns the type name of the given value
     */
    public static String className(Object value) {
        return name(value != null ? value.getClass() : null);
    }

    /**
     * Returns the canonical type name of the given value
     */
    public static String classCanonicalName(Object value) {
        if (value != null) {
            return value.getClass().getCanonicalName();
        } else {
            return null;
        }
    }

    /**
     * Attempts to load the given class name using the thread context class loader or the class loader used to load this
     * class
     *
     * @param name the name of the class to load
     * @return the class or <tt>null</tt> if it could not be loaded
     */
    public static Class<?> loadClass(String name) {
        return loadClass(name, ObjectCommand.class.getClassLoader());
    }

    /**
     * Attempts to load the given class name using the thread context class loader or the given class loader
     *
     * @param name the name of the class to load
     * @param loader the class loader to use after the thread context class loader
     * @return the class or <tt>null</tt> if it could not be loaded
     */
    public static Class<?> loadClass(String name, ClassLoader loader) {
        return loadClass(name, loader, false);
    }

    /**
     * Attempts to load the given class name using the thread context class loader or the given class loader
     *
     * @param name the name of the class to load
     * @param loader the class loader to use after the thread context class loader
     * @param needToWarn when <tt>true</tt> logs a warning when a class with the given name could not be loaded
     * @return the class or <tt>null</tt> if it could not be loaded
     */
    public static Class<?> loadClass(String name, ClassLoader loader, boolean needToWarn) {
        // must clean the name so its pure java name, eg removing \n or whatever people can do in the Spring XML
        name = normalizeClassName(name);
        if (ObjectCommand.isEmpty(name)) {
            return null;
        }

        // Try simple type first
        Class<?> clazz = loadSimpleType(name);
        if (clazz == null) {
            // try context class loader
            clazz = doLoadClass(name, Thread.currentThread().getContextClassLoader());
        }
        if (clazz == null) {
            // then the provided loader
            clazz = doLoadClass(name, loader);
        }
        if (clazz == null) {
            // and fallback to the loader the loaded the ObjectHelper class
            clazz = doLoadClass(name, ObjectCommand.class.getClassLoader());
        }

        return clazz;
    }

    /**
     * Load a simple type
     *
     * @param name the name of the class to load
     * @return the class or <tt>null</tt> if it could not be loaded
     */
    // CHECKSTYLE:OFF
    public static Class<?> loadSimpleType(String name) {
        // special for byte[] or Object[] as its common to use
        if ("java.lang.byte[]".equals(name) || "byte[]".equals(name)) {
            return byte[].class;
        } else if ("java.lang.Byte[]".equals(name) || "Byte[]".equals(name)) {
            return Byte[].class;
        } else if ("java.lang.Object[]".equals(name) || "Object[]".equals(name)) {
            return Object[].class;
        } else if ("java.lang.String[]".equals(name) || "String[]".equals(name)) {
            return String[].class;
            // and these is common as well
        } else if ("java.lang.String".equals(name) || "String".equals(name)) {
            return String.class;
        } else if ("java.lang.Boolean".equals(name) || "Boolean".equals(name)) {
            return Boolean.class;
        } else if ("boolean".equals(name)) {
            return boolean.class;
        } else if ("java.lang.Integer".equals(name) || "Integer".equals(name)) {
            return Integer.class;
        } else if ("int".equals(name)) {
            return int.class;
        } else if ("java.lang.Long".equals(name) || "Long".equals(name)) {
            return Long.class;
        } else if ("long".equals(name)) {
            return long.class;
        } else if ("java.lang.Short".equals(name) || "Short".equals(name)) {
            return Short.class;
        } else if ("short".equals(name)) {
            return short.class;
        } else if ("java.lang.Byte".equals(name) || "Byte".equals(name)) {
            return Byte.class;
        } else if ("byte".equals(name)) {
            return byte.class;
        } else if ("java.lang.Float".equals(name) || "Float".equals(name)) {
            return Float.class;
        } else if ("float".equals(name)) {
            return float.class;
        } else if ("java.lang.Double".equals(name) || "Double".equals(name)) {
            return Double.class;
        } else if ("double".equals(name)) {
            return double.class;
        } else if ("java.lang.Character".equals(name) || "Character".equals(name)) {
            return Character.class;
        } else if ("char".equals(name)) {
            return char.class;
        }
        return null;
    }
    // CHECKSTYLE:ON

    /**
     * Loads the given class with the provided classloader (may be null). Will ignore any class not found and return
     * null.
     *
     * @param name the name of the class to load
     * @param loader a provided loader (may be null)
     * @return the class, or null if it could not be loaded
     */
    private static Class<?> doLoadClass(String name, ClassLoader loader) {
        StringCommand.notEmpty(name, "name");
        if (loader == null) {
            return null;
        }

        try {
            return loader.loadClass(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Attempts to load the given resource as a stream using the thread context class loader or the class loader used to
     * load this class
     *
     * @param name the name of the resource to load
     * @return the stream or null if it could not be loaded
     */
    public static InputStream loadResourceAsStream(String name) {
        return loadResourceAsStream(name, null);
    }

    /**
     * Attempts to load the given resource as a stream using the thread context class loader or the class loader used to
     * load this class
     *
     * @param name the name of the resource to load
     * @param loader optional classloader to attempt first
     * @return the stream or null if it could not be loaded
     */
    public static InputStream loadResourceAsStream(String name, ClassLoader loader) {
        InputStream in = null;

        String resolvedName = resolveUriPath(name);
        if (loader != null) {
            in = loader.getResourceAsStream(resolvedName);
        }
        if (in == null) {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {
                in = contextClassLoader.getResourceAsStream(resolvedName);
            }
        }
        if (in == null) {
            in = ObjectCommand.class.getClassLoader().getResourceAsStream(resolvedName);
        }
        if (in == null) {
            in = ObjectCommand.class.getResourceAsStream(resolvedName);
        }

        return in;
    }

    /**
     * Attempts to load the given resource as a stream using the thread context class loader or the class loader used to
     * load this class
     *
     * @param name the name of the resource to load
     * @return the stream or null if it could not be loaded
     */
    public static URL loadResourceAsURL(String name) {
        return loadResourceAsURL(name, null);
    }

    /**
     * Attempts to load the given resource as a stream using the thread context class loader or the class loader used to
     * load this class
     *
     * @param name the name of the resource to load
     * @param loader optional classloader to attempt first
     * @return the stream or null if it could not be loaded
     */
    public static URL loadResourceAsURL(String name, ClassLoader loader) {
        URL url = null;

        String resolvedName = resolveUriPath(name);
        if (loader != null) {
            url = loader.getResource(resolvedName);
        }
        if (url == null) {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {
                url = contextClassLoader.getResource(resolvedName);
            }
        }
        if (url == null) {
            url = ObjectCommand.class.getClassLoader().getResource(resolvedName);
        }
        if (url == null) {
            url = ObjectCommand.class.getResource(resolvedName);
        }

        return url;
    }

    /**
     * Attempts to load the given resources from the given package name using the thread context class loader or the
     * class loader used to load this class
     *
     * @param packageName the name of the package to load its resources
     * @return the URLs for the resources or null if it could not be loaded
     */
    public static Enumeration<URL> loadResourcesAsURL(String packageName) {
        return loadResourcesAsURL(packageName, null);
    }

    /**
     * Attempts to load the given resources from the given package name using the thread context class loader or the
     * class loader used to load this class
     *
     * @param packageName the name of the package to load its resources
     * @param loader optional classloader to attempt first
     * @return the URLs for the resources or null if it could not be loaded
     */
    public static Enumeration<URL> loadResourcesAsURL(String packageName, ClassLoader loader) {
        Enumeration<URL> url = null;

        if (loader != null) {
            try {
                url = loader.getResources(packageName);
            } catch (IOException e) {
                // ignore
            }
        }

        if (url == null) {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {
                try {
                    url = contextClassLoader.getResources(packageName);
                } catch (IOException e) {
                    // ignore
                }
            }
        }
        if (url == null) {
            try {
                url = ObjectCommand.class.getClassLoader().getResources(packageName);
            } catch (IOException e) {
                // ignore
            }
        }

        return url;
    }

    /**
     * Helper operation used to remove relative path notation from resources. Most critical for resources on the
     * Classpath as resource loaders will not resolve the relative paths correctly.
     *
     * @param name the name of the resource to load
     * @return the modified or unmodified string if there were no changes
     */
    private static String resolveUriPath(String name) {
        // compact the path and use / as separator as that's used for loading resources on the classpath
        return FileCommand.compactPath(name, '/');
    }

    /**
     * A helper method to invoke a method via reflection and wrap any exceptions as
     * {@linksRuntimeCamelException} instances
     *
     * @param method the method to invoke
     * @param instance the object instance (or null for static methods)
     * @param parameters the parameters to the method
     * @return the result of the method invocation
     */
    public static Object invokeMethod(Method method, Object instance, Object...parameters) {
        try {
            return method.invoke(instance, parameters);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests whether the target method overrides the source method.
     * <p/>
     * Tests whether they have the same name, return type, and parameter list.
     *
     * @param source the source method
     * @param target the target method
     * @return <tt>true</tt> if it override, <tt>false</tt> otherwise
     */
    public static boolean isOverridingMethod(Method source, Method target) {
        return isOverridingMethod(source, target, true);
    }

    /**
     * Tests whether the target method overrides the source method.
     * <p/>
     * Tests whether they have the same name, return type, and parameter list.
     *
     * @param source the source method
     * @param target the target method
     * @param exact <tt>true</tt> if the override must be exact same types, <tt>false</tt> if the types should be
     *            assignable
     * @return <tt>true</tt> if it override, <tt>false</tt> otherwise
     */
    public static boolean isOverridingMethod(Method source, Method target, boolean exact) {
        return isOverridingMethod(target.getDeclaringClass(), source, target, exact);
    }

    /**
     * Tests whether the target method overrides the source method from the inheriting class.
     * <p/>
     * Tests whether they have the same name, return type, and parameter list.
     *
     * @param inheritingClass the class inheriting the target method overriding the source method
     * @param source the source method
     * @param target the target method
     * @param exact <tt>true</tt> if the override must be exact same types, <tt>false</tt> if the types should be
     *            assignable
     * @return <tt>true</tt> if it override, <tt>false</tt> otherwise
     */
    public static boolean isOverridingMethod(Class<?> inheritingClass, Method source, Method target, boolean exact) {

        if (source.equals(target)) {
            return true;
        } else if (target.getDeclaringClass().isAssignableFrom(source.getDeclaringClass())) {
            return false;
        } else if (!source.getDeclaringClass().isAssignableFrom(inheritingClass)
                || !target.getDeclaringClass().isAssignableFrom(inheritingClass)) {
            return false;
        }

        if (!source.getName().equals(target.getName())) {
            return false;
        }

        if (exact) {
            if (!source.getReturnType().equals(target.getReturnType())) {
                return false;
            }
        } else {
            if (!source.getReturnType().isAssignableFrom(target.getReturnType())) {
                boolean b1 = source.isBridge();
                boolean b2 = target.isBridge();
                // must not be bridge methods
                if (!b1 && !b2) {
                    return false;
                }
            }
        }

        // must have same number of parameter types
        if (source.getParameterCount() != target.getParameterCount()) {
            return false;
        }

        Class<?>[] sourceTypes = source.getParameterTypes();
        Class<?>[] targetTypes = target.getParameterTypes();
        // test if parameter types is the same as well
        for (int i = 0; i < source.getParameterCount(); i++) {
            if (exact) {
                if (!(sourceTypes[i].equals(targetTypes[i]))) {
                    return false;
                }
            } else {
                if (!(sourceTypes[i].isAssignableFrom(targetTypes[i]))) {
                    boolean b1 = source.isBridge();
                    boolean b2 = target.isBridge();
                    // must not be bridge methods
                    if (!b1 && !b2) {
                        return false;
                    }
                }
            }
        }

        // the have same name, return type and parameter list, so its overriding
        return true;
    }

    /**
     * Returns a list of methods which are annotated with the given annotation
     *
     * @param type the type to reflect on
     * @param annotationType the annotation type
     * @return a list of the methods found
     */
    public static List<Method> findMethodsWithAnnotation(Class<?> type,
            Class<? extends Annotation> annotationType) {
        return findMethodsWithAnnotation(type, annotationType, false);
    }

    /**
     * Returns a list of methods which are annotated with the given annotation
     *
     * @param type the type to reflect on
     * @param annotationType the annotation type
     * @param checkMetaAnnotations check for meta annotations
     * @return a list of the methods found
     */
    public static List<Method> findMethodsWithAnnotation(Class<?> type,
            Class<? extends Annotation> annotationType,
            boolean checkMetaAnnotations) {
        List<Method> answer = new ArrayList<>();
        do {
            Method[] methods = type.getDeclaredMethods();
            for (Method method : methods) {
                if (hasAnnotation(method, annotationType, checkMetaAnnotations)) {
                    answer.add(method);
                }
            }
            type = type.getSuperclass();
        } while (type != null);
        return answer;
    }

    /**
     * Checks if a Class or Method are annotated with the given annotation
     *
     * @param elem the Class or Method to reflect on
     * @param annotationType the annotation type
     * @param checkMetaAnnotations check for meta annotations
     * @return true if annotations is present
     */
    public static boolean hasAnnotation(AnnotatedElement elem, Class<? extends Annotation> annotationType,
            boolean checkMetaAnnotations) {
        if (elem.isAnnotationPresent(annotationType)) {
            return true;
        }
        if (checkMetaAnnotations) {
            for (Annotation a : elem.getAnnotations()) {
                for (Annotation meta : a.annotationType().getAnnotations()) {
                    if (meta.annotationType().getName().equals(annotationType.getName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Turns the given object arrays into a meaningful string
     *
     * @param objects an array of objects or null
     * @return a meaningful string
     */
    public static String asString(Object[] objects) {
        if (objects == null) {
            return "null";
        } else {
            StringBuilder buffer = new StringBuilder("{");
            int counter = 0;
            for (Object object : objects) {
                if (counter++ > 0) {
                    buffer.append(", ");
                }
                String text = (object == null) ? "null" : object.toString();
                buffer.append(text);
            }
            buffer.append("}");
            return buffer.toString();
        }
    }

    /**
     * Returns true if a class is assignable from another class like the {@linksClass#isAssignableFrom(Class)} method
     * but which also includes coercion between primitive types to deal with Java 5 primitive type wrapping
     */
    public static boolean isAssignableFrom(Class<?> a, Class<?> b) {
        a = convertPrimitiveTypeToWrapperType(a);
        b = convertPrimitiveTypeToWrapperType(b);
        return a.isAssignableFrom(b);
    }

    /**
     * Returns if the given {@code clazz} type is a Java primitive array type.
     *
     * @param clazz the Java type to be checked
     * @return {@code true} if the given type is a Java primitive array type
     */
    public static boolean isPrimitiveArrayType(Class<?> clazz) {
        if (clazz != null && clazz.isArray()) {
            return clazz.getComponentType().isPrimitive();
        }
        return false;
    }

    public static int arrayLength(Object[] pojo) {
        return pojo.length;
    }

    /**
     * Converts primitive types such as int to its wrapper type like {@linksInteger}
     */
    public static Class<?> convertPrimitiveTypeToWrapperType(Class<?> type) {
        Class<?> rc = type;
        if (type.isPrimitive()) {
            if (type == int.class) {
                rc = Integer.class;
            } else if (type == long.class) {
                rc = Long.class;
            } else if (type == double.class) {
                rc = Double.class;
            } else if (type == float.class) {
                rc = Float.class;
            } else if (type == short.class) {
                rc = Short.class;
            } else if (type == byte.class) {
                rc = Byte.class;
            } else if (type == boolean.class) {
                rc = Boolean.class;
            } else if (type == char.class) {
                rc = Character.class;
            }
        }
        return rc;
    }

    /**
     * Helper method to return the default character set name
     */
    public static String getDefaultCharacterSet() {
        return Charset.defaultCharset().name();
    }

    /**
     * Returns the Java Bean property name of the given method, if it is a setter
     */
    public static String getPropertyName(Method method) {
        String propertyName = method.getName();
        if (propertyName.startsWith("set") && method.getParameterCount() == 1) {
            propertyName = propertyName.substring(3, 4).toLowerCase(Locale.ENGLISH) + propertyName.substring(4);
        }
        return propertyName;
    }

    /**
     * Returns true if the given collection of annotations matches the given type
     */
    public static boolean hasAnnotation(Annotation[] annotations, Class<?> type) {
        for (Annotation annotation : annotations) {
            if (type.isInstance(annotation)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the annotation from the given instance.
     *
     * @param instance the instance
     * @param type the annotation
     * @return the annotation, or <tt>null</tt> if the instance does not have the given annotation
     */
    public static <A extends java.lang.annotation.Annotation> A getAnnotation(Object instance, Class<A> type) {
        return instance.getClass().getAnnotation(type);
    }

    /**
     * Closes the given resource if it is available, logging any closing exceptions to the given log
     *
     * @param closeable the object to close
     * @param name the name of the resource
     * @deprecated will be removed in Camel 3.0. Instead use
     *             {@linksorg.apache.camel.util.IOHelper#close(java.io.Closeable, String, org.slf4j.Logger)} instead
     */
    @Deprecated
    public static void close(Closeable closeable, String name) {
        StreamCommand.close(closeable, name);
    }

    /**
     * Converts the given value to the required type or throw a meaningful exception
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Class<T> toType, Object value) {
        if (toType == boolean.class) {
            return (T) cast(Boolean.class, value);
        } else if (toType.isPrimitive()) {
            Class<?> newType = convertPrimitiveTypeToWrapperType(toType);
            if (newType != toType) {
                return (T) cast(newType, value);
            }
        }
        try {
            return toType.cast(value);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Failed to convert: "
                    + value + " to type: " + toType.getName() + " due to: " + e, e);
        }
    }

    /**
     * A helper method to create a new instance of a type using the default constructor arguments.
     */
    public static <T> T newInstance(Class<T> type) {
        try {
            return type.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * A helper method to create a new instance of a type using the default constructor arguments.
     */
    public static <T> T newInstance(Class<?> actualType, Class<T> expectedType) {
        try {
            Object value = actualType.newInstance();
            return cast(expectedType, value);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Does the given class have a default public no-arg constructor.
     */
    public static boolean hasDefaultPublicNoArgConstructor(Class<?> type) {
        // getConstructors() returns only public constructors
        for (Constructor<?> ctr : type.getConstructors()) {
            if (ctr.getParameterCount() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the given name is a valid java identifier
     *
     * @deprecated use {@linksStringCommand#isJavaIdentifier(String)} instead
     */
    @Deprecated
    public static boolean isJavaIdentifier(String name) {
        return StringCommand.isJavaIdentifier(name);
    }

    /**
     * Returns the type of the given object or null if the value is null
     */
    public static Object type(Object bean) {
        return bean != null ? bean.getClass() : null;
    }

    /**
     * Evaluate the value as a predicate which attempts to convert the value to a boolean otherwise true is returned if
     * the value is not null
     */
    public static boolean evaluateValuePredicate(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof String) {
            if ("true".equalsIgnoreCase((String) value)) {
                return true;
            } else if ("false".equalsIgnoreCase((String) value)) {
                return false;
            }
        } else if (value instanceof NodeList) {
            // is it an empty dom with empty attributes
            if (value instanceof Node && ((Node) value).hasAttributes()) {
                return true;
            }
            NodeList list = (NodeList) value;
            return list.getLength() > 0;
        } else if (value instanceof Collection) {
            // is it an empty collection
            Collection<?> col = (Collection<?>) value;
            return col.size() > 0;
        }
        return value != null;
    }

    /**
     * Cleans the string to a pure Java identifier so we can use it for loading class names.
     * <p/>
     * Especially from Spring DSL people can have \n \t or other characters that otherwise would result in
     * ClassNotFoundException
     *
     * @param name the class name
     * @return normalized classname that can be load by a class loader.
     * @deprecated use {@linksStringCommand#normalizeClassName(String)} instead
     */
    @Deprecated
    public static String normalizeClassName(String name) {
        return StringCommand.normalizeClassName(name);
    }

    /**
     * Creates an Iterable to walk the exception from the bottom up (the last caused by going upwards to the root
     * exception).
     *
     * @param exception the exception
     * @return the Iterable
     * @see java.lang.Iterable
     */
    public static Iterable<Throwable> createExceptionIterable(Throwable exception) {
        List<Throwable> throwables = new ArrayList<>();

        Throwable current = exception;
        // spool to the bottom of the caused by tree
        while (current != null) {
            throwables.add(current);
            current = current.getCause();
        }
        Collections.reverse(throwables);

        return throwables;
    }

    /**
     * Creates an Iterator to walk the exception from the bottom up (the last caused by going upwards to the root
     * exception).
     *
     * @param exception the exception
     * @return the Iterator
     * @see Iterator
     */
    public static Iterator<Throwable> createExceptionIterator(Throwable exception) {
        return createExceptionIterable(exception).iterator();
    }

    /**
     * Retrieves the given exception type from the exception.
     * <p/>
     * Is used to get the caused exception that typically have been wrapped in some sort of Camel wrapper exception
     * <p/>
     * The strategy is to look in the exception hierarchy to find the first given cause that matches the type. Will
     * start from the bottom (the real cause) and walk upwards.
     *
     * @param type the exception type wanted to retrieve
     * @param exception the caused exception
     * @return the exception found (or <tt>null</tt> if not found in the exception hierarchy)
     */
    public static <T> T getException(Class<T> type, Throwable exception) {
        if (exception == null) {
            return null;
        }

        // check the suppressed exception first
        for (Throwable throwable : exception.getSuppressed()) {
            if (type.isInstance(throwable)) {
                return type.cast(throwable);
            }
        }

        // walk the hierarchy and look for it
        for (final Throwable throwable : createExceptionIterable(exception)) {
            if (type.isInstance(throwable)) {
                return type.cast(throwable);
            }
        }

        // not found
        return null;
    }

    public static String getIdentityHashCode(Object object) {
        return "0x" + Integer.toHexString(System.identityHashCode(object));
    }

    /**
     * Lookup the constant field on the given class with the given name
     *
     * @param clazz the class
     * @param name the name of the field to lookup
     * @return the value of the constant field, or <tt>null</tt> if not found
     */
    public static String lookupConstantFieldValue(Class<?> clazz, String name) {
        if (clazz == null) {
            return null;
        }

        // remove leading dots
        if (name.startsWith(",")) {
            name = name.substring(1);
        }

        for (Field field : clazz.getFields()) {
            if (field.getName().equals(name)) {
                try {
                    Object v = field.get(null);
                    return v.toString();
                } catch (IllegalAccessException e) {
                    // ignore
                    return null;
                }
            }
        }

        return null;
    }

    /**
     * Is the given value a numeric NaN type
     *
     * @param value the value
     * @return <tt>true</tt> if its a {@linksFloat#NaN} or {@linksDouble#NaN}.
     */
    public static boolean isNaN(Object value) {
        if (value == null || !(value instanceof Number)) {
            return false;
        }
        // value must be a number
        return value.equals(Float.NaN) || value.equals(Double.NaN);
    }

    /**
     * Calling the Callable with the setting of TCCL with a given classloader.
     *
     * @param call the Callable instance
     * @param classloader the class loader
     * @return the result of Callable return
     */
    public static Object callWithTCCL(Callable<?> call, ClassLoader classloader) throws Exception {
        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        try {
            if (classloader != null) {
                Thread.currentThread().setContextClassLoader(classloader);
            }
            return call.call();
        } finally {
            if (tccl != null) {
                Thread.currentThread().setContextClassLoader(tccl);
            }
        }
    }

    /**
     * Return whether the given throwable is a checked exception: that is, neither a RuntimeException nor an Error.
     *
     * @param ex the throwable to check
     * @return whether the throwable is a checked exception
     * @see java.lang.Exception
     * @see java.lang.RuntimeException
     * @see java.lang.Error
     */
    public static boolean isCheckedException(Throwable ex) {
        return !(ex instanceof RuntimeException || ex instanceof Error);
    }

    /**
     * Check whether the given exception is compatible with the specified exception types, as declared in a throws
     * clause.
     *
     * @param ex the exception to check
     * @param declaredExceptions the exception types declared in the throws clause
     * @return whether the given exception is compatible
     */
    public static boolean isCompatibleWithThrowsClause(Throwable ex, @Nullable Class<?>...declaredExceptions) {
        if (!isCheckedException(ex)) {
            return true;
        }
        if (declaredExceptions != null) {
            for (Class<?> declaredException : declaredExceptions) {
                if (declaredException.isInstance(ex)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determine whether the given object is an array: either an Object array or a primitive array.
     *
     * @param obj the object to check
     */
    public static boolean isArray(@Nullable Object obj) {
        return (obj != null && obj.getClass().isArray());
    }

    /**
     * Determine whether the given array is empty: i.e. {@code null} or of zero length.
     *
     * @param array the array to check
     * @see #isEmpty(Object)
     */
    public static boolean isEmpty(@Nullable Object[] array) {
        return (array == null || array.length == 0);
    }

    /**
     * Determine whether the given object is empty.
     * <p>
     * This method supports the following object types.
     * <ul>
     * <li>{@code Optional}: considered empty if {@linksOptional#empty()}</li>
     * <li>{@code Array}: considered empty if its length is zero</li>
     * <li>{@linksCharSequence}: considered empty if its length is zero</li>
     * <li>{@linksCollection}: delegates to {@linksCollection#isEmpty()}</li>
     * <li>{@linksMap}: delegates to {@linksMap#isEmpty()}</li>
     * </ul>
     * <p>
     * If the given object is non-null and not one of the aforementioned supported types, this method returns
     * {@code false}.
     *
     * @param obj the object to check
     * @return {@code true} if the object is {@code null} or <em>empty</em>
     * @tsee Optional#isPresent()
     * @tsee ObjectUtils isEmpty(Object[])
     * @tsee StringUtils hasLength(CharSequence)
     * @tsee StringUtils isEmpty(Object)
     * @tsee CollectionUtils isEmpty(java.util.Collection)
     * @tsee CollectionUtils isEmpty(java.util.Map)
     * @since 4.2
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(@Nullable Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof Optional) {
            return !((Optional) obj).isPresent();
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        // else
        return false;
    }

    /**
     * Unwrap the given object which is potentially a {@linksjava.util.Optional}.
     *
     * @param obj the candidate object
     * @return either the value held within the {@code Optional}, {@code null} if the {@code Optional} is empty, or
     *         simply the given object as-is
     * @since 5.0
     */
    @Nullable
    public static Object unwrapOptional(@Nullable Object obj) {
        if (obj instanceof Optional) {
            Optional<?> optional = (Optional<?>) obj;
            if (!optional.isPresent()) {
                return null;
            }
            Object result = optional.get();
            AssertCommand.isTrue(!(result instanceof Optional), "Multi-level Optional usage not supported");
            return result;
        }
        return obj;
    }

    /**
     * Check whether the given array contains the given element.
     *
     * @param array the array to check (may be {@code null}, in which case the return value will always be
     *            {@code false})
     * @param element the element to check for
     * @return whether the element has been found in the given array
     */
    public static boolean containsElement(@Nullable Object[] array, Object element) {
        if (array == null) {
            return false;
        }
        for (Object arrayEle : array) {
            if (nullSafeEquals(arrayEle, element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the given array of enum constants contains a constant with the given name, ignoring case when
     * determining a match.
     *
     * @param enumValues the enum values to check, typically obtained via {@code MyEnum.values()}
     * @param constant the constant name to find (must not be null or empty string)
     * @return whether the constant has been found in the given array
     */
    public static boolean containsConstant(Enum<?>[] enumValues, String constant) {
        return containsConstant(enumValues, constant, false);
    }

    /**
     * Check whether the given array of enum constants contains a constant with the given name.
     *
     * @param enumValues the enum values to check, typically obtained via {@code MyEnum.values()}
     * @param constant the constant name to find (must not be null or empty string)
     * @param caseSensitive whether case is significant in determining a match
     * @return whether the constant has been found in the given array
     */
    public static boolean containsConstant(Enum<?>[] enumValues, String constant, boolean caseSensitive) {
        for (Enum<?> candidate : enumValues) {
            if (caseSensitive ? candidate.toString().equals(constant)
                    : candidate.toString().equalsIgnoreCase(constant)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Case insensitive alternative to {@linksEnum#valueOf(Class, String)}.
     *
     * @param <E> the concrete Enum type
     * @param enumValues the array of all Enum constants in question, usually per {@code Enum.values()}
     * @param constant the constant to get the enum value of
     * @throws IllegalArgumentException if the given constant is not found in the given array of enum values. Use
     *             {@links#containsConstant(Enum[], String)} as a guard to avoid this exception.
     */
    public static <E extends Enum<?>> E caseInsensitiveValueOf(E[] enumValues, String constant) {
        for (E candidate : enumValues) {
            if (candidate.toString().equalsIgnoreCase(constant)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Constant [" + constant + "] does not exist in enum type " +
                enumValues.getClass().getComponentType().getName());
    }

    /**
     * Append the given object to the given array, returning a new array consisting of the input array contents plus the
     * given object.
     *
     * @param array the array to append to (can be {@code null})
     * @param obj the object to append
     * @return the new array (of the same component type; never {@code null})
     */
    public static <A, O extends A> A[] addObjectToArray(@Nullable A[] array, @Nullable O obj) {
        Class<?> compType = Object.class;
        if (array != null) {
            compType = array.getClass().getComponentType();
        } else if (obj != null) {
            compType = obj.getClass();
        }
        int newArrLength = (array != null ? array.length + 1 : 1);
        @SuppressWarnings("unchecked")
        A[] newArr = (A[]) Array.newInstance(compType, newArrLength);
        if (array != null) {
            System.arraycopy(array, 0, newArr, 0, array.length);
        }
        newArr[newArr.length - 1] = obj;
        return newArr;
    }

    /**
     * Convert the given array (which may be a primitive array) to an object array (if necessary of primitive wrapper
     * objects).
     * <p>
     * A {@code null} source value will be converted to an empty Object array.
     *
     * @param source the (potentially primitive) array
     * @return the corresponding object array (never {@code null})
     * @throws IllegalArgumentException if the parameter is not an array
     */
    public static Object[] toObjectArray(@Nullable Object source) {
        if (source instanceof Object[]) {
            return (Object[]) source;
        }
        if (source == null) {
            return new Object[0];
        }
        if (!source.getClass().isArray()) {
            throw new IllegalArgumentException("Source is not an array: " + source);
        }
        int length = Array.getLength(source);
        if (length == 0) {
            return new Object[0];
        }
        Class<?> wrapperType = Array.get(source, 0).getClass();
        Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
        for (int i = 0; i < length; i++) {
            newArray[i] = Array.get(source, i);
        }
        return newArray;
    }

    // ---------------------------------------------------------------------
    // Convenience methods for content-based equality/hash-code handling
    // ---------------------------------------------------------------------

    /**
     * Determine if the given objects are equal, returning {@code true} if both are {@code null} or {@code false} if
     * only one is {@code null}.
     * <p>
     * Compares arrays with {@code Arrays.equals}, performing an equality check based on the array elements rather than
     * the array reference.
     *
     * @param o1 first Object to compare
     * @param o2 second Object to compare
     * @return whether the given objects are equal
     * @see Object#equals(Object)
     * @see java.util.Arrays#equals
     */
    public static boolean nullSafeEquals(@Nullable Object o1, @Nullable Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        if (o1.equals(o2)) {
            return true;
        }
        if (o1.getClass().isArray() && o2.getClass().isArray()) {
            return arrayEquals(o1, o2);
        }
        return false;
    }

    /**
     * Compare the given arrays with {@code Arrays.equals}, performing an equality check based on the array elements
     * rather than the array reference.
     *
     * @param o1 first array to compare
     * @param o2 second array to compare
     * @return whether the given objects are equal
     * @see #nullSafeEquals(Object, Object)
     * @see java.util.Arrays#equals
     */
    private static boolean arrayEquals(Object o1, Object o2) {
        if (o1 instanceof Object[] && o2 instanceof Object[]) {
            return Arrays.equals((Object[]) o1, (Object[]) o2);
        }
        if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
            return Arrays.equals((boolean[]) o1, (boolean[]) o2);
        }
        if (o1 instanceof byte[] && o2 instanceof byte[]) {
            return Arrays.equals((byte[]) o1, (byte[]) o2);
        }
        if (o1 instanceof char[] && o2 instanceof char[]) {
            return Arrays.equals((char[]) o1, (char[]) o2);
        }
        if (o1 instanceof double[] && o2 instanceof double[]) {
            return Arrays.equals((double[]) o1, (double[]) o2);
        }
        if (o1 instanceof float[] && o2 instanceof float[]) {
            return Arrays.equals((float[]) o1, (float[]) o2);
        }
        if (o1 instanceof int[] && o2 instanceof int[]) {
            return Arrays.equals((int[]) o1, (int[]) o2);
        }
        if (o1 instanceof long[] && o2 instanceof long[]) {
            return Arrays.equals((long[]) o1, (long[]) o2);
        }
        if (o1 instanceof short[] && o2 instanceof short[]) {
            return Arrays.equals((short[]) o1, (short[]) o2);
        }
        return false;
    }

    /**
     * Return as hash code for the given object; typically the value of {@code Object#hashCode()}}. If the object is an
     * array, this method will delegate to any of the {@code nullSafeHashCode} methods for arrays in this class. If the
     * object is {@code null}, this method returns 0.
     *
     * @see Object#hashCode()
     * @see #nullSafeHashCode(Object[])
     * @see #nullSafeHashCode(boolean[])
     * @see #nullSafeHashCode(byte[])
     * @see #nullSafeHashCode(char[])
     * @see #nullSafeHashCode(double[])
     * @see #nullSafeHashCode(float[])
     * @see #nullSafeHashCode(int[])
     * @see #nullSafeHashCode(long[])
     * @see #nullSafeHashCode(short[])
     */
    public static int nullSafeHashCode(@Nullable Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj.getClass().isArray()) {
            if (obj instanceof Object[]) {
                return nullSafeHashCode((Object[]) obj);
            }
            if (obj instanceof boolean[]) {
                return nullSafeHashCode((boolean[]) obj);
            }
            if (obj instanceof byte[]) {
                return nullSafeHashCode((byte[]) obj);
            }
            if (obj instanceof char[]) {
                return nullSafeHashCode((char[]) obj);
            }
            if (obj instanceof double[]) {
                return nullSafeHashCode((double[]) obj);
            }
            if (obj instanceof float[]) {
                return nullSafeHashCode((float[]) obj);
            }
            if (obj instanceof int[]) {
                return nullSafeHashCode((int[]) obj);
            }
            if (obj instanceof long[]) {
                return nullSafeHashCode((long[]) obj);
            }
            if (obj instanceof short[]) {
                return nullSafeHashCode((short[]) obj);
            }
        }
        return obj.hashCode();
    }

    /**
     * Return a hash code based on the contents of the specified array. If {@code array} is {@code null}, this method
     * returns 0.
     */
    public static int nullSafeHashCode(@Nullable Object[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (Object element : array) {
            hash = MULTIPLIER * hash + nullSafeHashCode(element);
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array. If {@code array} is {@code null}, this method
     * returns 0.
     */
    public static int nullSafeHashCode(@Nullable boolean[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (boolean element : array) {
            hash = MULTIPLIER * hash + Boolean.hashCode(element);
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array. If {@code array} is {@code null}, this method
     * returns 0.
     */
    public static int nullSafeHashCode(@Nullable byte[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (byte element : array) {
            hash = MULTIPLIER * hash + element;
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array. If {@code array} is {@code null}, this method
     * returns 0.
     */
    public static int nullSafeHashCode(@Nullable char[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (char element : array) {
            hash = MULTIPLIER * hash + element;
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array. If {@code array} is {@code null}, this method
     * returns 0.
     */
    public static int nullSafeHashCode(@Nullable double[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (double element : array) {
            hash = MULTIPLIER * hash + Double.hashCode(element);
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array. If {@code array} is {@code null}, this method
     * returns 0.
     */
    public static int nullSafeHashCode(@Nullable float[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (float element : array) {
            hash = MULTIPLIER * hash + Float.hashCode(element);
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array. If {@code array} is {@code null}, this method
     * returns 0.
     */
    public static int nullSafeHashCode(@Nullable int[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (int element : array) {
            hash = MULTIPLIER * hash + element;
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array. If {@code array} is {@code null}, this method
     * returns 0.
     */
    public static int nullSafeHashCode(@Nullable long[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (long element : array) {
            hash = MULTIPLIER * hash + Long.hashCode(element);
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array. If {@code array} is {@code null}, this method
     * returns 0.
     */
    public static int nullSafeHashCode(@Nullable short[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (short element : array) {
            hash = MULTIPLIER * hash + element;
        }
        return hash;
    }

    /**
     * Return the same value as {@linksBoolean#hashCode(boolean)}}.
     *
     * @deprecated as of Spring Framework 5.0, in favor of the native JDK 8 variant
     */
    @Deprecated
    public static int hashCode(boolean bool) {
        return Boolean.hashCode(bool);
    }

    /**
     * Return the same value as {@linksDouble#hashCode(double)}}.
     *
     * @deprecated as of Spring Framework 5.0, in favor of the native JDK 8 variant
     */
    @Deprecated
    public static int hashCode(double dbl) {
        return Double.hashCode(dbl);
    }

    /**
     * Return the same value as {@linksFloat#hashCode(float)}}.
     *
     * @deprecated as of Spring Framework 5.0, in favor of the native JDK 8 variant
     */
    @Deprecated
    public static int hashCode(float flt) {
        return Float.hashCode(flt);
    }

    /**
     * Return the same value as {@linksLong#hashCode(long)}}.
     *
     * @deprecated as of Spring Framework 5.0, in favor of the native JDK 8 variant
     */
    @Deprecated
    public static int hashCode(long lng) {
        return Long.hashCode(lng);
    }

    // ---------------------------------------------------------------------
    // Convenience methods for toString output
    // ---------------------------------------------------------------------

    /**
     * Return a String representation of an object's overall identity.
     *
     * @param obj the object (may be {@code null})
     * @return the object's identity as String representation, or an empty String if the object was {@code null}
     */
    public static String identityToString(@Nullable Object obj) {
        if (obj == null) {
            return EMPTY_STRING;
        }
        return obj.getClass().getName() + "@" + getIdentityHexString(obj);
    }

    /**
     * Return a hex String form of an object's identity hash code.
     *
     * @param obj the object
     * @return the object's identity code in hex notation
     */
    public static String getIdentityHexString(Object obj) {
        return Integer.toHexString(System.identityHashCode(obj));
    }

    /**
     * Return a content-based String representation if {@code obj} is not {@code null}; otherwise returns an empty
     * String.
     * <p>
     * Differs from {@links#nullSafeToString(Object)} in that it returns an empty String rather than "null" for a
     * {@code null} value.
     *
     * @param obj the object to build a display String for
     * @return a display String representation of {@code obj}
     * @see #nullSafeToString(Object)
     */
    public static String getDisplayString(@Nullable Object obj) {
        if (obj == null) {
            return EMPTY_STRING;
        }
        return nullSafeToString(obj);
    }

    /**
     * Determine the class name for the given object.
     * <p>
     * Returns {@code "null"} if {@code obj} is {@code null}.
     *
     * @param obj the object to introspect (may be {@code null})
     * @return the corresponding class name
     */
    public static String nullSafeClassName(@Nullable Object obj) {
        return (obj != null ? obj.getClass().getName() : NULL_STRING);
    }

    /**
     * Return a String representation of the specified Object.
     * <p>
     * Builds a String representation of the contents in case of an array. Returns {@code "null"} if {@code obj} is
     * {@code null}.
     *
     * @param obj the object to build a String representation for
     * @return a String representation of {@code obj}
     */
    public static String nullSafeToString(@Nullable Object obj) {
        if (obj == null) {
            return NULL_STRING;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof Object[]) {
            return nullSafeToString((Object[]) obj);
        }
        if (obj instanceof boolean[]) {
            return nullSafeToString((boolean[]) obj);
        }
        if (obj instanceof byte[]) {
            return nullSafeToString((byte[]) obj);
        }
        if (obj instanceof char[]) {
            return nullSafeToString((char[]) obj);
        }
        if (obj instanceof double[]) {
            return nullSafeToString((double[]) obj);
        }
        if (obj instanceof float[]) {
            return nullSafeToString((float[]) obj);
        }
        if (obj instanceof int[]) {
            return nullSafeToString((int[]) obj);
        }
        if (obj instanceof long[]) {
            return nullSafeToString((long[]) obj);
        }
        if (obj instanceof short[]) {
            return nullSafeToString((short[]) obj);
        }
        String str = obj.toString();
        return (str != null ? str : EMPTY_STRING);
    }

    /**
     * Return a String representation of the contents of the specified array.
     * <p>
     * The String representation consists of a list of the array's elements, enclosed in curly braces ({@code "{}"}).
     * Adjacent elements are separated by the characters {@code ", "} (a comma followed by a space). Returns
     * {@code "null"} if {@code array} is {@code null}.
     *
     * @param array the array to build a String representation for
     * @return a String representation of {@code array}
     */
    public static String nullSafeToString(@Nullable Object[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATOR);
            }
            sb.append(String.valueOf(array[i]));
        }
        sb.append(ARRAY_END);
        return sb.toString();
    }

    /**
     * Return a String representation of the contents of the specified array.
     * <p>
     * The String representation consists of a list of the array's elements, enclosed in curly braces ({@code "{}"}).
     * Adjacent elements are separated by the characters {@code ", "} (a comma followed by a space). Returns
     * {@code "null"} if {@code array} is {@code null}.
     *
     * @param array the array to build a String representation for
     * @return a String representation of {@code array}
     */
    public static String nullSafeToString(@Nullable boolean[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATOR);
            }

            sb.append(array[i]);
        }
        sb.append(ARRAY_END);
        return sb.toString();
    }

    /**
     * Return a String representation of the contents of the specified array.
     * <p>
     * The String representation consists of a list of the array's elements, enclosed in curly braces ({@code "{}"}).
     * Adjacent elements are separated by the characters {@code ", "} (a comma followed by a space). Returns
     * {@code "null"} if {@code array} is {@code null}.
     *
     * @param array the array to build a String representation for
     * @return a String representation of {@code array}
     */
    public static String nullSafeToString(@Nullable byte[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATOR);
            }
            sb.append(array[i]);
        }
        sb.append(ARRAY_END);
        return sb.toString();
    }

    /**
     * Return a String representation of the contents of the specified array.
     * <p>
     * The String representation consists of a list of the array's elements, enclosed in curly braces ({@code "{}"}).
     * Adjacent elements are separated by the characters {@code ", "} (a comma followed by a space). Returns
     * {@code "null"} if {@code array} is {@code null}.
     *
     * @param array the array to build a String representation for
     * @return a String representation of {@code array}
     */
    public static String nullSafeToString(@Nullable char[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATOR);
            }
            sb.append("'").append(array[i]).append("'");
        }
        sb.append(ARRAY_END);
        return sb.toString();
    }

    /**
     * Return a String representation of the contents of the specified array.
     * <p>
     * The String representation consists of a list of the array's elements, enclosed in curly braces ({@code "{}"}).
     * Adjacent elements are separated by the characters {@code ", "} (a comma followed by a space). Returns
     * {@code "null"} if {@code array} is {@code null}.
     *
     * @param array the array to build a String representation for
     * @return a String representation of {@code array}
     */
    public static String nullSafeToString(@Nullable double[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATOR);
            }

            sb.append(array[i]);
        }
        sb.append(ARRAY_END);
        return sb.toString();
    }

    /**
     * Return a String representation of the contents of the specified array.
     * <p>
     * The String representation consists of a list of the array's elements, enclosed in curly braces ({@code "{}"}).
     * Adjacent elements are separated by the characters {@code ", "} (a comma followed by a space). Returns
     * {@code "null"} if {@code array} is {@code null}.
     *
     * @param array the array to build a String representation for
     * @return a String representation of {@code array}
     */
    public static String nullSafeToString(@Nullable float[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATOR);
            }

            sb.append(array[i]);
        }
        sb.append(ARRAY_END);
        return sb.toString();
    }

    /**
     * Return a String representation of the contents of the specified array.
     * <p>
     * The String representation consists of a list of the array's elements, enclosed in curly braces ({@code "{}"}).
     * Adjacent elements are separated by the characters {@code ", "} (a comma followed by a space). Returns
     * {@code "null"} if {@code array} is {@code null}.
     *
     * @param array the array to build a String representation for
     * @return a String representation of {@code array}
     */
    public static String nullSafeToString(@Nullable int[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATOR);
            }
            sb.append(array[i]);
        }
        sb.append(ARRAY_END);
        return sb.toString();
    }

    /**
     * Return a String representation of the contents of the specified array.
     * <p>
     * The String representation consists of a list of the array's elements, enclosed in curly braces ({@code "{}"}).
     * Adjacent elements are separated by the characters {@code ", "} (a comma followed by a space). Returns
     * {@code "null"} if {@code array} is {@code null}.
     *
     * @param array the array to build a String representation for
     * @return a String representation of {@code array}
     */
    public static String nullSafeToString(@Nullable long[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATOR);
            }
            sb.append(array[i]);
        }
        sb.append(ARRAY_END);
        return sb.toString();
    }

    /**
     * Return a String representation of the contents of the specified array.
     * <p>
     * The String representation consists of a list of the array's elements, enclosed in curly braces ({@code "{}"}).
     * Adjacent elements are separated by the characters {@code ", "} (a comma followed by a space). Returns
     * {@code "null"} if {@code array} is {@code null}.
     *
     * @param array the array to build a String representation for
     * @return a String representation of {@code array}
     */
    public static String nullSafeToString(@Nullable short[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATOR);
            }
            sb.append(array[i]);
        }
        sb.append(ARRAY_END);
        return sb.toString();
    }

    /**
     * 计算对象长度，如果是字符串调用其length函数，集合类调用其size函数，数组调用其length属性，其他可遍历对象遍历计算长度<br>
     * 支持的类型包括：
     * <ul>
     * <li>CharSequence</li>
     * <li>Map</li>
     * <li>Iterator</li>
     * <li>Enumeration</li>
     * <li>Array</li>
     * </ul>
     *
     * @param obj 被计算长度的对象
     * @return 长度
     */
    public static int length(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length();
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).size();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).size();
        }

        int count;
        if (obj instanceof Iterator) {
            Iterator<?> iter = (Iterator<?>) obj;
            count = 0;
            while (iter.hasNext()) {
                count++;
                iter.next();
            }
            return count;
        }
        if (obj instanceof Enumeration) {
            Enumeration<?> enumeration = (Enumeration<?>) obj;
            count = 0;
            while (enumeration.hasMoreElements()) {
                count++;
                enumeration.nextElement();
            }
            return count;
        }
        if (obj.getClass().isArray() == true) {
            return Array.getLength(obj);
        }
        return -1;
    }

    /**
     * 对象中是否包含元素<br>
     * 支持的对象类型包括：
     * <ul>
     * <li>String</li>
     * <li>Collection</li>
     * <li>Map</li>
     * <li>Iterator</li>
     * <li>Enumeration</li>
     * <li>Array</li>
     * </ul>
     *
     * @param obj 对象
     * @param element 元素
     * @return 是否包含
     */
    public static boolean contains(Object obj, Object element) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof String) {
            if (element == null) {
                return false;
            }
            return ((String) obj).contains(element.toString());
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).contains(element);
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).values().contains(element);
        }

        if (obj instanceof Iterator) {
            Iterator<?> iter = (Iterator<?>) obj;
            while (iter.hasNext()) {
                Object o = iter.next();
                if (equal(o, element)) {
                    return true;
                }
            }
            return false;
        }
        if (obj instanceof Enumeration) {
            Enumeration<?> enumeration = (Enumeration<?>) obj;
            while (enumeration.hasMoreElements()) {
                Object o = enumeration.nextElement();
                if (equal(o, element)) {
                    return true;
                }
            }
            return false;
        }
        if (obj.getClass().isArray() == true) {
            int len = Array.getLength(obj);
            for (int i = 0; i < len; i++) {
                Object o = Array.get(obj, i);
                if (equal(o, element)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查对象是否为null
     *
     * @param obj 对象
     * @return 是否为null
     */
    public static boolean isNull(Object obj) {
        return null == obj;
    }

    /**
     * 检查对象是否不为null
     *
     * @param obj 对象
     * @return 是否为null
     */
    public static boolean isNotNull(Object obj) {
        return null != obj;
    }

    /**
     * 克隆对象<br>
     * 如果对象实现Cloneable接口，调用其clone方法<br>
     * 如果实现Serializable接口，执行深度克隆<br>
     * 否则返回<code>null</code>
     *
     * @param obj 被克隆对象
     * @return 克隆后的对象
     */
    public static <T> T clone(T obj) {
        T result = ArrayCommand.clone(obj);
        if (null == result) {
            if (obj instanceof Cloneable) {
                result = ClassCommand.invoke(obj, "clone", new Object[] {});
            } else {
                result = cloneByStream(obj);
            }
        }
        return result;
    }

    /**
     * 返回克隆后的对象，如果克隆失败，返回原对象
     *
     * @param obj 对象
     * @return 克隆后或原对象
     */
    public static <T> T cloneIfPossible(final T obj) {
        T clone = null;
        try {
            clone = clone(obj);
        } catch (Exception e) {
            // pass
        }
        return clone == null ? obj : clone;
    }

    /**
     * 序列化后拷贝流的方式克隆<br>
     * 对象必须实现Serializable接口
     *
     * @param obj 被克隆对象
     * @return 克隆后的对象
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public static <T> T cloneByStream(T obj) {
        if (null == obj || false == (obj instanceof Serializable)) {
            return null;
        }
        final FastByteArrayOutputStream byteOut = new FastByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(byteOut);
            out.writeObject(obj);
            out.flush();
            final ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteOut.toByteArray()));
            return (T) in.readObject();
        } catch (Exception e) {
            throw new UtilException(e);
        } finally {
            IoCommand.close(out);
        }
    }

    public static Object cloneAttribute(Object from, Object to) {
        Field[] fieldClone = null;
        Field[] fieldBeCloned = null;
        Map<String, Field> map = new HashMap<String, Field>();
        try {
            Class<?> classClone = from.getClass();
            Class<?> classBecloned = to.getClass();

            fieldClone = classClone.getDeclaredFields();
            fieldBeCloned = classBecloned.getDeclaredFields();

            for (int t = 0; t < fieldBeCloned.length; t++) {
                map.put(fieldBeCloned[t].getName(), fieldBeCloned[t]);
            }

            for (int i = 0; i < fieldClone.length; i++) {
                String fieldCloneName = fieldClone[i].getName();
                Field fie = map.get(fieldCloneName);
                if (fie != null) {
                    Method method1 = classClone.getMethod(getMethodName(fieldCloneName));
                    Method method2 = classBecloned.getMethod(setMethodName(fieldCloneName), fie.getType());
                    method2.invoke(to, method1.invoke(from));
                }
            }
        } catch (Exception e) {
        } finally {
            fieldClone = null;
            fieldBeCloned = null;
            map.clear();
        }
        return to;
    }

    private static String getMethodName(String fieldName) {
        String head = fieldName.substring(0, 1).toUpperCase();
        String tail = fieldName.substring(1);
        return "get" + head + tail;
    }

    private static String setMethodName(String fieldName) {
        String head = fieldName.substring(0, 1).toUpperCase();
        String tail = fieldName.substring(1);
        return "set" + head + tail;
    }

    public static Map<String, Object> transBean2Map(Object obj, Boolean includeNull) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (!includeNull && value == null) {
                        continue;
                    }
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }
        return map;
    }

    public static <T> T transMap2Bean(Map<String, Object> map, Class<T> clazz) {
        return transMap2Bean(map, clazz, null);
    }

    public static <T> T transMap2Bean(Map<String, Object> map, Builder<T> builder) {
        return transMap2Bean(map, builder, null);
    }

    public static <T> T transMap2Bean(Map<String, Object> map, Class<T> clazz, String keyPrefix) {
        T obj = null;
        BeanInfo beanInfo = null;
        try {
            obj = clazz.newInstance();
            beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                if ("class".equals(property.getName())) {
                    continue;
                }
                String key = keyPrefix == null ? property.getName() : keyPrefix + property.getName();
                Object value = map.get(key);
                if (value == null) {
                    continue;
                }
                // 得到property对应的setter方法
                Method setter = property.getWriteMethod();
                setter.invoke(obj, value);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static <T> T transMap2Bean(Map<String, Object> map, Builder<T> builder, String keyPrefix) {
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(builder.getClass());
            MethodDescriptor[] methodDescriptor = beanInfo.getMethodDescriptors();
            for (MethodDescriptor descriptor : methodDescriptor) {
                if ("build".equals(descriptor.getName())) {
                    continue;
                }
                String key = keyPrefix == null ? descriptor.getName() : keyPrefix + descriptor.getName();
                Object value = map.get(key);
                if (value == null) {
                    continue;
                }
                Method bmethod = descriptor.getMethod();
                if (bmethod.getParameterCount() != 1) {
                    continue;
                }
                if (bmethod.getParameterTypes()[0].isAssignableFrom(value.getClass())) {
                    bmethod.invoke(builder, value);
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    public static <S, T> T generateCopy(S source, Class<T> targetClazz) {
        T bean = null;
        try {
            bean = targetClazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (bean == null || source == null) {
            return null;
        }
        Map<String, PropertyDescriptor> filed2Descriptor = new HashMap<>();
        try {
            BeanInfo sourceBeanInfo = Introspector.getBeanInfo(source.getClass());
            for (PropertyDescriptor descriptor : sourceBeanInfo.getPropertyDescriptors()) {
                filed2Descriptor.put(descriptor.getName(), descriptor);
            }
            BeanInfo targetBeanInfo = Introspector.getBeanInfo(targetClazz);
            for (PropertyDescriptor descriptor : targetBeanInfo.getPropertyDescriptors()) {
                if ("class".equals(descriptor.getName())) {
                    continue;
                }
                PropertyDescriptor sourceDescriptor = filed2Descriptor.get(descriptor.getName());
                if (sourceDescriptor == null) {
                    continue;
                }
                Object value = sourceDescriptor.getReadMethod().invoke(source);
                descriptor.getWriteMethod().invoke(bean, value);
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public static Map<String, Object> transBean2Map(Object obj) {
        return transBean2Map(obj, false);
    }

    /**
     * 序列化<br>
     * 对象必须实现Serializable接口
     *
     * @param <T>
     * @param obj 要被序列化的对象
     * @return 序列化后的字节码
     */
    public static <T> byte[] serialize(T obj) {
        if (null == obj || false == (obj instanceof Serializable)) {
            return null;
        }

        FastByteArrayOutputStream byteOut = new FastByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(byteOut);
            oos.writeObject(obj);
            oos.flush();
        } catch (Exception e) {
            throw new UtilException(e);
        } finally {
            IoCommand.close(oos);
        }
        return byteOut.toByteArray();
    }

    /**
     * 反序列化<br>
     * 对象必须实现Serializable接口
     *
     * @param <T>
     * @param bytes 反序列化的字节码
     * @return 反序列化后的对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T unserialize(byte[] bytes) {
        ObjectInputStream ois = null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (Exception e) {
            throw new UtilException(e);
        }
    }

    /**
     * 是否为基本类型，包括包装类型和非包装类型
     *
     * @param object 被检查对象
     * @return 是否为基本类型
     * @see ClassCommand#isBasicType(Class)
     */
    public static boolean isBasicType(Object object) {
        return ClassCommand.isBasicType(object.getClass());
    }

    /**
     * 检查是否为有效的数字<br>
     * 检查Double和Float是否为无限大，或者Not a Number<br>
     * 非数字类型和Null将返回true
     *
     * @param obj 被检查类型
     * @return 检查结果，非数字类型和Null将返回true
     */
    public static boolean isValidIfNumber(Object obj) {
        if (obj != null && obj instanceof Number) {
            if (obj instanceof Double) {
                if (((Double) obj).isInfinite() || ((Double) obj).isNaN()) {
                    return false;
                }
            } else if (obj instanceof Float) {
                if (((Float) obj).isInfinite() || ((Float) obj).isNaN()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static interface Builder<T> {
        T build();
    }
}
