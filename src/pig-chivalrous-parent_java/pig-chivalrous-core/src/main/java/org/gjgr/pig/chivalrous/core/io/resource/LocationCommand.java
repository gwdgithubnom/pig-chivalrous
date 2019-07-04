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

package org.gjgr.pig.chivalrous.core.io.resource;

import com.google.common.base.MoreObjects;
import com.google.common.io.Resources;
import org.gjgr.pig.chivalrous.core.io.file.FileCommand;
import org.gjgr.pig.chivalrous.core.lang.AssertCommand;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * new File("..\path\abc.txt") 中的三个方法获取路径的方法 1： getPath() 获取相对路径，例如 ..\path\abc.txt 2： getAbslutlyPath() 获取绝对路径，但可能包含
 * ".." 或 "." 字符，例如 D:\otherPath\..\path\abc.txt 3： getCanonicalPath() 获取绝对路径，但不包含 ".." 或 "." 字符，例如 D:\path\abc.txt
 */
public class LocationCommand {

    private static final String FOLDER_SEPARATOR = "/";
    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";
    private static final String TOP_PATH = "..";
    private static final String CURRENT_PATH = ".";
    private static String webRootPath;
    private static String rootClassPath;

    public static String pathValue(Class clazz, String path) {
        String check = pathValue(path);
        if (!FileCommand.isExist(check)) {
            check = LocationCommand.classPath(clazz) + File.separator + path;
        }
        return check;
    }

    public static String valuePath(String path) {
        String p = pathValue(path);
        if (!FileCommand.isExist(p)) {
            p = path;
        }
        return p;
    }

    public static String pathValue(String path) {
        String check = path;
        if (!FileCommand.isExist(check)) {
            if (System.getenv(path) != null) {
                check = System.getenv(path);
            } else {
                if (System.getenv("basedir") != null) {
                    check = System.getProperty("basedir") == null ? path
                            : System.getProperty("basedir") + File.separator + path;
                } else {
                    check = System.getProperty("user.dir") == null ? path
                            : System.getProperty("user.dir") + File.separator + path;
                }
            }
            if (!FileCommand.isExist(check)) {
                check = LocationCommand.userDir() + File.separator + path;
                if (!FileCommand.isExist(check)) {
                    check = LocationCommand.classPath() + File.separator + path;
                    // could try use other
                }
            }

        }
        return check;
    }

    public static URL getResource(String resourceName) {
        ClassLoader loader =
                MoreObjects.firstNonNull(
                        Thread.currentThread().getContextClassLoader(), Resources.class.getClassLoader());
        URL url = loader.getResource(resourceName);
        checkArgument(url != null, "resource %s not found.", resourceName);
        return url;
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

    public static String classPath(Class clazz) {
        String path = null;
        try {
            URL url = clazz.getResource("");
            URI uri = url.toURI();
            path = clazz.getResource("").getPath();

            if (path.contains(".jar")) {
                path = path.substring(path.indexOf(":/"), path.lastIndexOf("!/"));
            } else {
                path = userDir();
            }
            path = new File(path).getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    public static String userDir() {
        return System.getProperty("user.dir");
    }

    public static String classPath() {
        if (rootClassPath == null) {
            try {
                String path = null;
                URL url = LocationCommand.class.getClassLoader().getResource("");
                if (url == null) {
                    if (System.getProperty("java.class.path").contains(".jar")) {
                        path = System.getProperty("java.class.path");
                    } else {
                        path = userDir();
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

    /**
     * Apply the given relative path to the given Java resource path, assuming standard Java folder separation (i.e. "/"
     * separators).
     *
     * @param path         the path to start from (usually a full file path)
     * @param relativePath the relative path to apply (relative to the full file path above)
     * @return the full file path that results from applying the relative path
     */
    public static String applyRelativePath(String path, String relativePath) {
        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (separatorIndex != -1) {
            String newPath = path.substring(0, separatorIndex);
            if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
                newPath += FOLDER_SEPARATOR;
            }
            return newPath + relativePath;
        } else {
            return relativePath;
        }
    }

    /**
     * Normalize the path by suppressing sequences like "path/.." and inner simple dots.
     * <p>
     * The result is convenient for path comparison. For other uses, notice that Windows separators ("\") are replaced
     * by simple slashes.
     *
     * @param path the original path
     * @return the normalized path
     */
    public static String cleanPath(String path) {
        if (!StringCommand.hasLength(path)) {
            return path;
        }
        String pathToUse = StringCommand.replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);

        // Strip prefix from path to analyze, to not treat it as part of the
        // first path element. This is necessary to correctly parse paths like
        // "file:core/../core/io/Resource.class", where the ".." should just
        // strip the first "core" directory while keeping the "file:" prefix.
        int prefixIndex = pathToUse.indexOf(':');
        String prefix = "";
        if (prefixIndex != -1) {
            prefix = pathToUse.substring(0, prefixIndex + 1);
            if (prefix.contains("/")) {
                prefix = "";
            } else {
                pathToUse = pathToUse.substring(prefixIndex + 1);
            }
        }
        if (pathToUse.startsWith(FOLDER_SEPARATOR)) {
            prefix = prefix + FOLDER_SEPARATOR;
            pathToUse = pathToUse.substring(1);
        }

        String[] pathArray = StringCommand.delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
        List<String> pathElements = new LinkedList<>();
        int tops = 0;

        for (int i = pathArray.length - 1; i >= 0; i--) {
            String element = pathArray[i];
            if (CURRENT_PATH.equals(element)) {
                // Points to current directory - drop it.
            } else if (TOP_PATH.equals(element)) {
                // Registering top path found.
                tops++;
            } else {
                if (tops > 0) {
                    // Merging path element with element corresponding to top path.
                    tops--;
                } else {
                    // Normal path element found.
                    pathElements.add(0, element);
                }
            }
        }

        // Remaining top paths need to be retained.
        for (int i = 0; i < tops; i++) {
            pathElements.add(0, TOP_PATH);
        }

        return prefix + StringCommand.collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
    }

    /**
     * Compare two paths after normalization of them.
     *
     * @param path1 first path for comparison
     * @param path2 second path for comparison
     * @return whether the two paths are equivalent after normalization
     */
    public static boolean pathEquals(String path1, String path2) {
        return cleanPath(path1).equals(cleanPath(path2));
    }

    /**
     * Decode the given encoded URI component value. Based on the following rules:
     * <ul>
     * <li>Alphanumeric characters {@code "a"} through {@code "z"}, {@code "A"} through {@code "Z"}, and {@code "0"}
     * through {@code "9"} stay the same.</li>
     * <li>Special characters {@code "-"}, {@code "_"}, {@code "."}, and {@code "*"} stay the same.</li>
     * <li>A sequence "{@code %<i>xy</i>}" is interpreted as a hexadecimal representation of the character.</li>
     * </ul>
     *
     * @param source  the encoded String
     * @param charset the character set
     * @return the decoded value
     * @throws IllegalArgumentException when the given source contains invalid encoded sequences
     * @see java.net.URLDecoder#decode(String, String)
     * @since 5.0
     */
    public static String uriDecode(String source, Charset charset) {
        int length = source.length();
        if (length == 0) {
            return source;
        }
        AssertCommand.notNull(charset, "Charset must not be null");

        ByteArrayOutputStream bos = new ByteArrayOutputStream(length);
        boolean changed = false;
        for (int i = 0; i < length; i++) {
            int ch = source.charAt(i);
            if (ch == '%') {
                if (i + 2 < length) {
                    char hex1 = source.charAt(i + 1);
                    char hex2 = source.charAt(i + 2);
                    int u = Character.digit(hex1, 16);
                    int l = Character.digit(hex2, 16);
                    if (u == -1 || l == -1) {
                        throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
                    }
                    bos.write((char) ((u << 4) + l));
                    i += 2;
                    changed = true;
                } else {
                    throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
                }
            } else {
                bos.write(ch);
            }
        }
        return (changed ? new String(bos.toByteArray(), charset) : source);
    }

}
