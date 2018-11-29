package org.gjgr.pig.chivalrous.core.convert.impl;

import org.gjgr.pig.chivalrous.core.convert.AbstractConverter;
import org.gjgr.pig.chivalrous.core.convert.ConverterRegistry;
import org.gjgr.pig.chivalrous.core.util.ArrayUtil;

/**
 * byte 类型数组转换器
 *
 * @author Looly
 */
public class ByteArrayConverter extends AbstractConverter<byte[]> {

    @Override
    protected byte[] convertInternal(Object value) {
        final Byte[] result = ConverterRegistry.getInstance().convert(Byte[].class, value);
        return ArrayUtil.unWrap(result);
    }

}
