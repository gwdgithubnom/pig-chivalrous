package org.gjgr.pig.chivalrous.core.json;

import java.io.Writer;

/**
 * <code>JsonString</code>接口定义了一个<code>toJSONString()</code><br>
 * 实现此接口的类可以通过实现<code>toJSONString()</code>方法来改变转JSON字符串的方式。
 *
 * @author Looly
 */
public interface StringJson extends JsonString{

    /**
     * 将JSON内容写入Writer，无缩进<br>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @param writer Writer
     * @return Writer
     * @throws JsonException
     */
    public Writer write(Writer writer) throws JsonException;

    /**
     * 将JSON内容写入Writer<br>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @param writer       writer
     * @param indentFactor 每一级别的缩进量
     * @param indent       顶级别缩进量
     * @return Writer
     * @throws JsonException
     */
    public Writer write(Writer writer, int indentFactor, int indent) throws JsonException;
}
