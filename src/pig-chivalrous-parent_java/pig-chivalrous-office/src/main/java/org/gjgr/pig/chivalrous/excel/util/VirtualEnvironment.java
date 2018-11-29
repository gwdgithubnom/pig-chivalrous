package org.gjgr.pig.chivalrous.excel.util;

/**
 * Created by gwd on 2016/5/23.
 */
public class VirtualEnvironment {
    /**
     * This is the defulat of the path. eg:bin/>>>>
     */
    public static String System_Path = VirtualEnvironment.class.getResource("/").getPath().substring(1);

}
