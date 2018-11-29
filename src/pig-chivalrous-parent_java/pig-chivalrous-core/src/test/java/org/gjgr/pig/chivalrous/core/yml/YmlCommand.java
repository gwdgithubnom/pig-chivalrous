package org.gjgr.pig.chivalrous.core.yml;

import org.gjgr.pig.chivalrous.core.file.YmlNode;
import org.gjgr.pig.chivalrous.core.io.FileCommand;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

/**
 * @Author gwd
 * @Time 09-28-2018  Friday
 * @Description: developer.tools:
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
            YmlNode root = org.gjgr.pig.chivalrous.core.file.YmlCommand.getYmlNode("application.yml");

            System.out.println(root.get("pipeline0").get("push").get("country").value());

            System.out.println(oo.getClass().getName());
            System.out.println(oo.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
