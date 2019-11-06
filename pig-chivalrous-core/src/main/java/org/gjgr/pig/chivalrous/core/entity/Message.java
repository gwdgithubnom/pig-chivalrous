package org.gjgr.pig.chivalrous.core.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.gjgr.pig.chivalrous.core.crypto.CryptoCommand;
import org.gjgr.pig.chivalrous.core.json.JsonCommand;
import org.gjgr.pig.chivalrous.core.lang.ClassCommand;
import org.gjgr.pig.chivalrous.core.lang.ObjectCommand;
import org.gjgr.pig.chivalrous.log.SystemLogger;

/**
 * Created by gwd on 2016/4/28.
 */
public class Message implements Serializable, Cloneable {

    protected String version;
    protected Integer code;
    protected Integer status;
    protected String type;
    protected String message;
    protected Object data;
    protected Object datum;
    protected Long timestamp;
    /**
     * description of message information user, owner, name, group and other information.
     */
    protected HashMap<String, Object> info = new HashMap();

    public static <T> T stringCast(String string, Class<T> clazz) {
        T object = null;
        try {
            object = JsonCommand.fromJson(string, clazz);
        } catch (Exception e) {
            try {
                object = deserialize(string, clazz);
            } catch (Exception eee) {
                SystemLogger.error("cast object failed in data about {}", string);
            }
        }
        return object;
    }

    public static <T> T stringCastObject(String string, Class<T> clazz) {
        T object = null;
        try {
            object = JsonCommand.fromJson(string, clazz);
        } catch (Exception e) {
            try {
                object = JsonCommand.fromJson(CryptoCommand.base64DecodeString(string), clazz);
            } catch (Exception ee) {
                try {
                    object = deserialize(string, clazz);
                } catch (Exception eee) {
                    SystemLogger.error("cast object failed in data about {}", string);
                }
            }
        }
        return object;
    }

    public static <T> T caseObject(String json, Class<T> clazz) {
        Message message = null;
        try {
            message = JsonCommand.fromJson(json, Message.class);
        } catch (Exception e) {
            message = JsonCommand.fromJson(CryptoCommand.base64DecodeString(json), Message.class);
        }
        T object = message.castObject(clazz);
        return object;
    }

    public static Object serialize(Object object) {
        if (object == null) {
            return null;
        } else if (ClassCommand.isPrimitiveOrWrapper(object.getClass()) || ClassCommand.isPrimitiveWrapper(object.getClass())) {
            return object;
        } else if (object instanceof String) {
            return object;
        } else if (object instanceof Map) {
            return JsonCommand.json(object);
        } else if (object instanceof List) {
            return JsonCommand.json(object);
        } else if (object instanceof JsonObject) {
            return JsonCommand.json(object);
        } else if (object instanceof JsonArray) {
            return JsonCommand.json(object);
        } else if (object instanceof JsonPrimitive) {
            return JsonCommand.json(object);
        } else {
            Map result = null;
            try {
                result = JsonCommand.to(JsonCommand.toJson(object), LinkedHashMap.class);
                if (result == null) {
                    SystemLogger.warn("unsuppport the type of the object:{} in message parse.", object);
                    return object;
                } else {
                    return JsonCommand.json(result);
                }
            } catch (Exception e) {
                SystemLogger.debug("not covert to json type. in object:{} in message parse.", object);
                return object;
            }
        }
    }

    public static <T> T deserialize(Object data, Class clazz) {
        if (data != null && !data.equals("")) {
            String stringData = null;
            if (data.getClass().isPrimitive() || data instanceof String) {
                stringData = data.toString();
            } else {
                stringData = serialize(data) + "";
            }
            T t = null;
            boolean status = false;
            try {
                t = JsonCommand.to(stringData, clazz);
                if (t == null) {
                    if (ClassCommand.isPrimitiveOrWrapper(clazz) && ClassCommand.isPrimitiveWrapperArray(clazz)) {
                        try {
                            t = (T) ObjectCommand.cast(clazz, data);
                        } catch (Exception e) {
                            status = true;
                        }
                    } else {
                        if (clazz.isAssignableFrom(String.class)) {
                            t = (T) stringData;
                        } else if (clazz.getComponentType().isAssignableFrom(String.class)) {
                            String[] strings = new String[1];
                            strings[0] = stringData;
                            t = (T) strings;
                        } else {
                            status = true;
                        }
                    }
                }
            } catch (Exception e) {
                try {
                    t = (T) ObjectCommand.cast(clazz, data);
                } catch (Exception ee) {
                    status = true;
                }
            }
            if (status) {
                if (clazz.isAssignableFrom(String.class)) {
                    return (T) data.toString();
                } else if (clazz.isAssignableFrom(Integer.class)) {
                    return (T) (new Integer(Integer.parseInt(data.toString())));
                } else if (clazz.isAssignableFrom(Double.class)) {
                    return (T) (new Double(Double.parseDouble(data.toString())));
                } else if (clazz.isAssignableFrom(Float.class)) {
                    return (T) (new Float(Float.parseFloat(data.toString())));
                } else if (clazz.isAssignableFrom(Boolean.class)) {
                    return (T) (new Boolean(Boolean.parseBoolean(data.toString())));
                } else if (clazz.isAssignableFrom(Long.class)) {
                    return (T) (new Long(Long.parseLong(data.toString())));
                } else if (clazz.isAssignableFrom(Byte.class)) {
                    return (T) (new Byte(Byte.parseByte(data.toString())));
                } else if (clazz.isAssignableFrom(Short.class)) {
                    return (T) (new Short(Short.parseShort(data.toString())));
                } else {
                    if (clazz.getComponentType() != null && clazz.getComponentType().isAssignableFrom(String.class)) {
                        String[] strings = new String[1];
                        strings[0] = data.toString();
                        return (T) strings;
                    } else if (ClassCommand.isPrimitiveWrapperArray(clazz) || ClassCommand.isPrimitiveArray(clazz)) {
                        Class<?> type = clazz.getComponentType();
                        if (type.isAssignableFrom(char.class)) {
                            return (T) data.toString().toCharArray();
                        } else if (type.isAssignableFrom(int.class)) {
                            Integer[] integers = new Integer[1];
                            integers[0] = (new Integer(Integer.parseInt(data.toString())));
                            return (T) integers;
                        } else if (type.isAssignableFrom(double.class)) {
                            Double[] doubles = new Double[1];
                            doubles[0] = (new Double(Double.parseDouble(data.toString())));
                            return (T) doubles;
                        } else if (type.isAssignableFrom(float.class)) {
                            Float[] floats = new Float[1];
                            floats[0] = (new Float(Float.parseFloat(data.toString())));
                            return (T) floats;
                        } else if (type.isAssignableFrom(boolean.class)) {
                            Boolean[] booleans = new Boolean[1];
                            booleans[0] = (new Boolean(Boolean.parseBoolean(data.toString())));
                            return (T) booleans;
                        } else if (clazz.isAssignableFrom(long.class)) {
                            Long[] longs = new Long[1];
                            longs[0] = (new Long(Long.parseLong(data.toString())));
                            return (T) longs;
                        } else if (clazz.isAssignableFrom(byte.class)) {
                            Byte[] bytes = new Byte[1];
                            bytes[0] = (new Byte(Byte.parseByte(data.toString())));
                            return (T) bytes;
                        } else if (clazz.isAssignableFrom(short.class)) {
                            Short[] shorts = new Short[1];
                            shorts[0] = (new Short(Short.parseShort(data.toString())));
                            return (T) shorts;
                        } else {
                            SystemLogger.error("could not parse the type {} about data {} for in message parse", clazz, data);
                            throw new RuntimeException("could not covert class " + clazz + " data:" + data);
                        }
                    } else {
                        SystemLogger.error("could not parse the type {} about data {} in message parse", clazz, data);
                        throw new RuntimeException("could not covert class " + clazz + " data:" + data);
                    }
                }
            } else {
                return t;
            }
        } else {
            return null;
        }
    }

    public <T> T castObject(Class<T> clazz) {
        T object = null;
        if (this.getDatum() != null) {
            try {
                object = JsonCommand.fromJson(this.getDatum().toString(), clazz);
            } catch (Exception e) {
                SystemLogger.error("cast object failed in datum about {}", this);
            }
        } else if (this.getData() != null) {
            object = stringCastObject(this.getData().toString(), clazz);
        }
        return object;
    }

    public <T> T serializeData() {
        return (T) serialize(this.data);
    }

    public <T> T serializeDatum() {
        return (T) serialize(this.datum);
    }

    public <T> T dataDeserialize(Class clazz) {
        return deserialize(this.data, clazz);
    }

    public <T> T datumDeserialize(Class clazz) {
        return deserialize(this.datum, clazz);
    }

    public <T> T deserializeData(Class clazz) {
        try {
            return deserialize(this.data, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T deserializeDatum(Class clazz) {
        try {
            return deserialize(this.datum, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public Message setVersion(String version) {
        this.version = version;
        return this;
    }

    public String key(String prefix) {
        return prefix + ":" + key();
    }

    public String key(String prefix, String suffix) {
        return prefix + ":" + key() + ":" + suffix;
    }

    public String key() {
        String key = null;
        if (getCode() != null) {
            if (this.getType() != null) {
                if (getVersion() != null) {
                    key = this.getCode() + ":" + this.getType() + ":" + this.getVersion();
                } else {
                    key = this.getCode() + ":" + this.getType();
                }
            } else {
                throw new RuntimeException("did not define the code or type could not generate key.");
            }
        } else {
            if (this.getType() != null) {
                if (getVersion() != null) {
                    key = this.getType() + ":" + this.getVersion();
                } else {
                    key = this.getType();
                }
            } else {
                throw new RuntimeException("did not define the code or type could not generate key.");
            }
        }
        return key;
    }

    public HashMap<String, Object> getInfo() {
        return this.info;
    }

    public Message setInfo(HashMap<String, Object> info) {
        this.info = info;
        return this;
    }

    public Integer getCode() {
        return this.code;
    }

    public Message setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public Message setType(String type) {
        this.type = type;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public Message setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getDatum() {
        return this.datum;
    }

    public Message setDatum(Object datum) {
        this.datum = datum;
        return this;
    }

    public Object getData() {
        return this.data;
    }

    public Message setData(Object data) {
        this.data = data;
        return this;
    }

    public JsonObject dataOrDefault() {
        if (this.data == null) {
            this.data = new JsonObject();
            return (JsonObject) this.data;
        } else if (this.data instanceof JsonObject) {
            return (JsonObject) this.data;
        } else {
            return null;
        }
    }

    public JsonObject outData() {
        if (this.data == null) {
            this.data = new JsonObject();
        } else if (this.data instanceof JsonObject) {
            return (JsonObject) this.data;
        }
        return null;
    }

    public String json() {
        String string = JsonCommand.json(this);
        JsonObject jsonObject = JsonCommand.jsonObject(string);
        if (jsonObject.get("data").isJsonNull()) {
            jsonObject.remove("data");
        }
        if (jsonObject.get("message").isJsonNull()) {
            jsonObject.remove("message");
        }
        if (info.size() == 0 || jsonObject.get("info").isJsonNull()) {
            jsonObject.remove("info");
        }
        if (jsonObject.get("datum").isJsonNull()) {
            jsonObject.remove("datum");
        }
        if (jsonObject.get("version").isJsonNull()) {
            jsonObject.remove("version");
        }
        if (jsonObject.get("code").isJsonNull()) {
            jsonObject.remove("code");
        }
        if (jsonObject.get("type").isJsonNull()) {
            jsonObject.remove("type");
        }
        if (jsonObject.get("timestamp").isJsonNull()) {
            jsonObject.remove("timestamp");
        }
        if (jsonObject.get("status").isJsonNull()) {
            jsonObject.remove("status");
        }
        return jsonObject.toString();
    }

    @Override
    public String toString() {
        String s = null;
        if (this.data != null) {
            s = this.data.toString();
        }
        String ss;
        if (this.datum != null && this.data != null) {
            ss = "{\"code\":" + this.code + "\",\"type\":" + this.type + "\",\"message\":" + this.message
                    + ",\"datum\":" + this.datum.toString() + ",\"data\":" + s + "}";
        } else if (this.data != null) {
            ss = "{\"code\":" + this.code + "\",\"type\":" + this.type + "\",\"message\":" + this.message + ",\"data\":"
                    + this.data.toString() + "}";
        } else if (this.data != null && this.datum != null) {
            ss = "{\"code\":" + this.code + "\",\"type\":" + this.type + "\",\"message\":" + this.message
                    + ",\"datum\":" + this.datum.toString() + "}";
        } else {
            ss = "{\"code\":" + this.code + "\",\"type\":" + this.type + "\",\"message\":" + this.message + "}";
        }

        return ss;
    }

    public String toString(String data, String info) {
        String s;
        if (data != null && info != null) {
            s = "{\"code\":" + this.code + "\",\"type\":" + this.type + "\",\"message\":" + this.message + ",\"datum\":"
                    + data + ",\"data\":" + info + "}";
        } else if (info != null) {
            s = "{\"code\":" + this.code + "\",\"type\":" + this.type + "\",\"message\":" + this.message + ",\"data\":"
                    + info + "}";
        } else {
            s = "{\"code\":" + this.code + "\",\"type\":" + this.type + "\",\"message\":" + this.message + ",\"datum\":"
                    + data + "}";
        }

        return s;
    }

}
