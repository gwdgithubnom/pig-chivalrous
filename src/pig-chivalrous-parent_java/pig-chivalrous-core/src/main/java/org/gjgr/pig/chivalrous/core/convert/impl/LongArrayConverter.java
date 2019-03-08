package org.gjgr.pig.chivalrous.core.convert.impl;

import org.gjgr.pig.chivalrous.core.convert.AbstractConverter;
import org.gjgr.pig.chivalrous.core.convert.ConverterRegistry;
import org.gjgr.pig.chivalrous.core.lang.ArrayCommand;

/**
 * long 类型数组转换器
 *
 * @author Looly
 */
public class LongArrayConverter extends AbstractConverter<long[]> {

    @Override
    protected long[] convertInternal(Object value) {
        final Long[] result = ConverterRegistry.getInstance().convert(Long[].class, value);
        return ArrayCommand.unWrap(result);
    }

}
