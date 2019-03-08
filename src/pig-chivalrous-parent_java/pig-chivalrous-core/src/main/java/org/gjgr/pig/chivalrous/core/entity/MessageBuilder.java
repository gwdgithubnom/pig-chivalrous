/*
 * Copyright (c) 2018 Xiaomi, Inc. All Rights Reserved.
 */

package org.gjgr.pig.chivalrous.core.entity;

import java.util.HashMap;

/**
 * @Author gwd
 * @Time 08-24-2018 Friday
 * @Description: global-browser-mipush:
 * @Target:
 * @More:
 */
public class MessageBuilder {

    public static Message build() {
        Message message = new Message();
        return message;
    }

    public static Message message(Message message, Object data, HashMap info, Object datum) {
        message.setData(data);
        if (info != null) {
            message.setInfo(info);
        }
        message.setDatum(datum);
        return message;
    }

    public static Message message(int code, String type, String str, Object data, Object datum, HashMap info) {
        Message message = new Message();
        message.setCode(code);
        message.setType(type);
        if (type == null || str == null) {
            MessageStatus httpStatus;
            try {
                httpStatus = MessageStatus.valueOf(code);
            } catch (Exception e) {
                httpStatus = MessageStatus.valueOf(500);
            }
            if (type == null) {
                message.setType("notify");
            }
            message.setMessage(httpStatus.getReasonPhrase());
        } else {
            message.setMessage(str);
        }
        return message(message, data, info, datum);
    }

    public static Message message(int code, String type, String message, Object data, HashMap info) {
        return message(code, type, message, data, null, info);
    }

    public static Message message(int code, String type, String message, Object data, Object datum) {
        return message(code, type, message, data, datum, null);
    }

    public static Message message(int code, String type, String message, Object data) {
        return message(code, type, message, data, null, null);
    }

    public static Message message(int code, Object data) {
        String type = "info";
        String message = "success";
        if (code >= 300) {
            type = "debug";
        }
        return message(code, type, message, data, null, null);
    }

    public static Message message(int code, String type, String message) {
        return message(code, type, message, null, null, null);
    }

    public static Message message(int code, String type) {
        return message(code, type, null, null, null, null);
    }

    public static Message message(int code) {
        return message(code, "notify", null, null, null, null);
    }

    public static Message info(int code) {
        return message(code, "info");
    }

    public static Message warn(int code) {
        return message(code, "warn");
    }

    public static Message error(int code) {
        return message(code, "error");
    }

    public static Message info(int code, String message) {
        return message(code, "info", message);
    }

    public static Message warn(int code, String message) {
        return message(code, "warn", message);
    }

    public static Message error(int code, String message) {
        return message(code, "error", message);
    }

    public static Message exception(int code, String message) {
        return message(code, "exception", message);
    }

}
