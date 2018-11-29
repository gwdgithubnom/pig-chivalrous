package org.gjgr.pig.chivalrous.crypto.test;

import org.gjgr.pig.chivalrous.core.crypto.CryptoCommand;
import org.gjgr.pig.chivalrous.core.crypto.symmetric.SymmetricAlgorithm;
import org.gjgr.pig.chivalrous.core.crypto.symmetric.SymmetricCrypto;
import org.gjgr.pig.chivalrous.core.util.CharsetUtil;
import org.gjgr.pig.chivalrous.core.util.StrUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * 对称加密算法单元测试
 *
 * @author Looly
 */
public class SymmetricTest {

    @Test
    public void aesTest() {
        String content = "test中文";

        //随机生成密钥
        byte[] key = CryptoCommand.secretKey(SymmetricAlgorithm.AES.getValue()).getEncoded();

        //构建
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        //加密
        byte[] encrypt = aes.encrypt(content);
        //解密
        byte[] decrypt = aes.decrypt(encrypt);

        Assert.assertEquals(content, StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));

        //加密为16进制表示
        String encryptHex = aes.encryptHex(content);
        //解密为字符串
        String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);

        Assert.assertEquals(content, decryptStr);
    }

    @Test
    public void desTest() {
        String content = "test中文";

        byte[] key = CryptoCommand.secretKey(SymmetricAlgorithm.DES.getValue()).getEncoded();

        SymmetricCrypto des = new SymmetricCrypto(SymmetricAlgorithm.DES, key);
        byte[] encrypt = des.encrypt(content);
        byte[] decrypt = des.decrypt(encrypt);

        Assert.assertEquals(content, StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));

        String encryptHex = des.encryptHex(content);
        String decryptStr = des.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);

        Assert.assertEquals(content, decryptStr);
    }

    @Test
    public void testSignature() {
        String from = "https://fanyi.baidu.com/#en/zh/cryptogram";
        String to = CryptoCommand.md5(from);
        Assert.assertEquals(to, "549d849605c67cb3e95a6dd4143d7b92");
    }
}
