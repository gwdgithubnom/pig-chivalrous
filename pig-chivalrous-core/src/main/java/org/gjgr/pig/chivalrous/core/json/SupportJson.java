package org.gjgr.pig.chivalrous.core.json;

/**
 * JSON支持<br>
 * 继承此类实现实体类与JSON的相互转换
 *
 * @author Looly
 */
public class SupportJson implements JsonString {
    /**
     * Json String转Bean
     *
     * @param jsonString  Json String
     * @param ignoreError 是否忽略转换错误
     */
    public void parse(String jsonString, boolean ignoreError) {
        new MapJson(jsonString).toBean(this, ignoreError);
    }

    /**
     * @return JSON对象
     */
    public MapJson toJSON() {
        return new MapJson(this);
    }

    @Override
    public String toJSONString() {
        return toJSON().toString();
    }

    @Override
    public String toJSONString(int indentFactor) throws JsonException {
        return toJSON().toJSONString();
    }

    /**
     * 美化的JSON（使用回车缩进显示JSON），用于打印输出debug
     *
     * @return 美化的JSON
     */
    public String toPrettyString() {
        return toJSON().toJSONString(4);
    }

    @Override
    public String toString() {
        return toJSONString();
    }

}
