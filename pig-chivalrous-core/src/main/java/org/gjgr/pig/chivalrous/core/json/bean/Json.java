package org.gjgr.pig.chivalrous.core.json.bean;

import org.gjgr.pig.chivalrous.core.json.JsonException;

import java.io.Writer;

/**
 * JSON接口
 *
 * @author Looly
 */
public interface Json {

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

    /**
     * 转换为JSON字符串
     *
     * @param indentFactor 每一级别的缩进
     * @return JSON字符串
     * @throws JsonException
     */
    public String toJSONString(int indentFactor) throws JsonException;
}
