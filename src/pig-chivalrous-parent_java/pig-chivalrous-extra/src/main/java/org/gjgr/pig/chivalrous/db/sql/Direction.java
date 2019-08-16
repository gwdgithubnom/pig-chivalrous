package org.gjgr.pig.chivalrous.db.sql;

import org.gjgr.pig.chivalrous.core.lang.StringCommand;

/**
 * 排序方式（升序或者降序）
 *
 * @author Looly
 */
public enum Direction {
    /**
     * 升序
     */
    ASC,
    /**
     * 降序
     */
    DESC;

    /**
     * Returns the {@link Direction} enum for the given {@link String} value.
     *
     * @param value
     * @return {@link Direction}
     * @throws IllegalArgumentException in case the given value cannot be parsed into an enum value.
     */
    public static Direction fromString(String value) {

        try {
            return Direction.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException(StringCommand.format(
                    "Invalid value [{}] for orders given! Has to be either 'desc' or 'asc' (case insensitive).", value),
                    e);
        }
    }
}
