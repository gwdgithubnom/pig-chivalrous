package org.gjgr.pig.chivalrous.core.system;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.gjgr.pig.chivalrous.core.io.resource.LocationCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemCommand {

    private static final long serialVersionUID = 6761767368352810428L;

    private static final String webRootPath = LocationCommand.getWebRootPath();
    private static final String rootClassPath = LocationCommand.getWebRootPath();

    private static final String xmlDataPath = webRootPath + "\\WEB-INF\\data\\xml\\";
    private static final String xmlDataBackupPath = webRootPath + "\\WEB-INF\\data\\Backup";
    private static final String imagesDataPath = webRootPath + "\\WEB-INF\\data\\images\\";
    private static final String videoDataPath = webRootPath + "\\WEB-INF\\data\\videos\\";
    private static final String dataPath = webRootPath + "\\WEB-INF\\data\\";
    private static Logger log = LoggerFactory.getLogger(SystemCommand.class);

    /**
     * @return the datapath
     */
    public static String getDatapath() {
        return dataPath;
    }

    /**
     * @return the web rootpath
     */
    public static String getWebrootpath() {
        return webRootPath;
    }

    /**
     * @return the root classpath
     */
    public static String getRootclasspath() {
        return rootClassPath;
    }

    /**
     * @return the xmldatapath
     */
    public static String getXmldatapath() {
        return xmlDataPath;
    }

    /**
     * @return the xmldatabackuppath
     */
    public static String getXmldatabackuppath() {
        return xmlDataBackupPath;
    }

    /**
     * @return the imagesdatapath
     */
    public static String getImagesdatapath() {
        return imagesDataPath;
    }

    /**
     * @return the videodatapath
     */
    public static String getVideodatapath() {
        return videoDataPath;
    }

    /**
     * @return the webRootPath
     */
    public static String getWebRootPath() {
        return webRootPath;
    }

    /**
     * @return the rootClassPath
     */
    public static String getRootClassPath() {
        return rootClassPath;
    }

    /**
     * @return the xmldatapath
     */
    public static String getXmlDataPath() {
        return xmlDataPath;
    }

    public static String getXmlDataBackupPath() {
        return xmlDataBackupPath;
    }

    private boolean executeLinuxCommand(String command) {
        String s;
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null) {
                System.out.println(s);
            }
            p.waitFor();
            System.out.println("run command:" + command + ". exitValue:" + p.exitValue() + "");
            p.destroy();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        return true;
    }

}
