package org.gjgr.pig.chivalrous.web.http;

import org.gjgr.pig.chivalrous.core.lang.CollectionCommand;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.gjgr.pig.chivalrous.core.nio.CharsetCommand;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * http基类
 *
 * @param <T> 子类类型，方便链式编程
 * @author Looly
 */
@SuppressWarnings("unchecked")
public abstract class HttpBase<T> {

    /**
     * HTTP/1.0
     */
    public static final String HTTP_1_0 = "HTTP/1.0";
    /**
     * HTTP/1.1
     */
    public static final String HTTP_1_1 = "HTTP/1.1";

    /**
     * 存储头信息
     */
    protected Map<String, List<String>> headers = new HashMap<String, List<String>>();
    /**
     * 编码
     */
    protected String charset = CharsetCommand.UTF_8;
    /**
     * http版本
     */
    protected String httpVersion = HTTP_1_1;
    /**
     * 存储主体
     */
    protected String body;

    // ---------------------------------------------------------------- Headers start

    /**
     * 根据name获取头信息
     *
     * @param name Header名
     * @return Header值
     */
    public String header(String name) {
        if (StringCommand.isBlank(name)) {
            return null;
        }

        List<String> values = headers.get(name.trim());
        if (CollectionCommand.isEmpty(values)) {
            return null;
        }
        return values.get(0);
    }

    /**
     * 根据name获取头信息
     *
     * @param name Header名
     * @return Header值
     */
    public String header(Header name) {
        return header(name.toString());
    }

    /**
     * 移除一个头信息
     *
     * @param name Header名
     * @return this
     */
    public T removeHeader(String name) {
        if (name != null) {
            headers.remove(name.trim());
        }
        return (T) this;
    }

    /**
     * 移除一个头信息
     *
     * @param name Header名
     * @return this
     */
    public T removeHeader(Header name) {
        return removeHeader(name.toString());
    }

    /**
     * 设置一个header<br>
     * 如果覆盖模式，则替换之前的值，否则加入到值列表中
     *
     * @param name       Header名
     * @param value      Header值
     * @param isOverride 是否覆盖已有值
     * @return T 本身
     */
    public T header(String name, String value, boolean isOverride) {
        if (null != name && null != value) {
            final List<String> values = headers.get(name.trim());
            if (isOverride || CollectionCommand.isEmpty(values)) {
                final ArrayList<String> valueList = new ArrayList<String>();
                valueList.add(value);
                headers.put(name.trim(), valueList);
            } else {
                values.add(value.trim());
            }
        }
        return (T) this;
    }

    /**
     * 设置一个header<br>
     * 如果覆盖模式，则替换之前的值，否则加入到值列表中
     *
     * @param name       Header名
     * @param value      Header值
     * @param isOverride 是否覆盖已有值
     * @return T 本身
     */
    public T header(Header name, String value, boolean isOverride) {
        return header(name.toString(), value, isOverride);
    }

    /**
     * 设置一个header<br>
     * 覆盖模式，则替换之前的值
     *
     * @param name  Header名
     * @param value Header值
     * @return T 本身
     */
    public T header(Header name, String value) {
        return header(name.toString(), value, true);
    }

    /**
     * 设置一个header<br>
     * 覆盖模式，则替换之前的值
     *
     * @param name  Header名
     * @param value Header值
     * @return T 本身
     */
    public T header(String name, String value) {
        return header(name, value, true);
    }

    /**
     * 设置请求头<br>
     * 不覆盖原有请求头
     *
     * @param headers 请求头
     */
    public T header(Map<String, List<String>> headers) {
        if (CollectionCommand.isEmpty(headers)) {
            return (T) this;
        }

        String name;
        for (Entry<String, List<String>> entry : headers.entrySet()) {
            name = entry.getKey();
            for (String value : entry.getValue()) {
                this.header(name, StringCommand.nullToEmpty(value), false);
            }
        }
        return (T) this;
    }

    /**
     * 获取headers
     *
     * @return Map<String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               List                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               <                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               String>>
     */
    public Map<String, List<String>> headers() {
        return Collections.unmodifiableMap(headers);
    }
    // ---------------------------------------------------------------- Headers end

    /**
     * 返回http版本
     *
     * @return String
     */
    public String httpVersion() {
        return httpVersion;
    }

    /**
     * 设置http版本
     *
     * @param httpVersion
     * @return T
     */
    public T httpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
        return (T) this;
    }

    /**
     * 返回字符集
     *
     * @return 字符集
     */
    public String charset() {
        return charset;
    }

    /**
     * 设置字符集
     *
     * @param charset 字符集
     * @return T 自己
     */
    public T charset(String charset) {
        if (StringCommand.isNotBlank(charset)) {
            this.charset = charset;
        }
        return (T) this;
    }

    /**
     * 设置字符集
     *
     * @param charset 字符集
     * @return T 自己
     */
    public T charset(Charset charset) {
        if (null != charset) {
            this.charset = charset.name();
        }
        return (T) this;
    }

    @Override
    public String toString() {
        StringBuilder sb = StringCommand.builder();
        sb.append("Request Headers: ").append(StringCommand.CRLF);
        for (Entry<String, List<String>> entry : this.headers.entrySet()) {
            sb.append("    ").append(entry).append(StringCommand.CRLF);
        }

        sb.append("Request Body: ").append(StringCommand.CRLF);
        sb.append("    ").append(this.body).append(StringCommand.CRLF);

        return sb.toString();
    }
}
