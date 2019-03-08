package org.gjgr.pig.chivalrous.core.util;

import org.gjgr.pig.chivalrous.core.math.NumberCommand;
import org.junit.Assert;
import org.junit.Test;

/**
 * {@link NumberCommand} 单元测试类
 *
 * @author Looly
 */
public class NumberUtilTest {

    @Test
    public void roundTest() {

        // 四舍
        double round3 = NumberCommand.round(2.674, 2);
        double round4 = NumberCommand.round("2.674", 2);
        Assert.assertEquals(round3, 2.67, 0);
        Assert.assertEquals(round4, 2.67, 0);

        // 五入
        double round1 = NumberCommand.round(2.675, 2);
        double round2 = NumberCommand.round("2.675", 2);
        Assert.assertEquals(round1, 2.68, 0);
        Assert.assertEquals(round2, 2.68, 0);
    }

    @Test
    public void roundStrTest() {
        String roundStr = NumberCommand.roundStr(2.647, 2);
        Assert.assertEquals(roundStr, "2.65");
    }

    @Test
    public void decimalFormatTest() {
        long c = 299792458;// 光速

        String format = NumberCommand.decimalFormat(",###", c);
        Assert.assertEquals("299,792,458", format);
    }
}
