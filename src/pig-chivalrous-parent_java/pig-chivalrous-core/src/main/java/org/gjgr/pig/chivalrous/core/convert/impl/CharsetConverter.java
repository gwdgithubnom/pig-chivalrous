package org.gjgr.pig.chivalrous.core.convert.impl;

import java.nio.charset.Charset;

import org.gjgr.pig.chivalrous.core.convert.AbstractConverter;
import org.gjgr.pig.chivalrous.core.nio.CharsetCommand;

/**
 * 编码对象转换器
 *
 * @author Looly
 */
public class CharsetConverter extends AbstractConverter<Charset> {

    @Override
    protected Charset convertInternal(Object value) {
        return CharsetCommand.charset(convertToStr(value));
    }

}
