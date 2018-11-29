package org.gjgr.pig.chivalrous.core.convert.impl;

import org.gjgr.pig.chivalrous.core.convert.AbstractConverter;
import org.gjgr.pig.chivalrous.core.date.DateTimeCommand;
import org.gjgr.pig.chivalrous.core.util.StrUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期转换器
 *
 * @author Looly
 */
public class CalendarConverter extends AbstractConverter<Calendar> {

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
    protected Calendar convertInternal(Object value) {
        // Handle Date
        if (value instanceof Date) {
            return DateTimeCommand.calendar((Date) value);
        }

        // Handle Long
        if (value instanceof Long) {
            //此处使用自动拆装箱
            return DateTimeCommand.calendar((Long) value);
        }

        final String valueStr = convertToStr(value);
        return DateTimeCommand.calendar(StrUtil.isBlank(format) ? DateTimeCommand.parse(valueStr) : DateTimeCommand.parse(valueStr, format));
    }

}
