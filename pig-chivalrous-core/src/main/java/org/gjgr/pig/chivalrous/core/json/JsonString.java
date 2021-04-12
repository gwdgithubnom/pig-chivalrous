package org.gjgr.pig.chivalrous.core.json;

import java.io.Serializable;
import org.gjgr.pig.chivalrous.core.json.JsonException;

import java.io.Writer;

/**
 * JSON接口
 *
 * @author Looly
 */
public interface JsonString extends Serializable {

    /**
     * 自定义转JSON字符串的方法
     *
     * @return JSON字符串
     */
    public String toJSONString();

    /**
     * 转换为JSON字符串
     *
     * @param indentFactor 每一级别的缩进
     * @return JSON字符串
     * @throws JsonException
     */
    public String toJSONString(int indentFactor) throws JsonException;

}
