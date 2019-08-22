package org.gjgr.pig.chivalrous.core.convert.impl;

import org.gjgr.pig.chivalrous.core.convert.AbstractConverter;
import org.gjgr.pig.chivalrous.core.lang.ClassCommand;

/**
 * 类转换器<br>
 * 将类名转换为类
 *
 * @author Looly
 */
public class ClassConverter extends AbstractConverter<Class<?>> {

    @Override
    protected Class<?> convertInternal(Object value) {
        String valueStr = convertToStr(value);
        try {
            return ClassCommand.getClassLoader().loadClass(valueStr);
        } catch (Exception e) {
            // Ignore Exception
        }
        return null;
    }

}
