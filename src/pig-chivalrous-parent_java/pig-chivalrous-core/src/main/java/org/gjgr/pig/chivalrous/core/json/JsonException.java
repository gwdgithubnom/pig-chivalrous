package org.gjgr.pig.chivalrous.core.json;

import org.gjgr.pig.chivalrous.core.util.StrUtil;

/**
 * JSON异常
 *
 * @author looly
 * @since 3.0.2
 */
public class JsonException extends RuntimeException {
    private static final long serialVersionUID = 0;

    public JsonException(final String message) {
        super(message);
    }

    public JsonException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public JsonException(final Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public JsonException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }
}
