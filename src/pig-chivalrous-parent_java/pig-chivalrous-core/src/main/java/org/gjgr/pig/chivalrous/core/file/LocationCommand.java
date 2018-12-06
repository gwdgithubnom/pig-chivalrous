/**
 * Copyright (c) 2011-2015, James Zhan 詹波 (jfinal@126.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gjgr.pig.chivalrous.core.file;

import org.gjgr.pig.chivalrous.core.io.FileCommand;

import java.io.File;
import java.net.URI;
import java.net.URL;

/**
 * new File("..\path\abc.txt") 中的三个方法获取路径的方法
 * 1： getPath() 获取相对路径，例如   ..\path\abc.txt
 * 2： getAbslutlyPath() 获取绝对路径，但可能包含 ".." 或 "." 字符，例如  D:\otherPath\..\path\abc.txt
 * 3： getCanonicalPath() 获取绝对路径，但不包含 ".." 或 "." 字符，例如  D:\path\abc.txt
 */
public class LocationCommand {

    private static String webRootPath;
    private static String rootClassPath;

    public static String pathValue(Class clazz, String path) {
        String check = pathValue(path);
        if (!FileCommand.isExist(check)) {
            check = LocationCommand.getRootClassPath(clazz) + File.separator + path;
        }
        return check;
    }

    public static String pathValue(String path) {
        String check = path;
        if (!FileCommand.isExist(check)) {
            check = LocationCommand.getRootPath() + File.separator + path;
            if (!FileCommand.isExist(check)) {
                check = LocationCommand.getRootClassPath() + File.separator + path;
                if (!FileCommand.isExist(check)) {
                    check = System.getProperty("basedir") == null ? ""
                            : System.getProperty("basedir") + File.separator + path;
                }
            }
        }
        return check;
    }

    @SuppressWarnings("rawtypes")
    public static String getPath(Class clazz) {
        String path = clazz.getResource("").getPath();
        return new File(path).getAbsolutePath();
    }

    public static String getPath(Object object) {
        String path = object.getClass().getResource("").getPath();
        return new File(path).getAbsolutePath();
    }

    public static String getRootClassPath(Class clazz) {
        String path = null;
        try {
            URL url = clazz.getResource("");
            URI uri = url.toURI();
            path = clazz.getResource("").getPath();

            if (path.contains(".jar")) {
                path = path.substring(path.indexOf(":/"), path.lastIndexOf("!/"));
            } else {
                path = getRootPath();
            }
            path = new File(path).getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    public static String getRootPath() {
        return System.getProperty("user.dir");
    }

    public static String getRootClassPath() {
        if (rootClassPath == null) {
            try {
                String path = null;
                URL url = LocationCommand.class.getClassLoader().getResource("");
                if (url == null) {
                    if (System.getProperty("java.class.path").contains(".jar")) {
                        path = System.getProperty("java.class.path");
                    } else {
                        path = getRootPath();
                    }
                } else {
                    path = url.getPath();
                }
                rootClassPath = new File(path).getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rootClassPath;
    }

    public static String getPackagePath(Object object) {
        Package p = object.getClass().getPackage();
        return p != null ? p.getName().replaceAll("\\.", "/") : "";
    }

    public static File getFileFromJar(String file) {
        throw new RuntimeException("Not finish. Do not use this method.");
    }

    public static String getWebRootPath() {
        if (webRootPath == null) {
            webRootPath = detectWebRootPath();
        }
        ;
        return webRootPath;
    }

    public static void setWebRootPath(String webRootPath) {
        if (webRootPath == null) {
            return;
        }

        if (webRootPath.endsWith(File.separator)) {
            webRootPath = webRootPath.substring(0, webRootPath.length() - 1);
        }
        LocationCommand.webRootPath = webRootPath;
    }

    private static String detectWebRootPath() {
        try {
            String path = LocationCommand.class.getResource("/").toURI().getPath();
            return new File(path).getParentFile().getParentFile().getCanonicalPath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}


