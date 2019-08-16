package org.gjgr.pig.chivalrous.core.exceptions;

/**
 * @Author gwd
 * @Time 12-12-2018 Wednesday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public class TypeConversionException extends RuntimeException {
    private static final long serialVersionUID = -6118520819865759886L;

    private final transient Object value;
    private final transient Class<?> type;

    public TypeConversionException(Object value, Class<?> type, Throwable cause) {
        super(createMessage(value, type, cause), cause);
        this.value = value;
        this.type = type;
    }

    /**
     * Returns an error message for type conversion failed.
     */
    public static String createMessage(Object value, Class<?> type, Throwable cause) {
        return "Error during type conversion from type: " + (value != null ? value.getClass().getCanonicalName() : null)
                + " to the required type: " + type.getCanonicalName() + " with value " + value + " due "
                + cause.getMessage();
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
