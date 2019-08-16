/**
 * 文 件 名 :
 * CopyRright (c) 1949-xxxx:
 * 文件编号：
 * 创 建 人：龚文东
 * 日    期：Nov 19, 2015
 * 修 改 人：root
 * 日   期：
 * 修改备注：
 * 描   述：
 * 版 本 号：
 */

package org.gjgr.pig.chivalrous.core.json;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import org.gjgr.pig.chivalrous.core.io.exception.IORuntimeException;
import org.gjgr.pig.chivalrous.core.io.file.FileReader;
import org.gjgr.pig.chivalrous.core.io.file.yml.YmlNode;
import org.gjgr.pig.chivalrous.core.json.bean.Json;
import org.gjgr.pig.chivalrous.core.json.bean.JsonArray;
import org.gjgr.pig.chivalrous.core.json.bean.JsonNull;
import org.gjgr.pig.chivalrous.core.json.bean.JsonObject;
import org.gjgr.pig.chivalrous.core.json.bean.JsonString;
import org.gjgr.pig.chivalrous.core.lang.ArrayCommand;
import org.gjgr.pig.chivalrous.core.lang.ObjectCommand;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.gjgr.pig.chivalrous.core.xml.XmlBetweenJsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.type.TypeVariable;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * This class is used for ... ClassName: JSONHelper
 *
 * @author 龚文东 root
 * @version Nov 19, 2015 8:16:14 AM
 * @Description: TODO
 */
public class JsonCommand {

    private static Gson gson = null;
    private static GsonBuilder gsonBuilder = new GsonBuilder();
    private static Logger logger = LoggerFactory.getLogger(JsonCommand.class);

    static {
        gsonBuilder.serializeNulls();
    }

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    /**
     * Description : 带参数构造函数, 初始化模式名,名称和数据源类型
     *
     * @param schema ： 模式名
     * @param name ： 名称
     * @param type ： 数据源类型
     *            <p>
     *            Description:
     *            </p>
     */

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    public JsonCommand() {
        // TODO Auto-generated constructor stub
    }

    public static JsonElement parseJsonElement(String str) {
        return new JsonParser().parse(str);
    }

    public static <T> T parse(String str, TypeToken typeToken) {
        return new Gson().fromJson(str, typeToken.getType());
    }

    public static <T> T parse(String str, Type type) {
        if (type instanceof ParameterizedType) {
            // Collection Map<String, Object> map Class<?> Holder<String>
        } else if (type instanceof TypeVariable) {
            // T \ E
        } else if (type instanceof GenericArrayType) {
            // List<>[], T[] array
            return (T) Arrays.asList(gson.fromJson(str, type));
        } else if (type instanceof WildcardType) {
            // ?super T
        }
        return null;
    }

    public static JsonObject convert(JSONObject jsonObject) {
        return fromJson(jsonObject.toString(), JsonObject.class);
    }

    public static com.google.gson.JsonElement convert(JsonObject jsonObject) {
        JsonElement jsonElement = jsonObject.gson();
        return jsonElement;
    }

    /**
     * 对象转换成json字符串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        if (gson == null) {
            gson = new Gson();
        }
        return gson.toJson(obj);
    }

    public static String json(Object object) {
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }

    public static JsonElement jsonElement(Object object) {
        String string = json(object);
        JsonElement jsonElement = jsonElement(string);
        return jsonElement;
    }

    public static JsonElement jsonElement(String str) {
        return new JsonParser().parse(str);
    }

    public static com.google.gson.JsonArray jsonArray(Object object) {
        String string = json(object);
        com.google.gson.JsonArray jsonElement = jsonArray(string);
        return jsonElement;
    }

    public static com.google.gson.JsonArray jsonArray(String str) {
        JsonElement jsonElement = jsonElement(str);
        if (jsonElement.isJsonArray()) {
            return jsonElement.getAsJsonArray();
        } else {
            return new com.google.gson.JsonArray();
        }
    }

    public static com.google.gson.JsonObject jsonObject(Object object) {
        String string = json(object);
        com.google.gson.JsonObject jsonElement = jsonObject(string);
        return jsonElement;
    }

    public static com.google.gson.JsonObject jsonObject(String str) {
        JsonElement jsonElement = jsonElement(str);
        if (jsonElement.isJsonObject()) {
            return jsonElement.getAsJsonObject();
        } else {
            return new com.google.gson.JsonObject();
        }
    }

    /**
     * 将对象转换成json格式(并自定义日期格式)
     *
     * @param ts
     * @return
     */
    public static String serializer(Object ts,
                                    final String dateformat) {
        String jsonStr = null;
        gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Date.class,
                        new JsonSerializer<Date>() {
                            @Override
                            public JsonElement serialize(Date src,
                                                         Type typeOfSrc,
                                                         JsonSerializationContext context) {
                                SimpleDateFormat format = new SimpleDateFormat(
                                        dateformat);
                                return new JsonPrimitive(format.format(src));
                            }
                        })
                .setDateFormat(dateformat).create();
        if (gson != null) {
            jsonStr = gson.toJson(ts);
        }
        return jsonStr;
    }

    /**
     * 将json转换成bean对象
     *
     * @param jsonStr
     * @param cl
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T serializer(String jsonStr, Class<T> cl,
                                   final String pattern) {
        Object obj = null;
        gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    @Override
                    public Date deserialize(JsonElement json, Type typeOfT,
                                            JsonDeserializationContext context)
                            throws JsonParseException {
                        SimpleDateFormat format = new SimpleDateFormat(pattern);
                        String dateStr = json.getAsString();
                        try {
                            return format.parse(dateStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }).setDateFormat(pattern).create();
        if (gson != null) {
            obj = gson.fromJson(jsonStr, cl);
        }
        return (T) obj;
    }

    /**
     * json字符串转成对象
     *
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Class type) {
        if (gson == null) {
            gson = new Gson();
        }
        if (type.getName().equalsIgnoreCase(com.google.gson.JsonObject.class.getName())
                || type.getName().equalsIgnoreCase(com.google.gson.JsonArray.class.getName())
                || type.getName().equalsIgnoreCase(com.google.gson.JsonElement.class.getName())) {
            return (T) type.cast(fromJson(str));
        } else {
            return (T) gson.fromJson(str, type);
        }
    }

    public static <T> T fromObject(Object object, Class clazz) {
        String json = JsonCommand.toJson(object);
        return to(json, clazz);
    }

    public static <T> List<T> toArrayList(String json, Class<T> clazz) {
        Object[] array = (Object[]) java.lang.reflect.Array.newInstance(clazz, 0);
        array = gson.fromJson(json, array.getClass());
        List<T> list = new ArrayList<T>();
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                list.add(clazz.cast(array[i]));
            }
        }
        return list;
    }

    public static <T> List<T> toList(String json, Class<T> clazz) {
        // ArrayList<T> data= (ArrayList<T>) fromJson(json,
        // new TypeToken<ArrayList<T>>() {
        // }.getType());

        return toArrayList(json, clazz);
    }

    public static <T> T[] toArray(String json, Class<T> clazz) {
        Object[] array = (Object[]) java.lang.reflect.Array.newInstance(clazz, 0);
        array = gson.fromJson(json, array.getClass());
        return (T[]) array;
    }

    public static <T> List<T> toArrayList(JsonElement jsonElement, Class<T> clazz) {
        return toArrayList(toJson(jsonElement), clazz);
    }

    public static <T> T to(String str, Class clazz) {
        if (clazz.isArray()) {

        }
        return fromJson(str, clazz);
    }

    public static <T> T to(JsonElement jsonElement, Class clazz) {
        String string = toJson(jsonElement);
        return to(string, clazz);
    }

    public static JsonElement fromJson(String str) {
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(str);
    }

    /**
     * XML字符串转为JSONObject
     *
     * @param xmlStr XML字符串
     * @return JsonObject
     */
    public static com.google.gson.JsonObject fromXml(String xmlStr) {
        JsonElement jsonElement = XmlBetweenJsonObject.toJSONObject(xmlStr).gson();
        if (jsonElement.isJsonObject()) {
            return jsonElement.getAsJsonObject();
        }
        return new com.google.gson.JsonObject();
    }

    // -------------------------------------------------------------------- Pause start

    /**
     * Map转化为JSONObject
     *
     * @param map {@link Map}
     * @return JsonObject
     */
    public static com.google.gson.JsonObject fromMap(Map<?, ?> map) {
        JsonElement jsonElement = new JsonObject(map).gson();
        if (jsonElement.isJsonObject()) {
            return jsonElement.getAsJsonObject();
        }
        return new com.google.gson.JsonObject();
    }

    /**
     * ResourceBundle转化为JSONObject
     *
     * @param bundle ResourceBundle文件
     * @return JsonObject
     */
    public static JsonObject fromResourceBundle(ResourceBundle bundle) {
        JsonObject jsonObject = new JsonObject();
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (key != null) {
                InternalJsonUtil.propertyPut(jsonObject, key, bundle.getString(key));
            }
        }
        return jsonObject;
    }

    /**
     * 创建JSONObject
     *
     * @return JsonObject
     */
    private static JsonObject newJsonObject() {
        return new JsonObject();
    }

    /**
     * 创建 JsonArray
     *
     * @return JsonArray
     */
    private static JsonArray newJsonArray() {
        return new JsonArray();
    }

    /**
     * JSON字符串转JSONObject对象
     *
     * @param jsonStr JSON字符串
     * @return JsonObject
     */
    private static JsonObject newJsonObject(String jsonStr) {
        return new JsonObject(jsonStr);
    }

    /**
     * JSON字符串转JSONObject对象
     *
     * @param obj Bean对象或者Map
     * @return JsonObject
     */
    private static JsonObject newJsonObject(Object obj) {
        return new JsonObject(obj);
    }

    /**
     * JSON字符串转JSONArray
     *
     * @param jsonStr JSON字符串
     * @return JsonArray
     */
    public static JsonArray newJsonArray(String jsonStr) {
        return new JsonArray(jsonStr);
    }

    /**
     * 转换对象为JSON<br>
     * 支持的对象：<br>
     * String: 转换为相应的对象<br>
     * Array Collection：转换为JSONArray<br>
     * Bean对象：转为JSONObject
     *
     * @param obj 对象
     * @return Json
     */
    public static Json newJson(Object obj) {
        if (null == obj) {
            return null;
        }

        Json json = null;
        if (obj instanceof Json) {
            json = (Json) obj;
        } else if (obj instanceof String) {
            String jsonStr = ((String) obj).trim();
            if (jsonStr.startsWith("[")) {
                json = newJsonArray(jsonStr);
            } else {
                json = newJsonObject(jsonStr);
            }
        } else if (obj instanceof Collection || obj.getClass().isArray()) {
            // 列表
            json = new JsonArray(obj);
        } else {
            // 对象
            json = new JsonObject(obj);
        }

        return json;
    }

    /**
     * 读取JSON
     *
     * @param file    JSON文件
     * @param charset 编码
     * @return Json（包括JSONObject和JSONArray）
     * @throws IORuntimeException
     */
    public static Json readJSON(File file, Charset charset) throws IORuntimeException {
        return newJson(FileReader.create(file, charset).readString());
    }

    /**
     * 读取JSONObject
     *
     * @param file    JSON文件
     * @param charset 编码
     * @return JsonObject
     * @throws IORuntimeException
     */
    public static JsonObject readJSONObject(File file, Charset charset) throws IORuntimeException {
        return newJsonObject(FileReader.create(file, charset).readString());
    }

    /**
     * 读取JSONArray
     *
     * @param file    JSON文件
     * @param charset 编码
     * @return JsonArray
     * @throws IORuntimeException
     */
    public static JsonArray readJSONArray(File file, Charset charset) throws IORuntimeException {
        return newJsonArray(FileReader.create(file, charset).readString());
    }

    /**
     * 转为JSON字符串
     *
     * @param json         Json
     * @param indentFactor 每一级别的缩进
     * @return JSON字符串
     */
    public static String string(Json json, int indentFactor) {
        return json.toJSONString(indentFactor);
    }

    // -------------------------------------------------------------------- Pause end

    // -------------------------------------------------------------------- Read start

    /**
     * 转为JSON字符串
     *
     * @param json Json
     * @return JSON字符串
     */
    public static String string(Json json) {
        return json.toJSONString(0);
    }

    /**
     * 转为JSON字符串
     *
     * @param json Json
     * @return JSON字符串
     */
    public static String stringPretty(Json json) {
        return json.toJSONString(4);
    }

    /**
     * 转换为JSON字符串
     *
     * @param obj 被转为JSON的对象
     * @return JSON字符串
     */
    public static String string(Object obj) {
        String str = null;
        gson = new Gson();
        try {
            str = gson.toJson(obj);
        } catch (Exception e) {
            str = string(newJson(obj));
        }
        return str;
    }
    // -------------------------------------------------------------------- Read end

    // -------------------------------------------------------------------- toString start

    /**
     * 转换为格式化后的JSON字符串
     *
     * @param obj Bean对象
     * @return JSON字符串
     */
    public static String stringPretty(Object obj) {
        return stringPretty(newJson(obj));
    }

    /**
     * 转换为XML字符串
     *
     * @param json Json
     * @return XML字符串
     */
    public static String stringXml(Json json) {
        return XmlBetweenJsonObject.toString(json);
    }

    /**
     * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
     * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
     * JSON字符串中不能包含控制字符和未经转义的引号和反斜杠
     *
     * @param string A String
     * @return A String correctly formatted for insertion in a Json text.
     */
    public static String quote(String string) {
        StringWriter sw = new StringWriter();
        synchronized (sw.getBuffer()) {
            try {
                return quote(string, sw).toString();
            } catch (IOException ignored) {
                // will never happen - we are writing to a string writer
                return "";
            }
        }
    }

    /**
     * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
     * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
     * JSON字符串中不能包含控制字符和未经转义的引号和反斜杠
     *
     * @param string A String
     * @param writer Writer
     * @return A String correctly formatted for insertion in a Json text.
     * @throws IOException
     */
    public static Writer quote(String string, Writer writer) throws IOException {
        if (StringCommand.isEmpty(string)) {
            writer.write("\"\"");
            return writer;
        }

        char b; // back char
        char c = 0; // current char
        String hhhh;
        int i;
        int len = string.length();

        writer.write('"');
        for (i = 0; i < len; i++) {
            b = c;
            c = string.charAt(i);
            switch (c) {
                case '\\':
                case '"':
                    writer.write('\\');
                    writer.write(c);
                    break;
                case '/':
                    if (b == '<') {
                        writer.write('\\');
                    }
                    writer.write(c);
                    break;
                case '\b':
                    writer.write("\\b");
                    break;
                case '\t':
                    writer.write("\\t");
                    break;
                case '\n':
                    writer.write("\\n");
                    break;
                case '\f':
                    writer.write("\\f");
                    break;
                case '\r':
                    writer.write("\\r");
                    break;
                default:
                    if (c < ' ' || (c >= '\u0080' && c < '\u00a0') || (c >= '\u2000' && c < '\u2100')) {
                        writer.write("\\u");
                        hhhh = Integer.toHexString(c);
                        writer.write("0000", 0, 4 - hhhh.length());
                        writer.write(hhhh);
                    } else {
                        writer.write(c);
                    }
            }
        }
        writer.write('"');
        return writer;
    }

    /**
     * 在需要的时候包装对象<br>
     * 包装包括：
     * <ul>
     * <li><code>null</code> -> <code>JsonNull.NULL</code></li>
     * <li>array or collection -> JsonArray</li>
     * <li>beanMapWithHashSetValue -> JsonObject</li>
     * <li>standard property (Double, String, et al) -> 原对象</li>
     * <li>来自于java包 -> 字符串</li>
     * <li>其它 -> 尝试包装为JSONObject，否则返回<code>null</code></li>
     * </ul>
     *
     * @param object The object to wrap
     * @return The wrapped value
     */
    public static Object wrap(Object object) {
        try {
            if (object == null) {
                return JsonNull.NULL;
            }
            if (object instanceof Json
                    || JsonNull.NULL.equals(object)
                    || object instanceof JsonString
                    || object instanceof CharSequence
                    || object instanceof Number
                    || ObjectCommand.isBasicType(object)) {
                return object;
            }

            if (object instanceof Collection) {
                Collection<?> coll = (Collection<?>) object;
                return new JsonArray(coll);
            }
            if (ArrayCommand.isArray(object)) {
                return new JsonArray(object);
            }
            if (object instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) object;
                return new JsonObject(map);
            }
            Package objectPackage = object.getClass().getPackage();
            String objectPackageName = objectPackage != null ? objectPackage.getName() : "";
            if (objectPackageName.startsWith("java.") || objectPackageName.startsWith("javax.")
                    || object.getClass().getClassLoader() == null) {
                return object.toString();
            }
            return new JsonObject(object);
        } catch (Exception exception) {
            return null;
        }
    }

    public static com.google.gson.JsonPrimitive replace(Map map, JsonPrimitive jsonPrimitive) {
        JsonPrimitive data = null;
        if (jsonPrimitive.isString()) {
            String key = jsonPrimitive.getAsString();
            if (map.containsKey(key)) {
                data = new JsonPrimitive(map.get(jsonPrimitive.getAsString()).toString());
            }
        } else {
            String key = jsonPrimitive.getAsString();
            if (map.containsKey(key)) {
                data = new JsonPrimitive(map.get(jsonPrimitive.toString()).toString());
            }
        }
        return data;
    }

    public static com.google.gson.JsonObject replace(YmlNode ymlNode, JsonElement jsonElement, String to,
                                                     com.google.gson.JsonObject data) {
        try {
            Map map = ymlNode.map();
            if (jsonElement.isJsonPrimitive()) {
                JsonPrimitive temp = jsonElement.getAsJsonPrimitive();
                data.add(to, replace(map, temp));
            } else if (jsonElement.isJsonArray()) {
                com.google.gson.JsonArray temp = jsonElement.getAsJsonArray();
                com.google.gson.JsonArray newTemp = new com.google.gson.JsonArray();
                temp.forEach(element -> {
                    if (element.isJsonPrimitive()) {
                        JsonPrimitive cache = element.getAsJsonPrimitive();
                        JsonPrimitive newCache = replace(map, cache);
                        if (newCache != null) {
                            newTemp.add(newCache);
                        } else {
                            newTemp.add(cache);
                        }
                    }
                });
                data.add(to, newTemp);
            } else if (jsonElement == null || jsonElement.isJsonNull()) {
                logger.debug("skip null field about element", data);
            } else {
                logger.error("could not hand replace operation. about:{}", jsonElement);
            }

        } catch (Exception e) {
            String string = ymlNode.string();
            if (string != null) {
                data.addProperty(to, string);
            }
        }

        return data;
    }

    public static com.google.gson.JsonArray arrayFormat(YmlNode ymlNode, com.google.gson.JsonObject jsonObject) {
        com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
        if (ymlNode.get("source").list() != null) {
            List list = ymlNode.get("source").list();
            list.forEach(s -> {
                try {
                    com.google.gson.JsonArray array = (com.google.gson.JsonArray) jsonObject.get(s.toString());
                    array.forEach(element -> {
                        try {
                            com.google.gson.JsonObject data = format(ymlNode, (com.google.gson.JsonObject) element);
                            jsonArray.add(data);
                        } catch (Exception e) {
                            logger.error("should be a jsonObject in {} JsonArray", s, e);
                        }
                    });
                } catch (Exception e) {
                    logger.error("must be JsonArray format.", e);
                }
            });
            return jsonArray;
        } else {
            logger.error("should input a array refer about jsonobject");
            return null;
        }
    }

    public static com.google.gson.JsonObject format(YmlNode ymlNode, com.google.gson.JsonObject jsonObject) {
        com.google.gson.JsonObject messageData = new com.google.gson.JsonObject();
        com.google.gson.JsonObject source;
        Map scheme = ymlNode.get("data").map();
        Map map = ymlNode.get("source").map();
        if (map != null && map.size() > 0) {
            source = new com.google.gson.JsonObject();
            map.forEach((key, value) -> {
                if (key.equals(value)) {
                    com.google.gson.JsonElement tempSource = jsonObject.get(key.toString());
                    if (tempSource.isJsonObject()) {
                        com.google.gson.JsonObject referSource = (com.google.gson.JsonObject) tempSource;
                        referSource.entrySet().forEach((json) -> {
                            source.add(json.getKey(), json.getValue());
                        });
                    }
                } else {
                    source.add(value.toString(), jsonObject.get(key.toString()));
                }
            });
        } else {
            source = jsonObject;
        }
        scheme.forEach((key, value) -> {
            if (value instanceof String) {
                Object oo = getEntity(source, value.toString());
                if (oo != null) {
                    messageData.addProperty(key.toString(), oo.toString());
                } else {
                    messageData.add(key.toString(), com.google.gson.JsonNull.INSTANCE);
                }
            } else if (value instanceof List) {
                com.google.gson.JsonArray array = (com.google.gson.JsonArray) getEntity(source, value);
                if (array != null && array.size() > 0) {
                    messageData.add(key.toString(), array);
                } else {
                    messageData.add(key.toString(), com.google.gson.JsonNull.INSTANCE);
                }
            } else if (value instanceof Map) {
                com.google.gson.JsonObject object = (com.google.gson.JsonObject) getEntity(source, value);
                if (object != null && object.size() > 0) {
                    messageData.add(key.toString(), object);
                } else {
                    messageData.add(key.toString(), com.google.gson.JsonNull.INSTANCE);
                }
            }
        });
        map = ymlNode.get("mapData").map();
        if (map != null && map.size() > 0) {
            List mapScheme = ymlNode.get("mapScheme").list();
            mapScheme.forEach(s -> {
                JsonElement jsonElement = messageData.get(s.toString());
                YmlNode newValue = ymlNode.get("mapData").get(s.toString());
                replace(newValue, jsonElement, s.toString(), messageData);
            });
        }
        List listScheme = ymlNode.get("set").list();
        if (listScheme != null && listScheme.size() > 0) {
            listScheme.forEach(l -> {
                JsonElement jsonElement = messageData.get(l.toString());
                if (jsonElement != null) {
                    HashSet<JsonPrimitive> data = new HashSet<>();
                    if (jsonElement.isJsonArray()) {
                        jsonElement.getAsJsonArray().forEach(e -> {
                            if (e.isJsonPrimitive()) {
                                data.add(e.getAsJsonPrimitive());
                            }
                        });
                        if (data.size() > 0) {
                            try {
                                messageData.remove(l.toString());
                                com.google.gson.JsonArray covert = new com.google.gson.JsonArray();
                                data.forEach(value -> {
                                    covert.add(value);
                                });
                                messageData.add(l.toString(), covert);
                            } catch (Exception e) {
                                messageData.add(l.toString(), jsonElement);
                                logger.debug("could not set the field. reset the value to default.");
                            }

                        }
                    }
                }
            });
        }
        return messageData;
    }

    private static JsonElement getJsonEntity(com.google.gson.JsonObject data, String key) {
        JsonElement oo = null;
        if (data.has(key)) {
            if (data.get(key).isJsonPrimitive()) {
                oo = data.get(key).getAsJsonPrimitive();
            } else if (data.get(key).isJsonArray()) {
                oo = data.get(key).getAsJsonArray();
            } else if (data.get(key).isJsonObject()) {
                oo = data.get(key).getAsJsonObject();
            } else {
                oo = null;
            }
        } else {
            oo = com.google.gson.JsonNull.INSTANCE;
        }
        return oo;
    }

    private static Object getEntity(com.google.gson.JsonObject data, Object key) {
        Object oo;
        if (key instanceof String) {
            if (data.has(key.toString())) {
                if (data.get(key.toString()).isJsonPrimitive()) {
                    JsonPrimitive jsonPrimitive = data.get(key.toString()).getAsJsonPrimitive();
                    if (jsonPrimitive.isString()) {
                        oo = jsonPrimitive.getAsString();
                    } else if (jsonPrimitive.isBoolean()) {
                        oo = jsonPrimitive.getAsBoolean();
                    } else if (jsonPrimitive.isNumber()) {
                        oo = jsonPrimitive.getAsNumber();
                    } else {
                        oo = jsonPrimitive.getAsString();
                    }
                } else if (data.get(key.toString()).isJsonArray()) {
                    com.google.gson.JsonArray jsonElements = data.get(key.toString()).getAsJsonArray();
                    StringBuilder stringBuilder = new StringBuilder();
                    jsonElements.forEach(element -> {
                        stringBuilder.append(element.toString());
                    });
                    oo = stringBuilder.toString();
                } else if (data.get(key.toString()).isJsonObject()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    com.google.gson.JsonObject jsonObject = data.get(key.toString()).getAsJsonObject();
                    jsonObject.entrySet().forEach(entry -> {
                        stringBuilder.append(getEntity(data, entry.getKey()));
                    });
                    oo = stringBuilder.toString();
                } else {
                    oo = null;
                }
            } else {
                oo = null;
            }
        } else if (key instanceof List) {
            com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
            List list = (List) key;
            list.forEach(l -> {
                if (l instanceof String) {
                    JsonElement ooo = getJsonEntity(data, l.toString());
                    if (ooo instanceof com.google.gson.JsonArray) {
                        jsonArray.addAll((com.google.gson.JsonArray) ooo);
                    } else {
                        jsonArray.add(ooo);
                    }

                } else {
                    Object ooo = getEntity(data, l);
                    if (ooo != null) {
                        if (ooo instanceof JsonElement) {
                            jsonArray.add((JsonElement) ooo);
                        } else {
                            jsonArray.add(ooo.toString());
                        }
                    }
                }
            });
            oo = jsonArray;
        } else if (key instanceof Map) {
            com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
            Map map = (Map) key;
            map.forEach((k, v) -> {
                if (v instanceof String) {
                    JsonElement ooo = getJsonEntity(data, v.toString());
                    jsonObject.add(k.toString(), ooo);
                } else {
                    Object ooo = getEntity(data, v);
                    jsonObject.addProperty(k.toString(), ooo.toString());
                }
            });
            oo = jsonObject;
        } else {
            logger.warn("did not support this type:{} for yml config in the map config file.", key);
            return null;
        }
        return oo;
    }

    public static com.google.gson.JsonArray formatToArray(YmlNode ymlNode, com.google.gson.JsonObject jsonObject) {
        com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
        String index = ymlNode.get("index").string();
        if (index == null) {
            logger.error("config error could not covert the data to array.");
        } else {
            try {
                int size = jsonObject.get(index).getAsJsonArray().size();
                for (int i = 0; i < size; i++) {
                    com.google.gson.JsonObject covert = new com.google.gson.JsonObject();
                    List scheme = ymlNode.get("scheme").list();
                    int[] j = new int[1];
                    j[0] = i;
                    scheme.forEach(s -> {
                        if (jsonObject.has(s.toString()) && jsonObject.get(s.toString()) != null) {
                            if (jsonObject.get(s.toString()).isJsonArray()) {
                                covert.add(s.toString(), jsonObject.get(s.toString()).getAsJsonArray().get(j[0]));
                            } else if (jsonObject.get(s.toString()).isJsonPrimitive()) {
                                covert.add(s.toString(), jsonObject.get(s.toString()).getAsJsonPrimitive());
                            } else if (jsonObject.get(s.toString()).isJsonObject()) {
                                covert.add(s.toString(), jsonObject.get(s.toString()).getAsJsonObject());
                            } else {
                                covert.add(s.toString(), com.google.gson.JsonNull.INSTANCE);
                            }
                        } else {
                            covert.add(s.toString(), null);
                        }
                    });
                    com.google.gson.JsonObject result = format(ymlNode, covert);
                    jsonArray.add(result);
                }
            } catch (Exception e) {
                logger.error("type not correct {}. cause by {}", jsonObject, e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    public static com.google.gson.JsonObject merge(com.google.gson.JsonObject left, com.google.gson.JsonObject right) {
        Set<Map.Entry<String, JsonElement>> ss = right.entrySet();
        for (Map.Entry<String, JsonElement> sss : ss) {
            if (left.has(sss.getKey())) {
                throw new UnsupportedOperationException(
                        "left jsonObject and right jsonObject has conflict key:" + sss.getKey());
            }
            left.add(sss.getKey(), sss.getValue());
        }
        return left;
    }

    public static com.google.gson.JsonObject mergeInLeft(com.google.gson.JsonObject left,
                                                         com.google.gson.JsonObject right) {
        Set<Map.Entry<String, JsonElement>> ss = right.entrySet();
        for (Map.Entry<String, JsonElement> sss : ss) {
            if (left.has(sss.getKey())) {
                continue;
            }
            left.add(sss.getKey(), sss.getValue());
        }
        return left;
    }

    public static com.google.gson.JsonObject mergeInRight(com.google.gson.JsonObject left,
                                                          com.google.gson.JsonObject right) {
        return mergeInLeft(right, left);
    }

}
