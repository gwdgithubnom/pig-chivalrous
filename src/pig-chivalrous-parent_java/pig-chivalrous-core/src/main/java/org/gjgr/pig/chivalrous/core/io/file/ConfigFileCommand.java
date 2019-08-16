package org.gjgr.pig.chivalrous.core.io.file;

import org.gjgr.pig.chivalrous.core.io.file.yml.YmlCommand;
import org.gjgr.pig.chivalrous.core.io.file.yml.YmlNode;
import org.gjgr.pig.chivalrous.core.io.resource.LocationCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @Author gwd
 * @Time 11-08-2018 Thursday
 * @Description: micsql:
 * @Target:
 * @More:
 */
public class ConfigFileCommand {

    private static Logger logger = LoggerFactory.getLogger(ConfigFileCommand.class);
    private static String rootPath = null;

    public static String getRootPath() {
        return rootPath;
    }

    public static void setRootPath(String rootPath) {
        ConfigFileCommand.rootPath = rootPath;
    }

    public static InputStream tryUserSystemExternal(String filename) {
        InputStream inputStream;
        if (System.getenv(filename) != null) {
            if (FileCommand.isExist(System.getenv(filename))) {
                try {
                    inputStream = FileCommand
                            .inputStream(System.getenv(filename));
                } catch (RuntimeException e) {
                    inputStream = null;
                    e.printStackTrace();
                }
            } else {
                inputStream = null;
            }
        } else {
            inputStream = null;
        }
        if (inputStream == null) {
            if (FileCommand.isExist(System.getProperty(filename))) {
                try {
                    inputStream = FileCommand
                            .inputStream(System.getProperty(filename));
                } catch (RuntimeException e) {
                    inputStream = null;
                    e.printStackTrace();
                }
            } else {
                inputStream = null;
            }
        }
        return inputStream;
    }

    public static InputStream tryUserSystemDefault(String key, String filename) {
        InputStream inputStream;
        if (System.getProperty(key) != null) {
            if (FileCommand.isExist(System.getProperty(key) + File.separator + (filename))) {
                try {
                    inputStream = FileCommand
                            .inputStream(System.getProperty(key) + File.separator + (filename));
                } catch (RuntimeException e) {
                    inputStream = null;
                    e.printStackTrace();
                }
            } else {
                inputStream = null;
            }
        } else {
            inputStream = null;
        }
        if (inputStream == null) {
            if (System.getenv(key) != null) {
                if (FileCommand.isExist(System.getenv(key) + File.separator + (filename))) {
                    try {
                        inputStream = FileCommand
                                .inputStream(System.getenv(key) + File.separator + (filename));
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        inputStream = null;
                    }
                } else {
                    inputStream = null;
                }
            } else {
                inputStream = null;
            }
        }
        return inputStream;
    }

    public static InputStream getResource(String filename) {
        InputStream inputStream;
        if (FileCommand.isExist(filename)) {
            try {
                inputStream = FileCommand.inputStream(filename);
            } catch (RuntimeException e) {
                e.printStackTrace();
                inputStream = null;
            }
        } else {
            inputStream = tryUserSystemExternal(filename);
        }
        if (inputStream == null) {
            try {
                inputStream = FileCommand.inputStream(LocationCommand.pathValue(filename));
            } catch (RuntimeException e) {
                try {
                    inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(filename);
                } catch (Exception eeeee) {

                } finally {
                    if (inputStream == null) {
                        try {
                            inputStream = FileCommand.inputStream(new File(System.getProperty(filename)));
                        } catch (Exception eeeee) {
                            inputStream = tryUserSystemDefault("user.dir", filename);
                            if (inputStream == null) {
                                inputStream = tryUserSystemDefault("basedir", filename);
                            }
                            if (inputStream == null) {
                                logger.warn("should define the {} file location.", filename);
                            }
                        }
                    }
                }
            }
        }
        return inputStream;
    }

    public static YmlNode getYmlNode(String filename) {
        YmlNode ymlNode = null;
        InputStream inputStream = getResource(filename);
        try {
            if (inputStream != null) {
                ymlNode = YmlCommand.getYmlNode(inputStream);
            } else {
                ymlNode = null;
            }
        } catch (Exception e) {
            logger.error("should define the {} file location. excpetion:{}", filename, e.getLocalizedMessage());
        }
        return ymlNode;
    }

    public static List<String> scanResource(String rootPath) {
        List<String> result = new ArrayList<>();
        try {
            result = scanResource(ConfigFileCommand.class, rootPath);
        } catch (Exception e) {
            logger.warn("scan directory stop, cause by a exception.");
            e.printStackTrace();
            return result;
        }
        return result;
    }

    public static List<String> scanResource(Class clazz, String rootPath) {
        String path = null;
        logger.debug("check rootPath:{}, with base dir env:{}", rootPath, System.getProperty("basedir"));
        if (rootPath != null) {
            if (!FileCommand.isDirectory(rootPath)) {
                path = LocationCommand.classPath() + File.separator + rootPath;
                if (!FileCommand.isDirectory(path)) {
                    path = LocationCommand.classPath() + File.separator + rootPath;
                    if (!FileCommand.isDirectory(path)) {
                        path = LocationCommand.classPath(clazz) + File.separator + rootPath;
                        if (!FileCommand.isDirectory(path)) {
                            path = System.getProperty("basedir") == null ? ""
                                    : System.getProperty("basedir") + File.separator + rootPath;
                        }
                    }
                }
            } else {
                path = rootPath;
            }
        } else {
            path = LocationCommand.classPath() + File.separator + "config";
            if (!FileCommand.isDirectory(path)) {
                path = LocationCommand.classPath() + File.separator + "config";
            }
        }
        List<String> xmlPaths = new Vector<>();
        try {
            File[] files = FileCommand.lsFile(path);
            if (files != null) {
                for (int i = 0; files != null && i < files.length; i++) {
                    File f = files[i];
                    xmlPaths.add(f.getAbsolutePath());
                    logger.debug("found xml path in root directory {}.", f.getAbsolutePath());
                }
            }
            try {
                File[] root = FileCommand.dir(path);
                if (root != null) {
                    for (File f : root) {
                        File[] temp = FileCommand.lsFile(f.getAbsolutePath());
                        logger.debug("check xml config in directory {}", f.getAbsolutePath());
                        for (File t : temp) {
                            logger.debug("found xml path in directory {}, and add xml {}", f.getAbsoluteFile(),
                                    t.getAbsolutePath());
                            xmlPaths.add(t.getAbsolutePath());
                        }
                    }
                }
            } catch (Exception e) {
                logger.debug("did not contain any sub directory in path:{}", path);
            }
        } catch (Exception e) {
            logger.debug("did not contain any files in path:{}", path);
        }
        return xmlPaths;
    }
}
