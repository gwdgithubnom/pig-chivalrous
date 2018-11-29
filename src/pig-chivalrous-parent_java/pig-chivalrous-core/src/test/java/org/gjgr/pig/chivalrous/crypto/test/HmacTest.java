package org.gjgr.pig.chivalrous.crypto.test;

import org.gjgr.pig.chivalrous.core.crypto.digest.HMac;
import org.gjgr.pig.chivalrous.core.crypto.digest.HmacAlgorithm;
import org.gjgr.pig.chivalrous.core.io.IoCommand;
import org.gjgr.pig.chivalrous.core.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Hmac单元测试
 *
 * @author Looly
 */
public class HmacTest {
    @Test
    public void hmacTest() {
        String testStr = "test中文";

        byte[] key = "password".getBytes();
        HMac mac = new HMac(HmacAlgorithm.HmacMD5, key);

        String macHex1 = mac.digestHex(testStr);
        Assert.assertEquals("b977f4b13f93f549e06140971bded384", macHex1);

        String macHex2 = mac.digestHex(IoCommand.toStream(testStr, CharsetUtil.CHARSET_UTF_8));
        Assert.assertEquals("b977f4b13f93f549e06140971bded384", macHex2);
    }
}
