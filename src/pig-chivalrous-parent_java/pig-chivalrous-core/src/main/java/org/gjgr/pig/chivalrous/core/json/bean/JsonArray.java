package org.gjgr.pig.chivalrous.core.json.bean;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.gjgr.pig.chivalrous.core.json.InternalJsonUtil;
import org.gjgr.pig.chivalrous.core.json.JsonCommand;
import org.gjgr.pig.chivalrous.core.json.JsonException;
import org.gjgr.pig.chivalrous.core.json.JsonGetter;
import org.gjgr.pig.chivalrous.core.json.JsonTokener;

/**
 * JSON数组
 *
 * @author looly
 */
public class JsonArray extends JsonGetter<Integer> implements Json, List<Object> {

    /**
     * 持有原始数据的List
     */
    private final ArrayList<Object> rawArrayList;

    // --------------------------------------------------------------------------------------------------------------------
    // Constructor start

    /**
     * 构造
     */
    public JsonArray() {
        this.rawArrayList = new ArrayList<Object>();
    }

    /**
     * 使用 {@link JsonTokener} 做为参数构造
     *
     * @param x A {@link JsonTokener}
     * @throws JsonException If there is a syntax error.
     */
    public JsonArray(JsonTokener x) throws JsonException {
        this();
        if (x.nextClean() != '[') {
            throw x.syntaxError("A JsonArray text must start with '['");
        }
        if (x.nextClean() != ']') {
            x.back();
            for (;;) {
                if (x.nextClean() == ',') {
                    x.back();
                    this.rawArrayList.add(JsonNull.NULL);
                } else {
                    x.back();
                    this.rawArrayList.add(x.nextValue());
                }
                switch (x.nextClean()) {
                    case ',':
                        if (x.nextClean() == ']') {
                            return;
                        }
                        x.back();
                        break;
                    case ']':
                        return;
                    default:
                        throw x.syntaxError("Expected a ',' or ']'");
                }
            }
        }
    }

    /**
     * 从String构造（JSONArray字符串）
     *
     * @param source JSON数组字符串
     * @throws JsonException If there is a syntax error.
     */
    public JsonArray(String source) throws JsonException {
        this(new JsonTokener(source));
    }

    /**
     * 从数组或{@link Collection}对象构造
     *
     * @throws JsonException 非数组或集合
     */
    public JsonArray(Object arrayOrCollection) throws JsonException {
        this();
        if (arrayOrCollection.getClass().isArray()) {
            // 数组
            int length = Array.getLength(arrayOrCollection);
            for (int i = 0; i < length; i += 1) {
                this.put(JsonCommand.wrap(Array.get(arrayOrCollection, i)));
            }
        } else if (arrayOrCollection instanceof Iterable<?>) {
            // Iterable
            for (Object o : (Collection<?>) arrayOrCollection) {
                this.add(o);
            }
        } else {
            throw new JsonException("JsonArray initial value should be a string or collection or array.");
        }
    }
    // --------------------------------------------------------------------------------------------------------------------
    // Constructor start

    /**
     * 值是否为<code>null</code>
     *
     * @param index 值所在序列
     * @return true if the value at the index is null, or if there is no value.
     */
    public boolean isNull(int index) {
        return JsonNull.NULL.equals(this.getObj(index));
    }

    /**
     * JSONArray转为以<code>separator</code>为分界符的字符串
     *
     * @param separator 分界符
     * @return a string.
     * @throws JsonException If the array contains an invalid number.
     */
    public String join(String separator) throws JsonException {
        int len = this.rawArrayList.size();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < len; i += 1) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(InternalJsonUtil.valueToString(this.rawArrayList.get(i)));
        }
        return sb.toString();
    }

    @Override
    public Object getObj(Integer index, Object defaultValue) {
        return (index < 0 || index >= this.size()) ? defaultValue : this.rawArrayList.get(index);
    }

    /**
     * Append an object value. This increases the array's length by one. 加入元素，数组长度+1，等同于 {@link JsonArray#add(Object)}
     *
     * @param value 值，可以是： Boolean, Double, Integer, JsonArray, JsonObject, Long, or String, or the JsonNull.NULL。
     * @return this.
     */
    public JsonArray put(Object value) {
        this.add(value);
        return this;
    }

    /**
     * 加入或者替换JSONArray中指定Index的值，如果index大于JSONArray的长度，将在指定index设置值，之前的位置填充JSONNull.Null
     *
     * @param index 位置
     * @param value 值对象. 可以是以下类型: Boolean, Double, Integer, JsonArray, JsonObject, Long, String, or the JsonNull.NULL.
     * @return this.
     * @throws JsonException index < 0 或者非有限的数字
     */
    public JsonArray put(int index, Object value) throws JsonException {
        this.add(index, value);
        return this;
    }

    /**
     * 是否与其它对象相等，其它对象也必须是JSONArray，对象元素相等且元素顺序一致
     *
     * @param other The other JsonArray
     * @return true if they are equal
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof JsonArray)) {
            return false;
        }
        int len = this.size();
        if (len != ((JsonArray) other).size()) {
            return false;
        }
        for (int i = 0; i < len; i += 1) {
            Object valueThis = this.getObj(i);
            Object valueOther = ((JsonArray) other).getObj(i);
            if (valueThis instanceof JsonObject) {
                if (!((JsonObject) valueThis).equals(valueOther)) {
                    return false;
                }
            } else if (valueThis instanceof JsonArray) {
                if (!((JsonArray) valueThis).equals(valueOther)) {
                    return false;
                }
            } else if (!valueThis.equals(valueOther)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 转为JSON字符串，无缩进
     *
     * @return JSONArray字符串
     */
    @Override
    public String toString() {
        try {
            return this.toJSONString(0);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据给定名列表，与其位置对应的值组成JSONObject
     *
     * @param names 名列表，位置与JSONArray中的值位置对应
     * @return A JsonObject，无名或值返回null
     * @throws JsonException 如果任何一个名为null
     */
    public JsonObject toJSONObject(JsonArray names) throws JsonException {
        if (names == null || names.size() == 0 || this.size() == 0) {
            return null;
        }
        JsonObject jo = new JsonObject();
        for (int i = 0; i < names.size(); i += 1) {
            jo.put(names.getStr(i), this.getObj(i));
        }
        return jo;
    }

    @Override
    public Writer write(Writer writer) throws JsonException {
        return this.write(writer, 0, 0);
    }

    @Override
    public Writer write(Writer writer, int indentFactor, int indent) throws JsonException {
        try {
            boolean commanate = false;
            int length = this.size();
            writer.write('[');

            if (length == 1) {
                InternalJsonUtil.writeValue(writer, this.rawArrayList.get(0), indentFactor, indent);
            } else if (length != 0) {
                final int newindent = indent + indentFactor;

                for (int i = 0; i < length; i += 1) {
                    if (commanate) {
                        writer.write(',');
                    }
                    if (indentFactor > 0) {
                        writer.write('\n');
                    }
                    InternalJsonUtil.indent(writer, newindent);
                    InternalJsonUtil.writeValue(writer, this.rawArrayList.get(i), indentFactor, newindent);
                    commanate = true;
                }
                if (indentFactor > 0) {
                    writer.write('\n');
                }
                InternalJsonUtil.indent(writer, indent);
            }
            writer.write(']');
            return writer;
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * 转为JSON字符串，指定缩进值
     *
     * @param indentFactor 缩进值，既缩进空格数
     * @return JSON字符串
     * @throws JsonException
     */
    @Override
    public String toJSONString(int indentFactor) throws JsonException {
        StringWriter sw = new StringWriter();
        synchronized (sw.getBuffer()) {
            return this.write(sw, indentFactor, 0).toString();
        }
    }

    @Override
    public int size() {
        return rawArrayList.size();
    }

    @Override
    public boolean isEmpty() {
        return rawArrayList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return rawArrayList.contains(o);
    }

    @Override
    public Iterator<Object> iterator() {
        return rawArrayList.iterator();
    }

    @Override
    public Object[] toArray() {
        return rawArrayList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return rawArrayList.toArray(a);
    }

    @Override
    public boolean add(Object e) {
        return this.rawArrayList.add(JsonCommand.wrap(e));
    }

    @Override
    public boolean remove(Object o) {
        return rawArrayList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return rawArrayList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Object> c) {
        for (Object obj : c) {
            this.add(obj);
        }
        return rawArrayList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Object> c) {
        return rawArrayList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.rawArrayList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.rawArrayList.retainAll(c);
    }

    @Override
    public void clear() {
        this.rawArrayList.clear();

    }

    @Override
    public Object get(int index) {
        return this.rawArrayList.get(index);
    }

    @Override
    public Object set(int index, Object element) {
        return this.rawArrayList.set(index, JsonCommand.wrap(element));
    }

    @Override
    public void add(int index, Object element) {
        if (index < 0) {
            throw new JsonException("JsonArray[" + index + "] not found.");
        }
        if (index < this.size()) {
            InternalJsonUtil.testValidity(element);
            this.rawArrayList.set(index, JsonCommand.wrap(element));
        } else {
            while (index != this.size()) {
                this.add(JsonNull.NULL);
            }
            this.put(element);
        }

    }

    @Override
    public Object remove(int index) {
        return index >= 0 && index < this.size() ? this.rawArrayList.remove(index) : null;
    }

    @Override
    public int indexOf(Object o) {
        return this.rawArrayList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.rawArrayList.lastIndexOf(o);
    }

    @Override
    public ListIterator<Object> listIterator() {
        return this.rawArrayList.listIterator();
    }

    @Override
    public ListIterator<Object> listIterator(int index) {
        return this.rawArrayList.listIterator(index);
    }

    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        return this.rawArrayList.subList(fromIndex, toIndex);
    }
}
