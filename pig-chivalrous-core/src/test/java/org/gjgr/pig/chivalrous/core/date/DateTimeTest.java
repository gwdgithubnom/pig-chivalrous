package org.gjgr.pig.chivalrous.core.date;

import org.junit.Assert;
import org.junit.Test;

public class DateTimeTest {

    @Test
    public void datetimeTest() {
        DateTime dateTime = new DateTime("2017-01-05 12:34:23", DateStyle.YYYY_MM_DD_HH_MM_SS.getValue());

        // 年
        int year = dateTime.year();
        Assert.assertEquals(2017, year);

        // 季度（非季节）
        PigSeason pigSeason = dateTime.seasonEnum();
        Assert.assertEquals(PigSeason.SPRING, pigSeason);

        // 月份
        PigMonth month = dateTime.monthEnum();
        Assert.assertEquals(PigMonth.JANUARY, month);

        // 日
        int day = dateTime.dayOfMonth();
        Assert.assertEquals(5, day);
    }

    @Test
    public void mutableTest() {
        DateTime dateTime = new DateTime("2017-01-05 12:34:23", DateStyle.YYYY_MM_DD_HH_MM_SS.getValue());

        // 默认情况下DateTime为可变对象
        DateTime offsite = dateTime.offsite(DateField.YEAR, 0);
        Assert.assertTrue(offsite == dateTime);

        // 设置为不可变对象后变动将返回新对象
        dateTime.setMutable(false);
        offsite = dateTime.offsite(DateField.YEAR, 0);
        Assert.assertFalse(offsite == dateTime);
    }

    @Test
    public void toStringTest() {
        DateTime dateTime = new DateTime("2017-01-05 12:34:23", DateStyle.YYYY_MM_DD_HH_MM_SS.getValue());
        Assert.assertEquals("2017-01-05 12:34:23", dateTime.toString());

        String dateStr = dateTime.toString("yyyy/MM/dd");
        Assert.assertEquals("2017/01/05", dateStr);
    }
}
