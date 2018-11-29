package org.gjgr.pig.chivalrous.core.convert.impl;

import org.gjgr.pig.chivalrous.core.convert.AbstractConverter;
import org.gjgr.pig.chivalrous.core.date.DateTime;
import org.gjgr.pig.chivalrous.core.date.DateTimeCommand;
import org.gjgr.pig.chivalrous.core.util.StrUtil;

import java.util.Calendar;

/**
 * 日期时间转换器
 *
 * @author Looly
 */
public class DateTimeConverter extends AbstractConverter<DateTime> {

    /**
     * 日期格式化
     */
    private String format;

    /**
     * 获取日期格式
     *
     * @return 设置日期格式
     */
    public String getFormat() {
        return format;
    }

    /**
     * 设置日期格式
     *
     * @param format 日期格式
     */
    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    protected DateTime convertInternal(Object value) {
        // Handle Calendar
        if (value instanceof Calendar) {
            return new DateTime(((Calendar) value).getTime().getTime());
        }

        // Handle Long
        if (value instanceof Long) {
            //此处使用自动拆装箱
            return new DateTime((Long) value);
        }

        final String valueStr = convertToStr(value);
        try {
            final long date = StrUtil.isBlank(format) ? DateTimeCommand.parse(valueStr).getTime() : DateTimeCommand.parse(valueStr, format).getTime();
            return new DateTime(date);
        } catch (Exception e) {
            // Ignore Exception
        }
        return null;
    }

}
