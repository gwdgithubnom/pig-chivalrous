package org.gjgr.pig.chivalrous.core.convert.impl;

import org.gjgr.pig.chivalrous.core.convert.AbstractConverter;
import org.gjgr.pig.chivalrous.core.convert.ConverterRegistry;
import org.gjgr.pig.chivalrous.core.util.ArrayUtil;

/**
 * double 类型数组转换器
 *
 * @author Looly
 */
public class DoubleArrayConverter extends AbstractConverter<double[]> {

    @Override
    protected double[] convertInternal(Object value) {
        final Double[] result = ConverterRegistry.getInstance().convert(Double[].class, value);
        return ArrayUtil.unWrap(result);
    }

}
