package org.gjgr.pig.chivalrous.core.util;

import org.gjgr.pig.chivalrous.core.lang.Console;
import org.gjgr.pig.chivalrous.core.math.HexCommand;
import org.gjgr.pig.chivalrous.core.nio.CharsetCommand;
import org.junit.Assert;
import org.junit.Test;

/**
 * HexUtil单元测试
 *
 * @author Looly
 */
public class HexUtilTest {

    @Test
    public void hexStrTest() {
        String str = "我是一个字符串";

        String hex = HexCommand.encodeHexStr(str, CharsetCommand.CHARSET_UTF_8);
        Console.log(hex);

        String decodedStr = HexCommand.decodeHexStr(hex);

        Assert.assertEquals(str, decodedStr);
    }
}
