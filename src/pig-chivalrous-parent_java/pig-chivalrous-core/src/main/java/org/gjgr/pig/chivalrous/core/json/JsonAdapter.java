package org.gjgr.pig.chivalrous.core.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @Author gwd
 * @Time 08-17-2018 Friday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public class JsonAdapter implements JsonSerializer, JsonDeserializer {
    private String CLASSNAME = "CLASSNAME";
    private String DATA = "DATA";

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonPrimitive prim = (JsonPrimitive) json.getAsJsonObject().get(CLASSNAME);
        String className = prim.getAsString();
        Class klass = getObjectClass(className);
        return context.deserialize(json.getAsJsonObject().get(DATA), klass);
    }

    @Override
    public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
        com.google.gson.JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(CLASSNAME, src.getClass().getName());
        jsonObject.add(DATA, context.serialize(src));
        return jsonObject;
    }

    public Class getObjectClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            // e.printStackTrace();
            throw new JsonParseException(e.getMessage());
        }
    }
}
