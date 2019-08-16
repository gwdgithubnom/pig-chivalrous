package org.gjgr.pig.chivalrous.linux;

import org.gjgr.pig.chivalrous.core.linux.WgetCommand;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @Author gwd
 * @Time 11-29-2018 Thursday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public class TestLinuxCommand {
    @Ignore
    @Test
    public void testWget() {
        WgetCommand wgetCommand = new WgetCommand();
        wgetCommand.wget("www.baidu.com", "a.html").command();
    }
}
