package org.gjgr.pig.chivalrous.core.convert.impl;

import org.gjgr.pig.chivalrous.core.convert.AbstractConverter;
import org.gjgr.pig.chivalrous.core.convert.ConverterRegistry;
import org.gjgr.pig.chivalrous.core.util.ArrayUtil;

/**
 * char类型数组转换器
 *
 * @author Looly
 */
public class CharArrayConverter extends AbstractConverter<char[]> {

    @Override
    protected char[] convertInternal(Object value) {
        final Character[] result = ConverterRegistry.getInstance().convert(Character[].class, value);
        return ArrayUtil.unWrap(result);
    }

}
