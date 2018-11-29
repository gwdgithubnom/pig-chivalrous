package org.gjgr.pig.chivalrous.core.convert.impl;

import org.gjgr.pig.chivalrous.core.convert.AbstractConverter;
import org.gjgr.pig.chivalrous.core.convert.ConverterRegistry;
import org.gjgr.pig.chivalrous.core.util.ArrayUtil;

/**
 * int 类型数组转换器
 *
 * @author Looly
 */
public class IntArrayConverter extends AbstractConverter<int[]> {

    @Override
    protected int[] convertInternal(Object value) {
        final Integer[] result = ConverterRegistry.getInstance().convert(Integer[].class, value);
        return ArrayUtil.unWrap(result);
    }

}
