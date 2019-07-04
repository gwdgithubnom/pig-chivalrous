package org.gjgr.pig.chivalrous.core.crypto.asymmetric;

import org.gjgr.pig.chivalrous.core.crypto.CryptoException;
import org.gjgr.pig.chivalrous.core.lang.ArrayCommand;
import org.gjgr.pig.chivalrous.core.lang.BCD;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.gjgr.pig.chivalrous.core.nio.CharsetCommand;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.interfaces.RSAKey;

/**
 * RSA加密算法
 *
 * @author Looly
 */
public class RSA extends AsymmetricCrypto {

    private static final AsymmetricType ALGORITHM_RSA = AsymmetricType.RSA;

    // ------------------------------------------------------------------ Constructor start

    /**
     * 构造，生成新的私钥公钥对
     */
    public RSA() {
        super(ALGORITHM_RSA);
    }

    /**
     * 构造<br>
     * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
     * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
     *
     * @param privateKeyBase64 私钥Base64
     * @param publicKeyBase64  公钥Base64
     */
    public RSA(String privateKeyBase64, String publicKeyBase64) {
        super(ALGORITHM_RSA, privateKeyBase64, publicKeyBase64);
    }

    /**
     * 构造 <br>
     * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
     * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
     *
     * @param privateKey 私钥
     * @param publicKey  公钥
     */
    public RSA(byte[] privateKey, byte[] publicKey) {
        super(ALGORITHM_RSA, privateKey, publicKey);
    }
    // ------------------------------------------------------------------ Constructor end

    /**
     * 分组加密
     *
     * @param data    数据
     * @param keyType 密钥类型
     * @return 加密后的密文
     * @throws Exception
     */
    public String encryptStr(String data, KeyType keyType) {
        Key key = getKeyByType(keyType);
        // 模长
        int keyLength = ((RSAKey) key).getModulus().bitLength() / 8;
        StringBuilder sb = StringCommand.builder();
        lock.lock();
        try {
            clipher.init(Cipher.ENCRYPT_MODE, key);
            // 加密数据长度 <= 模长-11
            String[] datas = StringCommand.split(data, keyLength - 11);
            // 如果明文长度大于模长-11则要分组加密
            for (String s : datas) {
                sb.append(BCD.bcdToStr(clipher.doFinal(s.getBytes())));
            }
        } catch (Exception e) {
            throw new CryptoException(e);
        } finally {
            lock.unlock();
        }
        return sb.toString();
    }

    /**
     * 分组解密
     *
     * @param data    数据
     * @param keyType 密钥类型
     * @return 加密后的密文
     */
    public String decryptStr(String data, KeyType keyType) {
        Key key = getKeyByType(keyType);
        // 模长
        int keyLength = ((RSAKey) key).getModulus().bitLength() / 8;
        StringBuilder sb = StringCommand.builder();
        lock.lock();
        try {
            clipher.init(Cipher.DECRYPT_MODE, key);
            // 加密数据长度 <= 模长-11
            byte[] bcd = BCD.ascToBcd(StringCommand.utf8Bytes(data));
            // 如果密文长度大于模长则要分组解密
            byte[][] arrays = ArrayCommand.split(bcd, keyLength);
            for (byte[] arr : arrays) {
                sb.append(StringCommand.str(clipher.doFinal(arr), CharsetCommand.CHARSET_UTF_8));
            }
        } catch (Exception e) {
            throw new CryptoException(e);
        } finally {
            lock.unlock();
        }
        return sb.toString();
    }
}
