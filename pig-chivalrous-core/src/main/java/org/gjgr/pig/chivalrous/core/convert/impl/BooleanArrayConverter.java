package org.gjgr.pig.chivalrous.core.convert.impl;

import org.gjgr.pig.chivalrous.core.convert.AbstractConverter;
import org.gjgr.pig.chivalrous.core.convert.ConverterRegistry;
import org.gjgr.pig.chivalrous.core.lang.ArrayCommand;

/**
 * boolean类型数组转换器
 *
 * @author Looly
 */
public class BooleanArrayConverter extends AbstractConverter<boolean[]> {

    @Override
    protected boolean[] convertInternal(Object value) {
        final Boolean[] result = ConverterRegistry.getInstance().convert(Boolean[].class, value);
        return ArrayCommand.unWrap(result);
    }

}
