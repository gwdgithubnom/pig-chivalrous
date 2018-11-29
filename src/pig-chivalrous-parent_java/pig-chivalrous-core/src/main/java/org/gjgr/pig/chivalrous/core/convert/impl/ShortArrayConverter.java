package org.gjgr.pig.chivalrous.core.convert.impl;

import org.gjgr.pig.chivalrous.core.convert.AbstractConverter;
import org.gjgr.pig.chivalrous.core.convert.ConverterRegistry;
import org.gjgr.pig.chivalrous.core.util.ArrayUtil;

/**
 * short 类型数组转换器
 *
 * @author Looly
 */
public class ShortArrayConverter extends AbstractConverter<short[]> {

    @Override
    protected short[] convertInternal(Object value) {
        final Short[] result = ConverterRegistry.getInstance().convert(Short[].class, value);
        return ArrayUtil.unWrap(result);
    }

}
