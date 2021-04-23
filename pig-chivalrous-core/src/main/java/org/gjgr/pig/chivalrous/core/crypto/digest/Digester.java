package org.gjgr.pig.chivalrous.core.crypto.digest;

import org.gjgr.pig.chivalrous.core.crypto.CryptoException;
import org.gjgr.pig.chivalrous.core.io.IoCommand;
import org.gjgr.pig.chivalrous.core.io.file.FileCommand;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.gjgr.pig.chivalrous.core.math.HexCommand;
import org.gjgr.pig.chivalrous.core.nio.CharsetCommand;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 摘要算法<br>
 * 注意：此对象实例化后为非线程安全！
 *
 * @author Looly
 */
public class Digester {

    private MessageDigest digest;

    public Digester(DigestType algorithm) {
        init(algorithm.getValue());
    }

    /**
     * 初始化
     *
     * @param algorithm 算法
     * @return {@link Digester}
     * @throws CryptoException Cause by IOException
     */
    public Digester init(String algorithm) {
        try {
            digest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
        return this;
    }

    // ------------------------------------------------------------------------------------------- Digest

    /**
     * 生成文件摘要
     *
     * @param data    被摘要数据
     * @param charset 编码
     * @return 摘要
     */
    public byte[] digest(String data, String charset) {
        return digest(StringCommand.bytes(data, charset));
    }

    /**
     * 生成文件摘要
     *
     * @param data 被摘要数据
     * @return 摘要
     */
    public byte[] digest(String data) {
        return digest(data, CharsetCommand.UTF_8);
    }

    /**
     * 生成文件摘要，并转为16进制字符串
     *
     * @param data    被摘要数据
     * @param charset 编码
     * @return 摘要
     */
    public String digestHex(String data, String charset) {
        return HexCommand.encodeHexStr(digest(data, charset));
    }

    /**
     * 生成文件摘要
     *
     * @param data 被摘要数据
     * @return 摘要
     */
    public String digestHex(String data) {
        return digestHex(data, CharsetCommand.UTF_8);
    }

    /**
     * 生成文件摘要<br>
     * 使用默认缓存大小，见 {@link IoCommand#DEFAULT_BUFFER_SIZE}
     *
     * @param file 被摘要文件
     * @return 摘要bytes
     * @throws CryptoException Cause by IOException
     */
    public byte[] digest(File file) {
        InputStream in = null;
        try {
            in = IoCommand.bufferedInputStream(file);
            return digest(in);
        } catch (IOException e) {
            throw new CryptoException(e);
        } finally {
            IoCommand.close(in);
        }
    }

    /**
     * 生成文件摘要，并转为16进制字符串<br>
     * 使用默认缓存大小，见 {@link IoCommand#DEFAULT_BUFFER_SIZE}
     *
     * @param file 被摘要文件
     * @return 摘要
     */
    public String digestHex(File file) {
        return HexCommand.encodeHexStr(digest(file));
    }

    /**
     * 生成摘要
     *
     * @param data 数据bytes
     * @return 摘要bytes
     */
    public byte[] digest(byte[] data) {
        byte[] result;
        try {
            result = digest.digest(data);
        } finally {
            digest.reset();
        }
        return result;
    }

    /**
     * 生成摘要，并转为16进制字符串<br>
     *
     * @param data 被摘要数据
     * @return 摘要
     */
    public String digestHex(byte[] data) {
        return HexCommand.encodeHexStr(digest(data));
    }

    /**
     * 生成摘要，使用默认缓存大小，见 {@link IoCommand#DEFAULT_BUFFER_SIZE}
     *
     * @param data {@link InputStream} 数据流
     * @return 摘要bytes
     */
    public byte[] digest(InputStream data) {
        return digest(data, IoCommand.DEFAULT_BUFFER_SIZE);
    }

    /**
     * 生成摘要，并转为16进制字符串<br>
     * 使用默认缓存大小，见 {@link IoCommand#DEFAULT_BUFFER_SIZE}
     *
     * @param data 被摘要数据
     * @return 摘要
     */
    public String digestHex(InputStream data) {
        return HexCommand.encodeHexStr(digest(data));
    }

    /**
     * 生成摘要
     *
     * @param data         {@link InputStream} 数据流
     * @param bufferLength 缓存长度，不足1使用 {@link IoCommand#DEFAULT_BUFFER_SIZE} 做为默认值
     * @return 摘要bytes
     */
    public byte[] digest(InputStream data, int bufferLength) {
        if (bufferLength < 1) {
            bufferLength = IoCommand.DEFAULT_BUFFER_SIZE;
        }
        byte[] buffer = new byte[bufferLength];

        byte[] result = null;
        try {
            int read = data.read(buffer, 0, bufferLength);

            while (read > -1) {
                digest.update(buffer, 0, read);
                read = data.read(buffer, 0, bufferLength);
            }
            result = digest.digest();
        } catch (IOException e) {
            throw new CryptoException(e);
        } finally {
            digest.reset();
        }
        return result;
    }

    /**
     * 生成摘要，并转为16进制字符串<br>
     * 使用默认缓存大小，见 {@link IoCommand#DEFAULT_BUFFER_SIZE}
     *
     * @param data         被摘要数据
     * @param bufferLength 缓存长度，不足1使用 {@link IoCommand#DEFAULT_BUFFER_SIZE} 做为默认值
     * @return 摘要
     */
    public String digestHex(InputStream data, int bufferLength) {
        return HexCommand.encodeHexStr(digest(data, bufferLength));
    }

    /**
     * 获得 {@link MessageDigest}
     *
     * @return {@link MessageDigest}
     */
    public MessageDigest getDigest() {
        return digest;
    }
}
