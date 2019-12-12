package org.gjgr.pig.chivalrous.lang;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.junit.Test;

/**
 * @Author gwd
 * @Time 12-05-2019  Thursday
 * @Description: gjgrparent:
 * @Target:
 * @More:
 */
public class ParamTypeDebug {
    @Test
    public void test(){
        Debug debug = new Debug();
        Method[] deubugMethod = Debug.class.getMethods();
        System.out.println(StringCommand.join("\n",deubugMethod));
    }
}
