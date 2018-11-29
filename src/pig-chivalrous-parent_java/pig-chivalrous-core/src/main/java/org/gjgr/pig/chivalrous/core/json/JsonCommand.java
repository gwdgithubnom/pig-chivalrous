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
import org.gjgr.pig.chivalrous.core.io.IORuntimeException;
import org.gjgr.pig.chivalrous.core.io.file.FileReader;
import org.gjgr.pig.chivalrous.core.json.bean.Json;
import org.gjgr.pig.chivalrous.core.json.bean.JsonArray;
import org.gjgr.pig.chivalrous.core.json.bean.JsonNull;
import org.gjgr.pig.chivalrous.core.json.bean.JsonObject;
import org.gjgr.pig.chivalrous.core.json.bean.JsonString;
import org.gjgr.pig.chivalrous.core.util.ArrayUtil;
import org.gjgr.pig.chivalrous.core.util.ObjectUtil;
import org.gjgr.pig.chivalrous.core.util.StrUtil;
import org.gjgr.pig.chivalrous.core.xml.XmlBetweenJsonObject;

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
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javax.lang.model.type.TypeVariable;

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
     * @param schema
     *            ： 模式名
     * @param name
     *            ： 名称
     * @param type
     *            ： 数据源类型
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

    public static <T> T parse(String str, Type type) {
        if (type instanceof ParameterizedType) {
            //Collection  Map<String, Object> map Class<?>  Holder<String>
        } else if (type instanceof TypeVariable) {
            // T \ E
        } else if (type instanceof GenericArrayType) {
            // List<>[], T[] array
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
        gson = new Gson();
        return gson.toJson(obj);
    }

    public static String json(Object object) {
        gson = gsonBuilder.create();
        return gson.toJson(object);
    }

    public static JsonElement jsonElement(String str) {
        return new JsonParser().parse(str);
    }

    public static com.google.gson.JsonArray jsonArray(String str) {
        JsonElement jsonElement = jsonElement(str);
        if (jsonElement.isJsonArray()) {
            return jsonElement.getAsJsonArray();
        } else {
            return new com.google.gson.JsonArray();
        }
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
                        }).setDateFormat(dateformat).create();
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

    //-------------------------------------------------------------------- bean start

    /**
     * 转为实体类对象，转换异常将被抛出
     *
     * @param json JsonObject
     * @param beanClass 实体类对象
     * @return 实体类对象
     */
    public static <T> T bean(JsonObject json, Class<T> beanClass) {
        return bean(json, beanClass, false);
    }

    /**
     * 将json转换成bean对象
     *
     * @param jsonStr
     * @return
     */
    public static Object bean(String jsonStr, Class<?> cl) {
        Object obj = null;
        if (gson != null) {
            obj = gson.fromJson(jsonStr, cl);
        }
        return obj;
    }

    /**
     * 转为实体类对象
     *
     * @param json JsonObject
     * @param beanClass 实体类对象
     * @return 实体类对象
     */
    public static <T> T bean(JsonObject json, Class<T> beanClass, boolean ignoreError) {
        return null == json ? null : json.toBean(beanClass, ignoreError);
    }
    //-------------------------------------------------------------------- bean end

    /**
     * 将json格式转换成list对象
     *
     * @param jsonStr
     * @return
     */
    public static List<?> beanList(String jsonStr) {
        List<?> objList = null;
        if (gson != null) {
            Type type = new TypeToken<List<?>>() {
            }.getType();
            objList = gson.fromJson(jsonStr, type);
        }
        return objList;
    }

    public static Set<?> beanSet(String jsonStr) {
        Set<?> objSet = null;
        if (gson != null) {
            Type type = new TypeToken<Set<?>>() {
            }.getType();
            objSet = gson.fromJson(jsonStr, type);
        }
        return objSet;
    }

    /**
     * 将json格式转换成list对象，并准确指定类型
     *
     * @param jsonStr
     * @param type
     * @return
     */
    public static List<?> beanList(String jsonStr, Type type) {
        List<?> objList = null;
        if (gson != null) {
            objList = gson.fromJson(jsonStr, type);
        }
        return objList;
    }

    /**
     * 将json格式转换成Set对象，并准确指定类型
     *
     * @param jsonStr
     * @param type
     * @return
     */
    public static Set<?> beanSet(String jsonStr, Type type) {
        Set<?> objSet = null;
        if (gson != null) {
            objSet = gson.fromJson(jsonStr, type);
        }
        return objSet;
    }

    /**
     * 将json格式转换成map对象
     *
     * @param jsonStr
     * @return
     */
    public static Map<?, ?> beanMap(String jsonStr) {
        Map<?, ?> objMap = null;
        if (gson != null) {
            Type type = new TypeToken<Map<?, ?>>() {
            }.getType();
            objMap = gson.fromJson(jsonStr, type);
        }
        return objMap;
    }

    /**
     * 函 数 名 : to reverse the hbase data to object
     * 功能描述：
     * 输入参数:   @param str
     * 输入参数:   @return
     * 返 回 值:  - 类型 <说明>  Map<String,HashSet<String>>
     * 异    常：<按照异常名字的字母顺序>
     * 创 建 人:龚文东
     * 日    期:Nov 22, 2015
     * 修 改 人:root
     * 日    期:
     *
     * @throws:@param str
     * @throws:@return
     */
    public static Map<String, HashSet<String>> beanMapWithHashSetValue(String str) {
        gson = new Gson();
        Map<String, HashSet<String>> map = new HashMap<String, HashSet<String>>();
        map = (Map<String, HashSet<String>>) gson.fromJson(str, map.getClass());
        return map;
    }

    //-------------------------------------------------------------------- Pause start

    /**
     * json字符串转成对象
     *
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Class<T> type) {
        gson = new Gson();
        if (type.getName().equalsIgnoreCase(com.google.gson.JsonObject.class.getName()) || type.getName().equalsIgnoreCase(com.google.gson.JsonArray.class.getName()) || type.getName().equalsIgnoreCase(com.google.gson.JsonElement.class.getName())) {
            return type.cast(fromJson(str));
        } else {
            return gson.fromJson(str, type);
        }
    }

    /**
     * json字符串转成对象
     *
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Type type) {
        gson = new Gson();
        return gson.fromJson(str, type);
    }

    public static JsonElement fromJson(String str) {
        return new JsonParser().parse(str);
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
     * 根据
     *
     * @param jsonStr
     * @param key
     * @return
     */
    public static Object fromStringValue(String jsonStr, String key) {
        Object rulsObj = null;
        Map<?, ?> rulsMap = beanMap(jsonStr);
        if (rulsMap != null && rulsMap.size() > 0) {
            rulsObj = rulsMap.get(key);
        }
        return rulsObj;
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
        } else if (obj instanceof Collection || obj.getClass().isArray()) {//列表
            json = new JsonArray(obj);
        } else {//对象
            json = new JsonObject(obj);
        }

        return json;
    }

    //-------------------------------------------------------------------- Pause end

    //-------------------------------------------------------------------- Read start

    /**
     * 读取JSON
     *
     * @param file JSON文件
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
     * @param file JSON文件
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
     * @param file JSON文件
     * @param charset 编码
     * @return JsonArray
     * @throws IORuntimeException
     */
    public static JsonArray readJSONArray(File file, Charset charset) throws IORuntimeException {
        return newJsonArray(FileReader.create(file, charset).readString());
    }
    //-------------------------------------------------------------------- Read end

    //-------------------------------------------------------------------- toString start

    /**
     * 转为JSON字符串
     *
     * @param json Json
     * @param indentFactor 每一级别的缩进
     * @return JSON字符串
     */
    public static String string(Json json, int indentFactor) {
        return json.toJSONString(indentFactor);
    }

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
    //-------------------------------------------------------------------- toString end

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
        if (StrUtil.isEmpty(string)) {
            writer.write("\"\"");
            return writer;
        }

        char b;        //back char
        char c = 0; //current char
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
                    || ObjectUtil.isBasicType(object)) {
                return object;
            }

            if (object instanceof Collection) {
                Collection<?> coll = (Collection<?>) object;
                return new JsonArray(coll);
            }
            if (ArrayUtil.isArray(object)) {
                return new JsonArray(object);
            }
            if (object instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) object;
                return new JsonObject(map);
            }
            Package objectPackage = object.getClass().getPackage();
            String objectPackageName = objectPackage != null ? objectPackage.getName() : "";
            if (objectPackageName.startsWith("java.") || objectPackageName.startsWith("javax.") || object.getClass().getClassLoader() == null) {
                return object.toString();
            }
            return new JsonObject(object);
        } catch (Exception exception) {
            return null;
        }
    }
}
