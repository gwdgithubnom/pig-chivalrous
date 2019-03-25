package org.gjgr.pig.chivalrous.core.date;

import java.util.Calendar;
import java.util.Date;

import org.gjgr.pig.chivalrous.core.date.BetweenFormater.Level;
import org.junit.Assert;
import org.junit.Test;

public class DateUtilTest {

    @Test
    public void nowTest() {
        // 当前时间
        Date date = DateTimeCommand.dateTime();
        Assert.assertNotNull(date);
        // 当前时间
        Date date2 = DateTimeCommand.dateTime(Calendar.getInstance());
        Assert.assertNotNull(date2);
        // 当前时间
        Date date3 = DateTimeCommand.dateTime(System.currentTimeMillis());
        Assert.assertNotNull(date3);

        // 当前日期字符串，格式：yyyy-MM-dd HH:mm:ss
        String now = DateTimeCommand.now();
        Assert.assertNotNull(now);
        // 当前日期字符串，格式：yyyy-MM-dd
        String today = DateTimeCommand.today();

        Assert.assertNotNull(today);

    }

    @Test
    public void formatAndParseTest() {
        String dateStr = "2017-03-01";
        Date date = DateTimeCommand.parse(dateStr);

        String format = DateTimeCommand.format(date, "yyyy/MM/dd");
        Assert.assertEquals("2017/03/01", format);

        // 常用格式的格式化
        String formatDate = DateTimeCommand.formatDate(date);
        Assert.assertEquals("2017-03-01", formatDate);
        String formatDateTime = DateTimeCommand.formatDateTime(date);
        Assert.assertEquals("2017-03-01 00:00:00", formatDateTime);
        String formatTime = DateTimeCommand.formatTime(date);
        Assert.assertEquals("00:00:00", formatTime);

    }

    @Test
    public void beginAndEndTest() {
        String dateStr = "2017-03-01 22:33:23";
        Date date = DateTimeCommand.parse(dateStr);

        // 一天的开始
        Date beginOfDay = DateTimeCommand.beginOfDay(date);
        Assert.assertEquals("2017-03-01 00:00:00", beginOfDay.toString());
        // 一天的结束
        Date endOfDay = DateTimeCommand.endOfDateTime(date);
        Assert.assertEquals("2017-03-01 23:59:59", endOfDay.toString());

    }

    @Test
    public void offsetDateTest() {
        String dateStr = "2017-03-01 22:33:23";
        Date date = DateTimeCommand.parse(dateStr);

        Date newDate = DateTimeCommand.offsetOfDateTime(date, DateField.DAY_OF_MONTH, 2);
        Assert.assertEquals("2017-03-03 22:33:23", newDate.toString());

        // 常用偏移
        DateTime newDate2 = DateTimeCommand.offsetDayOfDateTime(date, 3);
        Assert.assertEquals("2017-03-04 22:33:23", newDate2.toString());
        // 常用偏移
        DateTime newDate3 = DateTimeCommand.offsetHourOfDateTime(date, -3);
        Assert.assertEquals("2017-03-01 19:33:23", newDate3.toString());
    }

    @Test
    public void betweenTest() {
        String dateStr1 = "2017-03-01 22:34:23";
        Date date1 = DateTimeCommand.parse(dateStr1);

        String dateStr2 = "2017-04-01 23:56:14";
        Date date2 = DateTimeCommand.parse(dateStr2);

        long betweenDay = DateTimeCommand.between(date1, date2, DateUnit.DAY);
        Assert.assertEquals(31, betweenDay);// 相差一个月，31天

        long between = DateTimeCommand.between(date1, date2, DateUnit.MS);
        String formatBetween = DateTimeCommand.formatBetween(between, Level.MINUTE);
        Assert.assertEquals("31天1小时21分", formatBetween);
    }

    @Test
    public void timerTest() {
        TimeInterval timer = DateTimeCommand.timer();

        // ---------------------------------
        // -------这是执行过程
        // ---------------------------------

        timer.interval();// 花费毫秒数
        timer.intervalRestart();// 返回花费时间，并重置开始时间
        timer.intervalMinute();// 花费分钟数
    }
}
