package org.gjgr.pig.chivalrous.core.json;

import org.gjgr.pig.chivalrous.core.convert.Convert;
import org.gjgr.pig.chivalrous.core.convert.ConvertException;
import org.gjgr.pig.chivalrous.core.convert.ConverterRegistry;
import org.gjgr.pig.chivalrous.core.json.bean.Json;
import org.gjgr.pig.chivalrous.core.json.bean.ListJson;
import org.gjgr.pig.chivalrous.core.json.bean.NullJson;
import org.gjgr.pig.chivalrous.core.json.bean.MapJson;
import org.gjgr.pig.chivalrous.core.json.bean.StringJson;
import org.gjgr.pig.chivalrous.core.lang.BeanUtil;
import org.gjgr.pig.chivalrous.core.lang.ObjectCommand;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.gjgr.pig.chivalrous.core.math.NumberCommand;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

/**
 * 内部JSON工具类，仅用于JSON内部使用
 *
 * @author Looly
 */
public final class InternalJsonUtil {

    private InternalJsonUtil() {
    }

    /**
     * 写入值到Writer
     *
     * @param writer       Writer
     * @param value        值
     * @param indentFactor
     * @param indent       缩进空格数
     * @return Writer
     * @throws JsonException
     * @throws IOException
     */
    public static final Writer writeValue(Writer writer, Object value, int indentFactor, int indent)
            throws JsonException, IOException {
        if (value == null || value.equals(null)) {
            writer.write("null");
        } else if (value instanceof Json) {
            ((Json) value).write(writer, indentFactor, indent);
        } else if (value instanceof Map) {
            new MapJson((Map<?, ?>) value).write(writer, indentFactor, indent);
        } else if (value instanceof Collection) {
            new ListJson((Collection<?>) value).write(writer, indentFactor, indent);
        } else if (value.getClass().isArray()) {
            new ListJson(value).write(writer, indentFactor, indent);
        } else if (value instanceof Number) {
            writer.write(NumberCommand.toStr((Number) value));
        } else if (value instanceof Boolean) {
            writer.write(value.toString());
        } else if (value instanceof StringJson) {
            Object o;
            try {
                o = ((StringJson) value).toJSONString();
            } catch (Exception e) {
                throw new JsonException(e);
            }
            writer.write(o != null ? o.toString() : JsonCommand.quote(value.toString()));
        } else {
            JsonCommand.quote(value.toString(), writer);
        }
        return writer;
    }

    /**
     * 缩进，使用空格符
     *
     * @param writer
     * @param indent
     * @throws IOException
     */
    public static final void indent(Writer writer, int indent) throws IOException {
        for (int i = 0; i < indent; i += 1) {
            writer.write(' ');
        }
    }

    /**
     * 如果对象是Number 且是 NaN or infinite，将抛出异常
     *
     * @param obj 被检查的对象
     * @throws JsonException If o is a non-finite number.
     */
    public static void testValidity(Object obj) throws JsonException {
        if (false == ObjectCommand.isValidIfNumber(obj)) {
            throw new JsonException("Json does not allow non-finite numbers.");
        }
    }

    /**
     * 值转为String，用于JSON中。 If the object has an value.toJSONString() method, then that method will be used to produce the
     * Json text. <br>
     * The method is required to produce a strictly conforming text. <br>
     * If the object does not contain a toJSONString method (which is the most common case), then a text will be
     * produced by other means. <br>
     * If the value is an array or Collection, then a JsonArray will be made from it and its toJSONString method will be
     * called. <br>
     * If the value is a MAP, then a JsonObject will be made from it and its toJSONString method will be called. <br>
     * Otherwise, the value's toString method will be called, and the result will be quoted.<br>
     *
     * @param value 需要转为字符串的对象
     * @return 字符串
     * @throws JsonException If the value is or contains an invalid number.
     */
    public static String valueToString(Object value) throws JsonException {
        if (value == null || value.equals(null)) {
            return "null";
        }
        if (value instanceof StringJson) {
            try {
                return ((StringJson) value).toJSONString();
            } catch (Exception e) {
                throw new JsonException(e);
            }
        } else if (value instanceof Number) {
            return NumberCommand.toStr((Number) value);
        } else if (value instanceof Boolean || value instanceof MapJson || value instanceof ListJson) {
            return value.toString();
        } else if (value instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) value;
            return new MapJson(map).toString();
        } else if (value instanceof Collection) {
            Collection<?> coll = (Collection<?>) value;
            return new ListJson(coll).toString();
        } else if (value.getClass().isArray()) {
            return new ListJson(value).toString();
        } else {
            return JsonCommand.quote(value.toString());
        }
    }

    /**
     * 尝试转换字符串为number, boolean, or null，无法转换返回String
     *
     * @param string A String.
     * @return A simple Json value.
     */
    public static Object stringToValue(String string) {
        Double d;
        if (null == string || "null".equalsIgnoreCase(string)) {
            return NullJson.NULL;
        }

        if (StringCommand.EMPTY.equals(string)) {
            return string;
        }
        if ("true".equalsIgnoreCase(string)) {
            return Boolean.TRUE;
        }
        if ("false".equalsIgnoreCase(string)) {
            return Boolean.FALSE;
        }

        /*
         * If it might be a number, try converting it. If a number cannot be produced, then the value will just be a
         * string.
         */
        char b = string.charAt(0);
        if ((b >= '0' && b <= '9') || b == '-') {
            try {
                if (string.indexOf('.') > -1 || string.indexOf('e') > -1 || string.indexOf('E') > -1) {
                    d = Double.valueOf(string);
                    if (!d.isInfinite() && !d.isNaN()) {
                        return d;
                    }
                } else {
                    Long myLong = new Long(string);
                    if (string.equals(myLong.toString())) {
                        if (myLong == myLong.intValue()) {
                            return myLong.intValue();
                        } else {
                            return myLong;
                        }
                    }
                }
            } catch (Exception ignore) {
            }
        }
        return string;
    }

    /**
     * 将Property的键转化为JSON形式<br>
     * 用于识别类似于：com.luxiaolei.package.hutool这类用点隔开的键
     *
     * @param mapJson JsonObject
     * @param key        键
     * @param value      值
     * @return JsonObject
     */
    public static MapJson propertyPut(MapJson mapJson, Object key, Object value) {
        String keyStr = Convert.toStr(key);
        String[] path = StringCommand.split(keyStr, StringCommand.DOT);
        int last = path.length - 1;
        MapJson target = mapJson;
        for (int i = 0; i < last; i += 1) {
            String segment = path[i];
            MapJson nextTarget = target.getJSONObject(segment);
            if (nextTarget == null) {
                nextTarget = new MapJson();
                target.put(segment, nextTarget);
            }
            target = nextTarget;
        }
        target.put(path[last], value);
        return mapJson;
    }

    /**
     * JSON或者
     *
     * @param mapJson  JSON对象
     * @param bean        目标Bean
     * @param ignoreError 是否忽略转换错误
     * @return 目标Bean
     */
    public static <T> T toBean(final MapJson mapJson, T bean, final boolean ignoreError) {
        return BeanUtil.fillBean(bean, new BeanUtil.ValueProvider<String>() {

            @Override
            public Object value(String key, Class<?> valueType) {
                return jsonConvert(valueType, mapJson.get(key), ignoreError);
            }

            @Override
            public boolean containsKey(String key) {
                return mapJson.containsKey(key);
            }

        }, BeanUtil.CopyOptions.create().setIgnoreError(ignoreError));
    }

    /**
     * JSON递归转换<br>
     * 首先尝试JDK类型转换，如果失败尝试JSON转Bean
     *
     * @param type        目标类型
     * @param value       值
     * @param ignoreError 是否忽略转换错误
     * @return 目标类型的值
     * @throws ConvertException 转换失败
     */
    public static <T> T jsonConvert(Class<T> type, Object value, boolean ignoreError) throws ConvertException {
        if (null == value) {
            return null;
        }

        T targetValue = null;
        try {
            targetValue = ConverterRegistry.getInstance().convert(type, value);
        } catch (ConvertException e) {
            // ignore
        }

        // 非标准转换格式
        if (null == targetValue) {

            // 子对象递归转换
            if (value instanceof MapJson) {
                targetValue = JsonCommand.fromJson(value.toString(), type);
            }
        }

        if (null == targetValue) {
            throw new ConvertException("Can not convert to type [{}]", type.getName());
        }

        return targetValue;
    }
}
