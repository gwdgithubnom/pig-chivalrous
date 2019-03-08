package org.gjgr.pig.chivalrous.core.convert.impl;

import org.gjgr.pig.chivalrous.core.convert.AbstractConverter;
import org.gjgr.pig.chivalrous.core.convert.ConverterRegistry;
import org.gjgr.pig.chivalrous.core.lang.ArrayCommand;

/**
 * float 类型数组转换器
 *
 * @author Looly
 */
public class FloatArrayConverter extends AbstractConverter<float[]> {

    @Override
    protected float[] convertInternal(Object value) {
        final Float[] result = ConverterRegistry.getInstance().convert(Float[].class, value);
        return ArrayCommand.unWrap(result);
    }

}
