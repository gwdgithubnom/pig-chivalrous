package org.gjgr.pig.chivalrous.core.convert.impl;

import java.util.TimeZone;

import org.gjgr.pig.chivalrous.core.convert.AbstractConverter;

/**
 * TimeZone转换器
 *
 * @author Looly
 */
public class TimeZoneConverter extends AbstractConverter<TimeZone> {

    @Override
    protected TimeZone convertInternal(Object value) {
        return TimeZone.getTimeZone(convertToStr(value));
    }

}
