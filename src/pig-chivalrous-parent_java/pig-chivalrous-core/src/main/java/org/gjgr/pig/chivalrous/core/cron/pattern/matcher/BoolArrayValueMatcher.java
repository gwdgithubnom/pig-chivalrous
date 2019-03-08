package org.gjgr.pig.chivalrous.core.cron.pattern.matcher;

import java.util.Collections;
import java.util.List;

import org.gjgr.pig.chivalrous.core.lang.StringCommand;

/**
 * 将表达式中的数字值列表转换为Boolean数组，匹配时匹配相应数组位
 *
 * @author Looly
 */
public class BoolArrayValueMatcher implements ValueMatcher {

    boolean[] bValues;

    public BoolArrayValueMatcher(List<Integer> intValueList) {
        bValues = new boolean[Collections.max(intValueList) + 1];
        for (Integer value : intValueList) {
            bValues[value] = true;
        }
    }

    @Override
    public boolean match(Integer value) {
        if (null == value || value >= bValues.length) {
            return false;
        }
        return bValues[value];
    }

    @Override
    public String toString() {
        return StringCommand.format("Matcher:{}", (Object) this.bValues);
    }
}
