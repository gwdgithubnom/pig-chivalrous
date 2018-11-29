package org.gjgr.pig.chivalrous.core.io;

import org.gjgr.pig.chivalrous.core.io.resource.ClassPathResource;
import org.gjgr.pig.chivalrous.core.lang.Console;

import java.io.IOException;
import java.util.Properties;

/**
 * ClassPath资源读取测试
 *
 * @author Looly
 */
public class ClassPathResourceTest {

    public void readTest() throws IOException {
        ClassPathResource resource = new ClassPathResource("test.properties");
        Properties properties = new Properties();
        properties.load(resource.getStream());

        Console.log("Properties: {}", properties);
    }
}
