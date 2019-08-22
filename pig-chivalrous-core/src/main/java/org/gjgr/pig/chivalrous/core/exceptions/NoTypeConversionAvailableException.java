package org.gjgr.pig.chivalrous.core.exceptions;

/**
 * @Author gwd
 * @Time 12-12-2018 Wednesday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public class NoTypeConversionAvailableException extends Exception {
    private static final long serialVersionUID = -8721487434390572636L;

    private final transient Object value;
    private final transient Class<?> type;

    public NoTypeConversionAvailableException(Object value, Class<?> type) {
        super(createMessage(value, type));
        this.value = value;
        this.type = type;
    }

    public NoTypeConversionAvailableException(Object value, Class<?> type, Throwable cause) {
        super(createMessage(value, type, cause), cause);
        this.value = value;
        this.type = type;
    }

    /**
     * Returns an error message for no type converter available.
     */
    public static String createMessage(Object value, Class<?> type) {
        return "No type converter available to convert from type: "
                + (value != null ? value.getClass().getCanonicalName() : null)
                + " to the required type: " + type.getCanonicalName() + " with value " + value;
    }

    /**
     * Returns an error message for no type converter available with the cause.
     */
    public static String createMessage(Object value, Class<?> type, Throwable cause) {
        return "Converting Exception when converting from type: "
                + (value != null ? value.getClass().getCanonicalName() : null) + " to the required type: "
                + type.getCanonicalName() + " with value " + value + ", which is caused by " + cause;
    }

    /**
     * Returns the value which could not be converted
     */
    public Object getValue() {
        return value;
    }

    /**
     * Returns the required <tt>to</tt> type
     */
    public Class<?> getToType() {
        return type;
    }

    /**
     * Returns the required <tt>from</tt> type. Returns <tt>null</tt> if the provided value was null.
     */
    public Class<?> getFromType() {
        if (value != null) {
            return value.getClass();
        } else {
            return null;
        }
    }
}
