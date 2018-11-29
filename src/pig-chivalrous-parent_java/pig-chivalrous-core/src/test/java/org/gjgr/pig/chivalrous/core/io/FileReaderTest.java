package org.gjgr.pig.chivalrous.core.io;

import org.gjgr.pig.chivalrous.core.io.file.FileReader;
import org.junit.Assert;

/**
 * 文件读取测试
 *
 * @author Looly
 */
public class FileReaderTest {

    public void fileReaderTest() {
        FileReader fileReader = new FileReader("test.properties");
        String result = fileReader.readString();
        Assert.assertNotNull(result);
    }
}
