package org.gjgr.pig.chivalrous.core.io;

import org.gjgr.pig.chivalrous.core.io.file.FileCommand;
import org.gjgr.pig.chivalrous.core.net.UriCommand;
import org.junit.Test;

/**
 * @Author gwd
 * @Time 07-10-2019  Wednesday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public class FileComandDev {

    @Test
    public void testGetPath() {
        String url = "/media/gwd/UBUNTU/home/gwd/Projects/Github/pig/pig-chivalrous/src/pig-chivalrous-parent_java/pig-chivalrous-core/target/test-classes/application.yml";
        System.out.println(UriCommand.decode(url));
        FileCommand.readUtf8String("application.yml");
    }
}
