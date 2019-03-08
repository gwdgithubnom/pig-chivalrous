package org.gjgr.pig.chivalrous.core.cron.pattern.matcher;

import org.gjgr.pig.chivalrous.core.lang.StringCommand;

/**
 * 值匹配，始终返回<code>true</code>
 *
 * @author Looly
 */
public class AlwaysTrueValueMatcher implements ValueMatcher {

    @Override
    public boolean match(Integer t) {
        return true;
    }

    @Override
    public String toString() {
        return StringCommand.format("[Matcher]: always true.");
    }
}
