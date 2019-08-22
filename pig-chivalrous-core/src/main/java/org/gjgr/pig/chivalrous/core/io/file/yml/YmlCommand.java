package org.gjgr.pig.chivalrous.core.io.file.yml;

import org.gjgr.pig.chivalrous.core.io.file.FileCommand;
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

    /**
     * from the specific yml file location, to transfer to Map object.
     *
     * @param ymlLocation
     * @return
     */
    public static Map getMap(String ymlLocation) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(FileCommand.file(ymlLocation));
            Yaml yaml = new Yaml();
            Object oo = yaml.load(inputStream);
            if (oo instanceof Map) {
                return (Map) oo;
            } else {
                throw new ClassCastException();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * return the YmlNode, so that could easy get the value.
     *
     * @param ymlLocation
     * @return
     */
    public static YmlNode getYmlNode(String ymlLocation) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(FileCommand.file(ymlLocation));
            return getYmlNode(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static YmlNode getYmlNode(InputStream inputStream) {
        Yaml yaml = new Yaml();
        Object oo = null;
        try {
            oo = yaml.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            oo = null;
        }
        return new YmlNode().setObject(oo);
    }

}
