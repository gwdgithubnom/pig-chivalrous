package org.gjgr.pig.chivalrous.core.cron.pattern.parser;

/**
 * 年值处理
 *
 * @author Looly
 */
public class YearValueParser extends SimpleValueParser {

    public YearValueParser() {
        super(1970, 2099);
    }

}
