package org.gjgr.pig.chivalrous.core.crypto.digest;

import org.gjgr.pig.chivalrous.core.exceptions.UtilException;
import org.gjgr.pig.chivalrous.core.nio.CharsetCommand;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.InputStream;

/**
 * 摘要算法工具类
 *
 * @author Looly
 */
public final class DigestUtil {

    private DigestUtil() {
    }

    // ------------------------------------------------------------------------------------------- MD5

    /**
     * 计算16位MD5摘要值
     *
     * @param data 被摘要数据
     * @return MD5摘要
     */
    public static byte[] md5(byte[] data) {
        return new Digester(DigestType.MD5).digest(data);
    }

    /**
     * 计算16位MD5摘要值
     *
     * @param data    被摘要数据
     * @param charset 编码
     * @return MD5摘要
     */
    public static byte[] md5(String data, String charset) {
        return new Digester(DigestType.MD5).digest(data, charset);
    }

    /**
     * 计算16位MD5摘要值，使用UTF-8编码
     *
     * @param data 被摘要数据
     * @return MD5摘要
     */
    public static byte[] md5(String data) {
        return md5(data, CharsetCommand.UTF_8);
    }

    /**
     * 计算16位MD5摘要值
     *
     * @param data 被摘要数据
     * @return MD5摘要
     * @throws UtilException Cause by IOException
     */
    public static byte[] md5(InputStream data) {
        return new Digester(DigestType.MD5).digest(data);
    }

    /**
     * 计算16位MD5摘要值
     *
     * @param file 被摘要文件
     * @return MD5摘要
     * @throws UtilException Cause by IOException
     */
    public static byte[] md5(File file) {
        return new Digester(DigestType.MD5).digest(file);
    }

    /**
     * 计算16位MD5摘要值，并转为16进制字符串
     *
     * @param data 被摘要数据
     * @return MD5摘要的16进制表示
     */
    public static String md5Hex(byte[] data) {
        return new Digester(DigestType.MD5).digestHex(data);
    }

    /**
     * 计算16位MD5摘要值，并转为16进制字符串
     *
     * @param data    被摘要数据
     * @param charset 编码
     * @return MD5摘要的16进制表示
     */
    public static String md5Hex(String data, String charset) {
        return new Digester(DigestType.MD5).digestHex(data, charset);
    }

    /**
     * 计算16位MD5摘要值，并转为16进制字符串
     *
     * @param data 被摘要数据
     * @return MD5摘要的16进制表示
     */
    public static String md5Hex(String data) {
        return md5Hex(data, CharsetCommand.UTF_8);
    }

    /**
     * 计算16位MD5摘要值，并转为16进制字符串
     *
     * @param data 被摘要数据
     * @return MD5摘要的16进制表示
     */
    public static String md5Hex(InputStream data) {
        return new Digester(DigestType.MD5).digestHex(data);
    }

    /**
     * 计算16位MD5摘要值，并转为16进制字符串
     *
     * @param file 被摘要文件
     * @return MD5摘要的16进制表示
     */
    public static String md5Hex(File file) {
        return new Digester(DigestType.MD5).digestHex(file);
    }

    // ------------------------------------------------------------------------------------------- SHA-1

    /**
     * 计算SHA-1摘要值
     *
     * @param data 被摘要数据
     * @return SHA-1摘要
     */
    public static byte[] sha1(byte[] data) {
        return new Digester(DigestType.SHA1).digest(data);
    }

    /**
     * 计算SHA-1摘要值
     *
     * @param data    被摘要数据
     * @param charset 编码
     * @return SHA-1摘要
     */
    public static byte[] sha1(String data, String charset) {
        return new Digester(DigestType.SHA1).digest(data, charset);
    }

    /**
     * 计算sha1摘要值，使用UTF-8编码
     *
     * @param data 被摘要数据
     * @return MD5摘要
     */
    public static byte[] sha1(String data) {
        return sha1(data, CharsetCommand.UTF_8);
    }

    /**
     * 计算SHA-1摘要值
     *
     * @param data 被摘要数据
     * @return SHA-1摘要
     */
    public static byte[] sha1(InputStream data) {
        return new Digester(DigestType.SHA1).digest(data);
    }

    /**
     * 计算SHA-1摘要值
     *
     * @param file 被摘要文件
     * @return SHA-1摘要
     * @throws UtilException Cause by IOException
     */
    public static byte[] sha1(File file) {
        return new Digester(DigestType.SHA1).digest(file);
    }

    /**
     * 计算SHA-1摘要值，并转为16进制字符串
     *
     * @param data 被摘要数据
     * @return SHA-1摘要的16进制表示
     */
    public static String sha1Hex(byte[] data) {
        return new Digester(DigestType.SHA1).digestHex(data);
    }

    /**
     * 计算SHA-1摘要值，并转为16进制字符串
     *
     * @param data    被摘要数据
     * @param charset 编码
     * @return SHA-1摘要的16进制表示
     */
    public static String sha1Hex(String data, String charset) {
        return new Digester(DigestType.SHA1).digestHex(data, charset);
    }

    /**
     * 计算SHA-1摘要值，并转为16进制字符串
     *
     * @param data 被摘要数据
     * @return SHA-1摘要的16进制表示
     */
    public static String sha1Hex(String data) {
        return sha1Hex(data, CharsetCommand.UTF_8);
    }

    /**
     * 计算SHA-1摘要值，并转为16进制字符串
     *
     * @param data 被摘要数据
     * @return SHA-1摘要的16进制表示
     */
    public static String sha1Hex(InputStream data) {
        return new Digester(DigestType.SHA1).digestHex(data);
    }

    /**
     * 计算SHA-1摘要值，并转为16进制字符串
     *
     * @param file 被摘要文件
     * @return SHA-1摘要的16进制表示
     */
    public static String sha1Hex(File file) {
        return new Digester(DigestType.SHA1).digestHex(file);
    }

    // ------------------------------------------------------------------------------------------- Hmac

    /**
     * 创建HMac对象，调用digest方法可获得hmac值
     *
     * @param algorithm {@link HmacAlgorithm}
     * @param key       密钥，如果为<code>null</code>生成随机密钥
     * @return {@link HMac}
     * @since 3.0.3
     */
    public static HMac hmac(HmacAlgorithm algorithm, byte[] key) {
        return new HMac(algorithm, key);
    }

    /**
     * 创建HMac对象，调用digest方法可获得hmac值
     *
     * @param algorithm {@link HmacAlgorithm}
     * @param key       密钥{@link SecretKey}，如果为<code>null</code>生成随机密钥
     * @return {@link HMac}
     * @since 3.0.3
     */
    public static HMac hmac(HmacAlgorithm algorithm, SecretKey key) {
        return new HMac(algorithm, key);
    }
}
