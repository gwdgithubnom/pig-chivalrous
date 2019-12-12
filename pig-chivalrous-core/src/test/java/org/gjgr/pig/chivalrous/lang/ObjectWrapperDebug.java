package org.gjgr.pig.chivalrous.lang;

import java.util.List;
import org.gjgr.pig.chivalrous.core.lang.ObjectWrapper;
import org.gjgr.pig.chivalrous.core.lang.ReturnCatchCommand;
import org.junit.Test;

/**
 * @Author gwd
 * @Time 12-04-2019  Wednesday
 * @Description: gjgrparent:
 * @Target:
 * @More:
 */
public class ObjectWrapperDebug {
    Debug debug = new Debug();

    @Test
    public void test() {
        System.out.println(new ObjectWrapper<List>() {
        }.wrapperNotNull(null).size());
//        ObjectWrapper<HashMap> test = new ObjectWrapper<HashMap>();
//        System.out.println(test.wrapperNotNull(null).size());
//        System.out.println(new ObjectWrapper<HashMap>().wrapperNotNull(null).size());
        System.out.println(testMethod(1, "s", "a").toString());
    }

    public Object testMethod(int i, String j, String s) {
        return ReturnCatchCommand.autoSwithReturnWithDefualt(3, debug, new ObjectWrapper<List>() {
        }, 1, j, s);
    }

//    public Object testMethod(long i, String j, String s) {
//        return ReturnCatchCommand.smartAutoSwithReturnWithDefualt(3, debug, new ObjectWrapper<List>() {
//        }, new int[1], new boolean[1], i);
//    }
}
