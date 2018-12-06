package org.gjgr.pig.chivalrous.core.entity;

import com.google.gson.JsonObject;
import org.gjgr.pig.chivalrous.core.json.JsonCommand;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by gwd on 2016/4/28.
 */
public class Message implements Serializable {

    private Integer code;
    private String version;
    private String type;
    private String message;
    private Object data;
    private Object datum;
    private HashMap<String, Object> info = new HashMap();

    public Message() {
    }

    public String getVersion() {
        return version;
    }

    public Message setVersion(String version) {
        this.version = version;
        return this;
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

    public JsonObject getDataOrDefault() {
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
        if (info.size()==0||jsonObject.get("info").isJsonNull()) {
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
        return jsonObject.toString();
    }

    @Override
    public String toString() {
        String s = this.getData().toString();
        String ss;
        if (this.datum != null && this.data != null) {
            ss = "{\"code\":" + this.code + "\",\"type\":" + this.type + "\",\"message\":" + this.message + ",\"datum\":" + this.datum.toString() + ",\"data\":" + s + "}";
        } else if (this.datum != null) {
            ss = "{\"code\":" + this.code + "\",\"type\":" + this.type + "\",\"message\":" + this.message + ",\"data\":" + this.data.toString() + "}";
        } else if (this.data != null) {
            ss = "{\"code\":" + this.code + "\",\"type\":" + this.type + "\",\"message\":" + this.message + ",\"datum\":" + this.datum.toString() + "}";
        } else {
            ss = "{\"code\":" + this.code + "\",\"type\":" + this.type + "\",\"message\":" + this.message + "}";
        }

        return ss;
    }

    public String toString(String data, String info) {
        String s;
        if (data != null && info != null) {
            s = "{\"code\":" + this.code + "\",\"type\":" + this.type + "\",\"message\":" + this.message + ",\"datum\":" + data + ",\"data\":" + info + "}";
        } else if (info != null) {
            s = "{\"code\":" + this.code + "\",\"type\":" + this.type + "\",\"message\":" + this.message + ",\"data\":" + info + "}";
        } else {
            s = "{\"code\":" + this.code + "\",\"type\":" + this.type + "\",\"message\":" + this.message + ",\"datum\":" + data + "}";
        }

        return s;
    }

}
