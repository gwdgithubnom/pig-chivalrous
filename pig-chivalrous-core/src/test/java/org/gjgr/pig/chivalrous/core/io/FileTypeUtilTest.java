package org.gjgr.pig.chivalrous.core.io;

import org.gjgr.pig.chivalrous.core.io.file.FileTypeUtil;
import org.gjgr.pig.chivalrous.core.io.file.FileUtil;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;

/**
 * 文件类型判断单元测试
 *
 * @author Looly
 */
public class FileTypeUtilTest {

    public void fileTypeUtilTest() throws IOException {
        File file = FileUtil.file("hutool.jpg");
        String type = FileTypeUtil.getType(file);
        Assert.assertEquals("jpg", type);

        FileTypeUtil.putFileType("ffd8ffe000104a464946", "new_jpg");
        String newType = FileTypeUtil.getType(file);
        Assert.assertEquals("new_jpg", newType);
    }
}
