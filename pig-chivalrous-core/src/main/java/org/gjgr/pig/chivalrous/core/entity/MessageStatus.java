package org.gjgr.pig.chivalrous.core.entity;

import org.gjgr.pig.chivalrous.core.lang.Nullable;

/**
 * @Author gwd
 * @Time 01-04-2019 Friday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public enum MessageStatus {
    CONTINUE(100, "debug", "Continue"),
    SWITCHING_PROTOCOLS(101, "debug", "Switching Protocols"),
    PROCESSING(102, "debug", "Processing"),
    CHECKPOINT(103, "debug", "Checkpoint"),
    OK(200, "info", "OK"),
    CREATED(201, "info", "Created"),
    ACCEPTED(202, "info", "Accepted"),
    NON_AUTHORITATIVE_INFORMATION(203, "info", "Non-Authoritative Information"),
    NO_CONTENT(204, "info", "No Content"),
    RESET_CONTENT(205, "info", "Reset Content"),
    PARTIAL_CONTENT(206, "info", "Partial Content"),
    MULTI_STATUS(207, "info", "Multi-Status"),
    ALREADY_REPORTED(208, "info", "Already Reported"),
    IM_USED(226, "info", "IM Used"),
    MULTIPLE_CHOICES(300, "warn", "Multiple Choices"),
    MOVED_PERMANENTLY(301, "warn", "Moved Permanently"),
    FOUND(302, "warn", "Found"),
    /**
     * @deprecated
     */
    @Deprecated
    MOVED_TEMPORARILY(302, "warn", "Moved Temporarily"),
    SEE_OTHER(303, "warn", "See Other"),
    NOT_MODIFIED(304, "warn", "Not Modified"),
    /**
     * @deprecated
     */
    @Deprecated
    USE_PROXY(305, "warn", "Use Proxy"),
    TEMPORARY_REDIRECT(307, "warn", "Temporary Redirect"),
    PERMANENT_REDIRECT(308, "warn", "Permanent Redirect"),
    BAD_REQUEST(400, "error", "Bad Request"),
    UNAUTHORIZED(401, "error", "Unauthorized"),
    PAYMENT_REQUIRED(402, "error", "Payment Required"),
    FORBIDDEN(403, "error", "Forbidden"),
    NOT_FOUND(404, "error", "Not Found"),
    METHOD_NOT_ALLOWED(405, "error", "Method Not Allowed"),
    NOT_ACCEPTABLE(406, "error", "Not Acceptable"),
    PROXY_AUTHENTICATION_REQUIRED(407, "error", "Proxy Authentication Required"),
    REQUEST_TIMEOUT(408, "error", "Request Timeout"),
    CONFLICT(409, "error", "Conflict"),
    GONE(410, "error", "Gone"),
    LENGTH_REQUIRED(411, "error", "Length Required"),
    PRECONDITION_FAILED(412, "error", "Precondition Failed"),
    PAYLOAD_TOO_LARGE(413, "error", "Payload Too Large"),
    /**
     * @deprecated
     */
    @Deprecated
    REQUEST_ENTITY_TOO_LARGE(413, "error", "Request Entity Too Large"),
    URI_TOO_LONG(414, "error", "URI Too Long"),
    /**
     * @deprecated
     */
    @Deprecated
    REQUEST_URI_TOO_LONG(414, "error", "Request-URI Too Long"),
    UNSUPPORTED_MEDIA_TYPE(415, "error", "Unsupported Media Type"),
    REQUESTED_RANGE_NOT_SATISFIABLE(416, "error", "Requested range not satisfiable"),
    EXPECTATION_FAILED(417, "error", "Expectation Failed"),
    I_AM_A_TEAPOT(418, "error", "I'm a teapot"),
    /**
     * @deprecated
     */
    @Deprecated
    INSUFFICIENT_SPACE_ON_RESOURCE(419, "error", "Insufficient Space On Resource"),
    /**
     * @deprecated
     */
    @Deprecated
    METHOD_FAILURE(420, "error", "Method Failure"),
    /**
     * @deprecated
     */
    @Deprecated
    DESTINATION_LOCKED(421, "error", "Destination Locked"),
    UNPROCESSABLE_ENTITY(422, "error", "Unprocessable Entity"),
    LOCKED(423, "error", "Locked"),
    FAILED_DEPENDENCY(424, "error", "Failed Dependency"),
    UPGRADE_REQUIRED(426, "error", "Upgrade Required"),
    PRECONDITION_REQUIRED(428, "error", "Precondition Required"),
    TOO_MANY_REQUESTS(429, "error", "Too Many Requests"),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, "error", "Request Header Fields Too Large"),
    UNAVAILABLE_FOR_LEGAL_REASONS(451, "error", "Unavailable For Legal Reasons"),
    INTERNAL_SERVER_ERROR(500, "fatal", "Internal Server Error"),
    NOT_IMPLEMENTED(501, "fatal", "Not Implemented"),
    BAD_GATEWAY(502, "fatal", "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, "fatal", "Service Unavailable"),
    GATEWAY_TIMEOUT(504, "fatal", "Gateway Timeout"),
    HTTP_VERSION_NOT_SUPPORTED(505, "fatal", "HTTP Version not supported"),
    VARIANT_ALSO_NEGOTIATES(506, "fatal", "Variant Also Negotiates"),
    INSUFFICIENT_STORAGE(507, "fatal", "Insufficient Storage"),
    LOOP_DETECTED(508, "fatal", "Loop Detected"),
    BANDWIDTH_LIMIT_EXCEEDED(509, "fatal", "Bandwidth Limit Exceeded"),
    NOT_EXTENDED(510, "fatal", "Not Extended"),
    NETWORK_AUTHENTICATION_REQUIRED(511, "fatal", "Network Authentication Required"),
    Exception(900, "exception", "Exception"),
    UNKNOWN_STATUS_EXCEPTION(1024, "exception", "Unknown Exception"),
    MAIL(25, "email", "mail"),
    CP_ARTICLE(-1655481367, "article", "Cp article message"),
    ARTICLE(-732377866, "article", "Article message"),
    MICSQL(-1074356697, "micsql", "Micsql message"),
    SPIDER(-895953179, "spider", "Spider message"),
    CRAWLER(1025508116, "crawler", "Crawler message"),
    VIDEO(112202875, "video", "Video message");
    private final int value;
    private String reasonPhrase;
    private String type;

    private MessageStatus(int value, String type, String reasonPhrase) {
        this.value = value;
        this.type = type;
        this.reasonPhrase = reasonPhrase;
    }

    public static MessageStatus valueOf(int statusCode) {
        MessageStatus status = resolve(statusCode);
        if (status == null) {
            throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
        } else {
            return status;
        }
    }

    @Nullable
    public static MessageStatus resolve(int statusCode) {
        MessageStatus[] var1 = values();
        int var2 = var1.length;
        for (int var3 = 0; var3 < var2; ++var3) {
            MessageStatus status = var1[var3];
            if (status.value == statusCode) {
                return status;
            }
        }
        return null;
    }

    public static Message message(int code) {
        MessageStatus messageStatus = null;
        try {
            messageStatus = MessageStatus.valueOf(code);
        } catch (Exception e) {
            try {
                messageStatus = MessageStatus.resolve(code);
            } catch (Exception ee) {
                messageStatus = MessageStatus.UNKNOWN_STATUS_EXCEPTION;
                messageStatus.appendReasonPhrase("code " + code);
            }
        }
        return MessageBuilder.message(messageStatus);
    }

    public Message message() {
        return MessageBuilder.message(this);
    }

    public String type() {
        return this.type;
    }

    public MessageStatus changeType(String type) {
        this.type = type;
        return this;
    }

    public MessageStatus appendReasonPhrase(String phrase) {
        this.reasonPhrase = this.reasonPhrase + " " + phrase;
        return this;
    }

    public int value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public boolean is1xxInformational() {
        return MessageStatus.Series.INFORMATIONAL.equals(this.series());
    }

    public boolean is2xxSuccessful() {
        return MessageStatus.Series.SUCCESSFUL.equals(this.series());
    }

    public boolean is3xxRedirection() {
        return MessageStatus.Series.REDIRECTION.equals(this.series());
    }

    public boolean is4xxClientError() {
        return MessageStatus.Series.CLIENT_ERROR.equals(this.series());
    }

    public boolean is5xxServerError() {
        return MessageStatus.Series.SERVER_ERROR.equals(this.series());
    }

    public boolean isError() {
        return this.is4xxClientError() || this.is5xxServerError();
    }

    public MessageStatus.Series series() {
        return MessageStatus.Series.valueOf(this);
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }

    public static enum Series {
        INFORMATIONAL(1),
        SUCCESSFUL(2),
        REDIRECTION(3),
        CLIENT_ERROR(4),
        SERVER_ERROR(5);

        private final int value;

        private Series(int value) {
            this.value = value;
        }

        public static MessageStatus.Series valueOf(int status) {
            int seriesCode = status / 100;
            MessageStatus.Series[] var2 = values();
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                MessageStatus.Series series = var2[var4];
                if (series.value == seriesCode) {
                    return series;
                }
            }

            throw new IllegalArgumentException("No matching constant for [" + status + "]");
        }

        public static MessageStatus.Series valueOf(MessageStatus status) {
            return valueOf(status.value);
        }

        public int value() {
            return this.value;
        }
    }
}
