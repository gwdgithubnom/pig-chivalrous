package org.gjgr.pig.chivalrous.core.nio;

import org.gjgr.pig.chivalrous.core.lang.StringCommand;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 字符集工具类
 *
 * @author xiaoleilu
 */
public final class CharsetCommand {

    /**
     * ISO-8859-1
     */
    public static final String ISO_8859_1 = "ISO-8859-1";
    /**
     * UTF-8
     */
    public static final String UTF_8 = "UTF-8";
    /**
     * GBK
     */
    public static final String GBK = "GBK";

    /**
     * ISO-8859-1
     */
    public static final Charset CHARSET_ISO_8859_1 = StandardCharsets.ISO_8859_1;
    /**
     * UTF-8
     */
    public static final Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;
    /**
     * GBK
     */
    public static final Charset CHARSET_GBK = Charset.forName(GBK);

    private CharsetCommand() {
    }

    /**
     * 转换为Charset对象
     *
     * @param charsetName 字符集，为空则返回默认字符集
     * @return Charset
     */
    public static Charset charset(String charsetName) {
        return StringCommand.isBlank(charsetName) ? Charset.defaultCharset() : Charset.forName(charsetName);
    }

    /**
     * 转换字符串的字符集编码
     *
     * @param source      字符串
     * @param srcCharset  源字符集，默认ISO-8859-1
     * @param destCharset 目标字符集，默认UTF-8
     * @return 转换后的字符集
     */
    public static String convert(String source, String srcCharset, String destCharset) {
        return convert(source, Charset.forName(srcCharset), Charset.forName(destCharset));
    }

    /**
     * 转换字符串的字符集编码
     *
     * @param source      字符串
     * @param srcCharset  源字符集，默认ISO-8859-1
     * @param destCharset 目标字符集，默认UTF-8
     * @return 转换后的字符集
     */
    public static String convert(String source, Charset srcCharset, Charset destCharset) {
        if (null == srcCharset) {
            srcCharset = StandardCharsets.ISO_8859_1;
        }

        if (null == destCharset) {
            destCharset = StandardCharsets.UTF_8;
        }

        if (StringCommand.isBlank(source) || srcCharset.equals(destCharset)) {
            return source;
        }
        return new String(source.getBytes(srcCharset), destCharset);
    }

    /**
     * 系统字符集编码，与 {@link CharsetCommand#defaultCharsetName()}功能相同，别名不同
     *
     * @return 系统字符集编码
     * @see CharsetCommand#defaultCharsetName()
     */
    public static String systemCharset() {
        return defaultCharsetName();
    }

    /**
     * 系统默认字符集编码
     *
     * @return 系统字符集编码
     * @see CharsetCommand#defaultCharsetName()
     */
    public static String defaultCharsetName() {
        return Charset.defaultCharset().name();
    }

    /**
     * 系统默认字符集编码
     *
     * @return 系统字符集编码
     * @see CharsetCommand#defaultCharsetName()
     */
    public static Charset defaultCharset() {
        return Charset.defaultCharset();
    }

    public static Charset utf8() {
        return StandardCharsets.UTF_8;
    }

    public static Charset utf16() {
        return StandardCharsets.UTF_16;
    }

    public static Charset utf16Be() {
        return StandardCharsets.UTF_16BE;
    }

    public static Charset utf16Le() {
        return StandardCharsets.UTF_16LE;
    }

    public static Charset ascii() {
        return StandardCharsets.US_ASCII;
    }

    public static Charset iso88591() {
        return StandardCharsets.ISO_8859_1;
    }

    public static Charset gbk() {
        return Charset.forName(GBK);
    }
}
