package org.gjgr.pig.chivalrous.core.lang;

import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

    @Test
    public void encodeAndDecodeTest() {
        String a = "伦家是一个非常长的字符串";
        String encode = Base64.encode(a);
        Assert.assertEquals("5Lym5a625piv5LiA5Liq6Z2e5bi46ZW/55qE5a2X56ym5Liy", encode);

        String decodeStr = Base64.decodeStr(encode);
        Assert.assertEquals(a, decodeStr);
    }
}
