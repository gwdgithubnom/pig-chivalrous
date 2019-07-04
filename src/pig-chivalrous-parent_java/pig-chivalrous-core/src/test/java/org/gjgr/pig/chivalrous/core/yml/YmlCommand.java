package org.gjgr.pig.chivalrous.core.yml;

import org.gjgr.pig.chivalrous.core.io.file.FileCommand;
import org.gjgr.pig.chivalrous.core.io.file.yml.YmlNode;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

/**
 * @Author gwd
 * @Time 09-28-2018 Friday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public class YmlCommand {
    @Test
    public void testYml() {
        try {
            InputStream inputStream = new FileInputStream(FileCommand.file("application.yml"));
            Yaml yaml = new Yaml();
            Object oo = yaml.load(inputStream);
            if (oo instanceof Map) {
                System.out.println("true");
            }
            YmlNode root = org.gjgr.pig.chivalrous.core.io.file.yml.YmlCommand.getYmlNode("application.yml");

            System.out.println(root.get("test").get("save").get("jsonArray").get("set").object().getClass());
            System.out.println(oo.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
