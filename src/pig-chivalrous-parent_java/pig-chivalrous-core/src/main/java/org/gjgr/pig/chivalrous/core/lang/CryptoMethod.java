package org.gjgr.pig.chivalrous.core.lang;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * @Author gwd
 * @Time 10-29-2018 Monday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public class CryptoMethod {
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final byte[] DEFAULT_KEY =
            { 0x41, 0x69, 0x14, 0x38, 0x48, 0x26, 0x48, 0x61, 0x16, 0x61, 0x15, 0x79, 0x36, 0x25, 0x24, 0x3 };
    private static final byte[] DEFAULT_IV =
            { 0x64, 0x17, 0x54, 0x72, 0x48, 0x00, 0x4, 0x61, 0x49, 0x61, 0x2, 0x34, 0x54, 0x66, 0x12, 0x20 };
    private static final CryptoMethod DEFAULT = new CryptoMethod(DEFAULT_KEY, DEFAULT_IV);
    private byte[] key;
    private byte[] iv;

    public CryptoMethod(byte[] key, byte[] iv) {
        this.key = key;
        this.iv = iv;
    }

    public static String aes_cbc_crypt(String input, Type type) throws Exception {
        switch (type) {
            case DECRYPT:
                return DEFAULT.aes_cbc_decrypt(input);
            case ENCRYPT:
                return DEFAULT.aes_cbc_encrypt(input);
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }

    /**
     * Decrypt the input string encoded with Base64
     */
    public String aes_cbc_decrypt(String input) throws Exception {
        byte[] encrypted = new Base64().decode(input); // UTF-8 is used in Base64.decode
        byte[] origial = createCipher(Cipher.DECRYPT_MODE).doFinal(encrypted);
        return new String(origial, DEFAULT_ENCODING);
    }

    /**
     * Encrypt the input string and return the result encoded with Base64
     */
    public String aes_cbc_encrypt(String input) throws Exception {
        byte[] encrypted = createCipher(Cipher.ENCRYPT_MODE).doFinal(input.getBytes(DEFAULT_ENCODING));
        // encode the bytes without line break
        return new Base64(-1).encodeToString(encrypted);
    }

    private Cipher createCipher(int mode) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        cipher.init(mode, skeySpec, ivSpec);
        return cipher;
    }

    public static enum Type {
        ENCRYPT,
        DECRYPT
    }
}
