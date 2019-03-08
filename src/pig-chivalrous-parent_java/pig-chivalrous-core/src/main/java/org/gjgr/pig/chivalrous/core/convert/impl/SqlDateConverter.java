package org.gjgr.pig.chivalrous.core.convert.impl;

import java.sql.Date;
import java.util.Calendar;

import org.gjgr.pig.chivalrous.core.convert.AbstractConverter;
import org.gjgr.pig.chivalrous.core.date.DateTimeCommand;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;

/**
 * SQL日期转换器
 *
 * @author Looly
 */
public class SqlDateConverter extends AbstractConverter<Date> {

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
    protected Date convertInternal(Object value) {
        // Handle Calendar
        if (value instanceof Calendar) {
            return new Date(((Calendar) value).getTime().getTime());
        }

        // Handle Long
        if (value instanceof Long) {
            // 此处使用自动拆装箱
            return new Date((Long) value);
        }

        final String valueStr = convertToStr(value);
        try {
            final long date = StringCommand.isBlank(format) ? DateTimeCommand.parse(valueStr).getTime()
                    : DateTimeCommand.parse(valueStr, format).getTime();
            return new Date(date);
        } catch (Exception e) {
            // Ignore Exception
        }
        return null;
    }

}
