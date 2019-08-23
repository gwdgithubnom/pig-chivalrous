package org.gjgr.pig.chivalrous.core.json.bean;

/**
 * 用于定义<code>null</code>，与Javascript中null相对应<br>
 * Java中的<code>null</code>值在js中表示为undefined。
 *
 * @author Looly
 */
public class JsonNull {

    /**
     * <code>NULL</code> 对象用于减少歧义来表示Java 中的<code>null</code> <br>
     * <code>NULL.equals(null)</code> 返回 <code>true</code>. <br>
     * <code>NULL.toString()</code> 返回 <code>"null"</code>.
     */
    public static final JsonNull NULL = new JsonNull();

    /**
     * A Null object is equal to the null value and to itself. 对象与其本身和<code>null</code>值相等
     *
     * @param object An object to test for nullness.
     * @return true if the object parameter is the JsonObject.NULL object or null.
     */
    @Override
    public boolean equals(Object object) {
        return object == null || object == this;
    }

    /**
     * There is only intended to be a single instance of the NULL object, so the clone method returns itself.
     * 克隆方法只返回本身，此对象是个单例对象
     *
     * @return NULL.
     */
    @Override
    protected final Object clone() {
        return this;
    }

    /**
     * Get the "null" string value. 获得“null”字符串
     *
     * @return The string "null".
     */
    @Override
    public String toString() {
        return "null";
    }
}