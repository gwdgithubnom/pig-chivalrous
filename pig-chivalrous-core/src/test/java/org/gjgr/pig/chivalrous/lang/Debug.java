package org.gjgr.pig.chivalrous.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author gwd
 * @Time 12-04-2019  Wednesday
 * @Description: gjgrparent:
 * @Target:
 * @More:
 */
public class Debug {

    public List<String> testMethod(Integer i, String j, String s) {
        System.out.println("test1 " + i + " " + j + " " + s + "");
        return new ArrayList();
    }

    public List<String> testMethod(int i, String j, String s) {
        System.out.println("test2 " + i + " " + j + " " + s + "");
        return new ArrayList();
    }

    public List<String> testMethod(long i, int j, String s) {
        System.out.println("test3 " + i + " " + j + " " + s + "");
        return new ArrayList();
    }

    public Map<String, String> testMethod(Integer j, String a) {
        System.out.println("test4 " + j + " " + a + "");
        return new HashMap();
    }
}
