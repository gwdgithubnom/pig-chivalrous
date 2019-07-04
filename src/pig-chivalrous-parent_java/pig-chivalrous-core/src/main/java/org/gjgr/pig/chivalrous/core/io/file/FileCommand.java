package org.gjgr.pig.chivalrous.core.io.file;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.gjgr.pig.chivalrous.core.io.IoCommand;
import org.gjgr.pig.chivalrous.core.io.exception.IORuntimeException;
import org.gjgr.pig.chivalrous.core.io.file.download.DirectDownloader;
import org.gjgr.pig.chivalrous.core.io.file.download.DownloadListener;
import org.gjgr.pig.chivalrous.core.io.file.download.DownloadTask;
import org.gjgr.pig.chivalrous.core.io.file.download.HttpConnector;
import org.gjgr.pig.chivalrous.core.io.resource.LocationCommand;
import org.gjgr.pig.chivalrous.core.io.stream.BOMInputStream;
import org.gjgr.pig.chivalrous.core.io.stream.StreamCommand;
import org.gjgr.pig.chivalrous.core.lang.ArrayCommand;
import org.gjgr.pig.chivalrous.core.lang.AssertCommand;
import org.gjgr.pig.chivalrous.core.lang.ClassCommand;
import org.gjgr.pig.chivalrous.core.lang.CollectionCommand;
import org.gjgr.pig.chivalrous.core.lang.Nullable;
import org.gjgr.pig.chivalrous.core.lang.ObjectCommand;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.gjgr.pig.chivalrous.core.net.UriBuilder;
import org.gjgr.pig.chivalrous.core.net.UriCommand;
import org.gjgr.pig.chivalrous.core.nio.CharsetCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 文件工具类
 *
 * @author xiaoleilu
 */
public final class FileCommand {

    /**
     * Class文件扩展名
     */
    public static final String CLASS_EXT = ".class";
    /**
     * Jar文件扩展名
     */
    public static final String JAR_FILE_EXT = ".jar";
    /**
     * 在Jar中的路径jar的扩展名形式
     */
    public static final String JAR_PATH_EXT = ".jar!";
    /**
     * 当Path为文件形式时, path会加入一个表示文件的前缀
     */
    public static final String PATH_FILE_PRE = "file:";
    public static final int BUFFER_SIZE = 128 * 1024;
    /**
     * Pseudo URL prefix for loading from the class path: "classpath:"
     */
    public static final String CLASSPATH_URL_PREFIX = "classpath:";
    /**
     * URL prefix for loading from the file system: "file:"
     */
    public static final String FILE_URL_PREFIX = "file:";
    /**
     * URL prefix for loading from a jar file: "jar:"
     */
    public static final String JAR_URL_PREFIX = "jar:";
    /**
     * URL prefix for loading from a war file on Tomcat: "war:"
     */
    public static final String WAR_URL_PREFIX = "war:";
    /**
     * URL protocol for a file in the file system: "file"
     */
    public static final String URL_PROTOCOL_FILE = "file";
    /**
     * URL protocol for an entry from a jar file: "jar"
     */
    public static final String URL_PROTOCOL_JAR = "jar";
    /**
     * URL protocol for an entry from a war file: "war"
     */
    public static final String URL_PROTOCOL_WAR = "war";
    /**
     * URL protocol for an entry from a zip file: "zip"
     */
    public static final String URL_PROTOCOL_ZIP = "zip";
    /**
     * URL protocol for an entry from a WebSphere jar file: "wsjar"
     */
    public static final String URL_PROTOCOL_WSJAR = "wsjar";
    /**
     * URL protocol for an entry from a JBoss jar file: "vfszip"
     */
    public static final String URL_PROTOCOL_VFSZIP = "vfszip";
    /**
     * URL protocol for a JBoss file system resource: "vfsfile"
     */
    public static final String URL_PROTOCOL_VFSFILE = "vfsfile";
    /**
     * URL protocol for a general JBoss VFS resource: "vfs"
     */
    public static final String URL_PROTOCOL_VFS = "vfs";
    /**
     * File extension for a regular jar file: ".jar"
     */
    public static final String JAR_FILE_EXTENSION = ".jar";
    /**
     * Separator between JAR URL and file path within the JAR: "!/"
     */
    public static final String JAR_URL_SEPARATOR = "!/";
    /**
     * Special separator between WAR URL and jar part on Tomcat
     */
    public static final String WAR_URL_SEPARATOR = "*/";
    /**
     * The Unix separator character.
     */
    private static final char UNIX_SEPARATOR = '/';
    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_SEPARATOR = '\\';
    private static final int RETRY_SLEEP_MILLIS = 10;
    /**
     * The System property key for the user directory.
     */
    private static final String USER_DIR_KEY = "user.dir";
    private static final File USER_DIR = new File(System.getProperty(USER_DIR_KEY));
    private static final char EXTENSION_SEPARATOR = '.';
    private static final String FOLDER_SEPARATOR = "/";
    private static File defaultTempDir;
    private static Thread shutdownHook;
    private static boolean windowsOs = initWindowsOs();
    private static Logger logger = LoggerFactory.getLogger(FileCommand.class.getName());

    private FileCommand() {
    }

    /**
     * Delete the supplied {@link File} - for directories, recursively delete any nested directories or files as well.
     * <p>
     * Note: Like {@link File#delete()}, this method does not throw any exception but rather silently returns
     * {@code false} in case of I/O errors. Consider using {@link #deleteRecursively(Path)} for NIO-style handling of
     * I/O errors, clearly differentiating between non-existence and failure to delete an existing file.
     *
     * @param root the root {@code File} to delete
     * @return {@code true} if the {@code File} was successfully deleted, otherwise {@code false}
     */
    public static boolean deleteRecursively(@Nullable File root) {
        if (root == null) {
            return false;
        }

        try {
            return deleteRecursively(root.toPath());
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Delete the supplied {@link File} - for directories, recursively delete any nested directories or files as well.
     *
     * @param root the root {@code File} to delete
     * @return {@code true} if the {@code File} existed and was deleted, or {@code false} it it did not exist
     * @throws IOException in the case of I/O errors
     * @since 5.0
     */
    public static boolean deleteRecursively(@Nullable Path root) throws IOException {
        if (root == null) {
            return false;
        }
        if (!Files.exists(root)) {
            return false;
        }

        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
        return true;
    }

    /**
     * Recursively copy the contents of the {@code src} file/directory to the {@code dest} file/directory.
     *
     * @param src  the source directory
     * @param dest the destination directory
     * @throws IOException in the case of I/O errors
     */
    public static void copyRecursively(File src, File dest) throws IOException {
        AssertCommand.notNull(src, "Source File must not be null");
        AssertCommand.notNull(dest, "Destination File must not be null");
        copyRecursively(src.toPath(), dest.toPath());
    }

    /**
     * Recursively copy the contents of the {@code src} file/directory to the {@code dest} file/directory.
     *
     * @param src  the source directory
     * @param dest the destination directory
     * @throws IOException in the case of I/O errors
     * @since 5.0
     */
    public static void copyRecursively(Path src, Path dest) throws IOException {
        AssertCommand.notNull(src, "Source Path must not be null");
        AssertCommand.notNull(dest, "Destination Path must not be null");
        BasicFileAttributes srcAttr = Files.readAttributes(src, BasicFileAttributes.class);

        if (srcAttr.isDirectory()) {
            Files.walkFileTree(src, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Files.createDirectories(dest.resolve(src.relativize(dir)));
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.copy(file, dest.resolve(src.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }
            });
        } else if (srcAttr.isRegularFile()) {
            Files.copy(src, dest);
        } else {
            throw new IllegalArgumentException("Source File must denote a directory or file");
        }
    }

    /**
     * 列出目录文件<br>
     * 给定的绝对路径不能是压缩包中的路径
     *
     * @param path 目录绝对路径或者相对路径
     * @return 文件列表（包含目录）
     */
    public static File[] ls(String path) {
        if (path == null) {
            return null;
        }
        path = getAbsolutePath(path);

        File file = file(path);
        if (file.isDirectory()) {
            return file.listFiles();
        }
        throw new IORuntimeException(StringCommand.format("Path [{}] is not directory!", path));
    }

    /**
     * find file by clazz path location, and return the @See InputStreem
     *
     * @param clazz
     * @param location
     * @return
     */
    public static InputStream file(Class clazz, String location) {
        InputStream inputStream = null;
        String path = location;
        boolean status = false;
        try {
            try {
                inputStream = FileCommand.bufferedInputStream(path);
            } catch (Exception e) {
                path = LocationCommand.userDir();
                path = path + File.separator + location;
                try {
                    inputStream = FileCommand.bufferedInputStream(path);
                } catch (Exception ee) {
                    path = LocationCommand.userDir();
                    path = path + location;
                    try {
                        inputStream = FileCommand.bufferedInputStream(path);
                    } catch (Exception eee) {
                        inputStream = clazz.getResourceAsStream(location);
                        status = true;
                    }
                }
            }
            if (inputStream == null) {
                inputStream = clazz.getResourceAsStream(location);
                status = true;
            }
            if (inputStream == null && status) {
                if (!location.startsWith("/")) {
                    inputStream = clazz.getResourceAsStream(File.separator + location);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * return file list object.
     */
    public static List<File> lsFileList(String path) {
        List<File> files = null;
        if (!FileCommand.isExist(path)) {
            files = Arrays.asList(FileCommand.lsFile(path));
        } else {
            files = new ArrayList<File>();
        }
        return files;
    }

    /**
     * 列出目录下的目录
     */
    public static File[] dir(String path) {
        File[] files = ls(path);
        List<File> dirs = new ArrayList<File>();
        for (File file : files) {
            if (FileCommand.isDirectory(file)) {
                dirs.add(file);
            }
        }
        if (dirs.size() > 0) {
            return dirs.toArray(new File[dirs.size()]);
        } else {
            return null;
        }
    }

    /**
     * 列出目录下的文件
     */

    public static File[] lsFile(String path) {
        File[] files = ls(path);
        List<File> nodirs = new ArrayList<File>();
        for (File file : files) {
            if (FileCommand.isFile(file)) {
                nodirs.add(file);
            }
        }
        if (nodirs.size() > 0) {
            return nodirs.toArray(new File[nodirs.size()]);
        } else {
            return null;
        }
    }

    /**
     * 文件是否为空<br>
     * 目录：里面没有文件时为空 文件：文件大小为0时为空
     *
     * @param file 文件
     * @return 是否为空，当提供非目录时，返回false
     */
    public static boolean isEmpty(File file) {
        if (null == file) {
            return true;
        }

        if (file.isDirectory()) {
            String[] subFiles = file.list();
            if (ArrayCommand.isEmpty(subFiles)) {
                return true;
            }
        } else if (file.isFile()) {
            return file.length() <= 0;
        }

        return false;
    }

    /**
     * 目录是否为空
     *
     * @param file 目录
     * @return 是否为空，当提供非目录时，返回false
     */
    public static boolean isNotEmpty(File file) {
        return false == isEmpty(file);
    }

    /**
     * 目录是否为空
     *
     * @param dirPath 目录
     * @return 是否为空
     * @throws IOException IOException
     */
    public static boolean isDirEmpty(Path dirPath) {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(dirPath)) {
            return false == dirStream.iterator().hasNext();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 目录是否为空
     *
     * @param dir 目录
     * @return 是否为空
     */
    public static boolean isDirEmpty(File dir) {
        return isDirEmpty(dir.toPath());
    }

    /**
     * 递归遍历目录以及子目录中的所有文件<br>
     * 如果提供file为文件，直接返回过滤结果
     *
     * @param file       当前遍历文件或目录
     * @param fileFilter 文件过滤规则对象，选择要保留的文件，只对文件有效，不过滤目录
     */
    public static List<File> loopFiles(File file, FileFilter fileFilter) {
        List<File> fileList = new ArrayList<File>();
        if (file == null) {
            return fileList;
        } else if (file.exists() == false) {
            return fileList;
        }

        if (file.isDirectory()) {
            for (File tmp : file.listFiles()) {
                fileList.addAll(loopFiles(tmp, fileFilter));
            }
        } else {
            if (null == fileFilter || fileFilter.accept(file)) {
                fileList.add(file);
            }
        }

        return fileList;
    }

    /**
     * 递归遍历目录以及子目录中的所有文件
     *
     * @param file 当前遍历文件
     */
    public static List<File> loopFiles(File file) {
        return loopFiles(file, null);
    }

    /**
     * 获得指定目录下所有文件<br>
     * 不会扫描子目录
     *
     * @param path 相对ClassPath的目录或者绝对路径目录
     * @return 文件路径列表（如果是jar中的文件，则给定类似.jar!/xxx/xxx的路径）
     * @throws IOException
     */
    public static List<String> listFileNames(String path) {
        if (path == null) {
            return null;
        }
        path = getAbsolutePath(path);
        if (path.endsWith(String.valueOf(UNIX_SEPARATOR)) == false) {
            path = path + UNIX_SEPARATOR;
        }

        List<String> paths = new ArrayList<String>();
        int index = path.lastIndexOf(FileCommand.JAR_PATH_EXT);
        try {
            if (index == -1) {
                // 普通目录路径
                File[] files = ls(path);
                for (File file : files) {
                    if (file.isFile()) {
                        paths.add(file.getName());
                    }
                }
            } else {
                // jar文件中的路径
                index = index + FileCommand.JAR_FILE_EXT.length();
                final String jarPath = path.substring(0, index);
                final String subPath = path.substring(index + 2);
                for (JarEntry entry : Collections.list(new JarFile(jarPath).entries())) {
                    final String name = entry.getName();
                    if (name.startsWith(subPath)) {
                        String nameSuffix = StringCommand.removePrefix(name, subPath);
                        if (nameSuffix.contains(String.valueOf(UNIX_SEPARATOR)) == false) {
                            paths.add(nameSuffix);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new IORuntimeException(StringCommand.format("Can not read file path of [{}]", path), e);
        }
        return paths;
    }

    /**
     * 创建File对象，自动识别相对或绝对路径，相对路径将自动从ClassPath下寻找
     *
     * @param path 文件路径
     * @return File
     */
    public static File file(String path) {
        if (StringCommand.isBlank(path)) {
            throw new NullPointerException("File path is blank!" + path);
        }
        return new File(getAbsolutePath(path));
    }

    /**
     * 创建File对象
     *
     * @param parent 父目录
     * @param path   文件路径
     * @return File
     */
    public static File file(String parent, String path) {
        if (StringCommand.isBlank(path)) {
            throw new NullPointerException("File path is blank!" + path);
        }
        return new File(parent, path);
    }

    /**
     * 创建File对象
     *
     * @param parent 父文件对象
     * @param path   文件路径
     * @return File
     */
    public static File file(File parent, String path) {
        if (StringCommand.isBlank(path)) {
            throw new NullPointerException("File path is blank!" + path);
        }
        return new File(parent, path);
    }

    /**
     * 创建File对象
     *
     * @param uri 文件URI
     * @return File
     */
    public static File file(URI uri) {
        if (uri == null) {
            throw new NullPointerException("File uri is null!");
        }
        return new File(uri);
    }

    /**
     * 创建File对象
     *
     * @param url 文件URL
     * @return File
     */
    public static File file(URL url) {
        return new File(UriCommand.toURI(url));
    }

    /**
     * 判断文件是否存在，如果path为null，则返回false
     *
     * @param path 文件路径
     * @return 如果存在返回true
     */
    public static boolean isExist(String path) {
        return (path == null) ? false : file(path).exists();
    }

    /**
     * 判断文件是否存在，如果file为null，则返回false
     *
     * @param file 文件
     * @return 如果存在返回true
     */
    public static boolean isExist(File file) {
        return (file == null) ? false : file.exists();
    }

    /**
     * 是否存在匹配文件
     *
     * @param directory 文件夹路径
     * @param regexp    文件夹中所包含文件名的正则表达式
     * @return 如果存在匹配文件返回true
     */
    public static boolean isExist(String directory, String regexp) {
        File file = new File(directory);
        if (!file.exists()) {
            return false;
        }

        String[] fileList = file.list();
        if (fileList == null) {
            return false;
        }

        for (String fileName : fileList) {
            if (fileName.matches(regexp)) {
                return true;
            }

        }
        return false;
    }

    /**
     * 指定文件最后修改时间
     *
     * @param file 文件
     * @return 最后修改时间
     */
    public static Date lastModifiedTime(File file) {
        if (!isExist(file)) {
            return null;
        }

        return new Date(file.lastModified());
    }

    /**
     * 指定路径文件最后修改时间
     *
     * @param path 绝对路径
     * @return 最后修改时间
     */
    public static Date lastModifiedTime(String path) {
        return lastModifiedTime(new File(path));
    }

    /**
     * 计算目录或文件的总大小<br>
     * 当给定对象为文件时，直接调用 {@link File#length()}<br>
     * 当给定对象为目录时，遍历目录下的所有文件和目录，递归计算其大小，求和返回
     *
     * @param file 目录或文件
     * @return 总大小
     */
    public static long size(File file) {
        AssertCommand.notNull(file, "file argument is null !");
        if (false == file.exists()) {
            throw new IllegalArgumentException(StringCommand.format("File [{}] not isExist !", file.getAbsolutePath()));
        }

        if (file.isDirectory()) {
            long size = 0L;
            File[] subFiles = file.listFiles();
            if (ArrayCommand.isEmpty(subFiles)) {
                return 0L;// empty directory
            }
            for (int i = 0; i < subFiles.length; i++) {
                size += size(subFiles[i]);
            }
            return size;
        } else {
            return file.length();
        }
    }

    /**
     * 给定文件或目录的最后修改时间是否晚于给定时间
     *
     * @param file      文件或目录
     * @param reference 参照文件
     * @return 是否晚于给定时间
     */
    public static boolean newerThan(File file, File reference) {
        if (null == file || false == reference.exists()) {
            return true;// 文件一定比一个不存在的文件新
        }
        return newerThan(file, reference.lastModified());
    }

    /**
     * 给定文件或目录的最后修改时间是否晚于给定时间
     *
     * @param file       文件或目录
     * @param timeMillis 做为对比的时间
     * @return 是否晚于给定时间
     */
    public static boolean newerThan(File file, long timeMillis) {
        if (null == file || false == file.exists()) {
            return false;// 不存在的文件一定比任何时间旧
        }
        return file.lastModified() > timeMillis;
    }

    /**
     * 创建文件及其父目录，如果这个文件存在，直接返回这个文件<br>
     * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
     *
     * @param fullFilePath 文件的全路径，使用POSIX风格
     * @return 文件，若路径为null，返回null
     * @throws IOException
     */
    public static File touch(String fullFilePath) throws IORuntimeException {
        if (fullFilePath == null) {
            return null;
        }
        return touch(file(fullFilePath));
    }

    /**
     * 创建文件及其父目录，如果这个文件存在，直接返回这个文件<br>
     * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
     *
     * @param file 文件对象
     * @return 文件，若路径为null，返回null
     * @throws IOException
     */
    public static File touch(File file) {
        if (null == file) {
            return null;
        }
        if (false == file.exists()) {
            mkParentDirs(file);
            try {
                file.createNewFile();
            } catch (Exception e) {
                throw new IORuntimeException(e);
            }
        }
        return file;
    }

    /**
     * 创建文件及其父目录，如果这个文件存在，直接返回这个文件<br>
     * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
     *
     * @param parent 父文件对象
     * @param path   文件路径
     * @return File
     * @throws IOException
     */
    public static File touch(File parent, String path) throws IOException {
        return touch(file(parent, path));
    }

    /**
     * 创建文件及其父目录，如果这个文件存在，直接返回这个文件<br>
     * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
     *
     * @param parent 父文件对象
     * @param path   文件路径
     * @return File
     * @throws IOException
     */
    public static File touch(String parent, String path) throws IOException {
        return touch(file(parent, path));
    }

    /**
     * 创建所给文件或目录的父目录
     *
     * @param file 文件或目录
     * @return 父目录
     */
    public static File mkParentDirs(File file) {
        final File parentFile = file.getParentFile();
        if (null != parentFile && false == parentFile.exists()) {
            parentFile.mkdirs();
        }
        return parentFile;
    }

    /**
     * 创建父文件夹，如果存在直接返回此文件夹
     *
     * @param path 文件夹路径，使用POSIX格式，无论哪个平台
     * @return 创建的目录
     */
    public static File mkParentDirs(String path) {
        if (path == null) {
            return null;
        }
        return mkParentDirs(file(path));
    }

    /**
     * 删除文件或者文件夹<br>
     * 路径如果为相对路径，会转换为ClassPath路径！ 注意：删除文件夹时不会判断文件夹是否为空，如果不空则递归删除子文件或文件夹<br>
     * 某个文件删除失败会终止删除操作
     *
     * @param fullFileOrDirPath 文件或者目录的路径
     * @return 成功与否
     * @throws IOException
     */
    public static boolean del(String fullFileOrDirPath) throws IOException {
        return del(file(fullFileOrDirPath));
    }

    /**
     * 删除文件或者文件夹<br>
     * 注意：删除文件夹时不会判断文件夹是否为空，如果不空则递归删除子文件或文件夹<br>
     * 某个文件删除失败会终止删除操作
     *
     * @param file 文件对象
     * @return 成功与否
     * @throws IOException
     */
    public static boolean del(File file) throws IOException {
        if (file == null || file.exists() == false) {
            return true;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File childFile : files) {
                boolean isOk = del(childFile);
                if (isOk == false) {
                    // 删除一个出错则本次删除任务失败
                    return false;
                }
            }
        }
        return file.delete();
    }

    /**
     * 创建文件夹，如果存在直接返回此文件夹<br>
     * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
     *
     * @param dirPath 文件夹路径，使用POSIX格式，无论哪个平台
     * @return 创建的目录
     */
    public static File mkdir(String dirPath) {
        if (dirPath == null) {
            return null;
        }
        final File dir = file(dirPath);
        return mkdir(dir);
    }

    /**
     * 创建文件夹，会递归自动创建其不存在的父文件夹，如果存在直接返回此文件夹<br>
     * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
     *
     * @param dir 目录
     * @return 创建的目录
     */
    public static File mkdir(File dir) {
        if (dir == null) {
            return null;
        }
        if (false == dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    // -------------------------------------------------------------------------------------------- name start

    /**
     * 创建临时文件<br>
     * 创建后的文件名为 prefix[Randon].tmp
     *
     * @param dir 临时文件创建的所在目录
     * @return 临时文件
     * @throws IOException
     */
    public static File createTempFile(File dir) throws IOException {
        return createTempFile("hutool", null, dir, true);
    }

    /**
     * 创建临时文件<br>
     * 创建后的文件名为 prefix[Randon].tmp
     *
     * @param dir       临时文件创建的所在目录
     * @param isReCreat 是否重新创建文件（删掉原来的，创建新的）
     * @return 临时文件
     * @throws IOException
     */
    public static File createTempFile(File dir, boolean isReCreat) throws IOException {
        return createTempFile("hutool", null, dir, isReCreat);
    }

    /**
     * 创建临时文件<br>
     * 创建后的文件名为 prefix[Randon].suffix From com.jodd.io.FileCommand
     *
     * @param prefix    前缀，至少3个字符
     * @param suffix    后缀，如果null则使用默认.tmp
     * @param dir       临时文件创建的所在目录
     * @param isReCreat 是否重新创建文件（删掉原来的，创建新的）
     * @return 临时文件
     * @throws IOException
     */
    public static File createTempFile(String prefix, String suffix, File dir, boolean isReCreat) throws IOException {
        int exceptionsCount = 0;
        while (true) {
            try {
                File file = File.createTempFile(prefix, suffix, dir).getCanonicalFile();
                if (isReCreat) {
                    file.delete();
                    file.createNewFile();
                }
                return file;
            } catch (IOException ioex) { // fixes java.io.WinNTFileSystem.createFileExclusively access denied
                if (++exceptionsCount >= 50) {
                    throw ioex;
                }
            }
        }
    }

    /**
     * 通过JDK7+的 {@link Files#copy(Path, Path, CopyOption...)} 方法拷贝文件
     *
     * @param src     源文件路径
     * @param dest    目标文件或目录路径，如果为目录使用与源文件相同的文件名
     * @param options {@link StandardCopyOption}
     * @return File
     * @throws IOException
     */
    public static File copyFile(String src, String dest, StandardCopyOption... options) throws IOException {
        AssertCommand.notBlank(src, "Source File path is blank !");
        AssertCommand.notNull(src, "Destination File path is null !");
        return copyFile(Paths.get(src), Paths.get(dest), options).toFile();
    }

    /**
     * 通过JDK7+的 {@link Files#copy(Path, Path, CopyOption...)} 方法拷贝文件
     *
     * @param src     源文件
     * @param dest    目标文件或目录，如果为目录使用与源文件相同的文件名
     * @param options {@link StandardCopyOption}
     * @return File
     * @throws IOException
     */
    public static File copyFile(File src, File dest, StandardCopyOption... options) throws IOException {
        // check
        AssertCommand.notNull(src, "Source File is null !");
        if (false == src.exists()) {
            throw new FileNotFoundException("File not isExist: " + src);
        }
        AssertCommand.notNull(dest, "Destination File or directiory is null !");
        if (equals(src, dest)) {
            throw new IOException("Files '" + src + "' and '" + dest + "' are equal");
        }

        Path srcPath = src.toPath();
        Path destPath = dest.isDirectory() ? dest.toPath().resolve(srcPath.getFileName()) : dest.toPath();
        return Files.copy(srcPath, destPath, options).toFile();
    }

    /**
     * 通过JDK7+的 {@link Files#copy(Path, Path, CopyOption...)} 方法拷贝文件
     *
     * @param src     源文件路径
     * @param dest    目标文件或目录，如果为目录使用与源文件相同的文件名
     * @param options {@link StandardCopyOption}
     * @return Path
     * @throws IOException
     */
    public static Path copyFile(Path src, Path dest, StandardCopyOption... options) throws IOException {
        AssertCommand.notNull(src, "Source File is null !");
        AssertCommand.notNull(dest, "Destination File or directiory is null !");

        Path destPath = dest.toFile().isDirectory() ? dest.resolve(src.getFileName()) : dest;
        return Files.copy(src, destPath, options);
    }
    // -------------------------------------------------------------------------------------------- name end

    // -------------------------------------------------------------------------------------------- in start

    /**
     * 复制文件或目录<br>
     * 如果目标文件为目录，则将源文件以相同文件名拷贝到目标目录
     *
     * @param srcPath    源文件或目录
     * @param destPath   目标文件或目录，目标不存在会自动创建（目录、文件都创建）
     * @param isOverride 是否覆盖目标文件
     * @return 目标目录或文件
     * @throws IOException
     */
    public static File copy(String srcPath, String destPath, boolean isOverride) throws IOException {
        return copy(file(srcPath), file(destPath), isOverride);
    }

    /**
     * 复制文件或目录<br>
     * 情况如下：<br>
     * 1、src和dest都为目录，则讲src下所有文件目录拷贝到dest下<br>
     * 2、src和dest都为文件，直接复制，名字为dest<br>
     * 3、src为文件，dest为目录，将src拷贝到dest目录下<br>
     *
     * @param src        源文件
     * @param dest       目标文件或目录，目标不存在会自动创建（目录、文件都创建）
     * @param isOverride 是否覆盖目标文件
     * @return 目标目录或文件
     * @throws IOException
     */
    public static File copy(File src, File dest, boolean isOverride) throws IOException {
        // check
        AssertCommand.notNull(src, "Source File is null !");
        if (false == src.exists()) {
            throw new FileNotFoundException("File not isExist: " + src);
        }
        AssertCommand.notNull(dest, "Destination File or directiory is null !");
        if (equals(src, dest)) {
            throw new IOException("Files '" + src + "' and '" + dest + "' are equal");
        }

        if (src.isDirectory()) {
            // 复制目录
            internalCopyDir(src, dest, isOverride);
        } else {
            // 复制文件
            internalCopyFile(src, dest, isOverride);
        }
        return dest;
    }

    /**
     * 拷贝目录，只用于内部，不做任何安全检查
     *
     * @param src        源目录
     * @param dest       目标目录
     * @param isOverride 是否覆盖
     * @throws IOException
     */
    private static void internalCopyDir(File src, File dest, boolean isOverride) throws IOException {
        if (false == dest.exists()) {
            dest.mkdirs();
        } else if (dest.isFile()) {
            throw new IOException(
                    StringCommand.format("Src [{}] is a directory but dest [{}] is a file!", src.getPath(),
                            dest.getPath()));
        }

        final String[] files = src.list();
        for (String file : files) {
            File srcFile = new File(src, file);
            File destFile = new File(dest, file);
            // 递归复制
            if (src.isDirectory()) {
                internalCopyDir(srcFile, destFile, isOverride);
            } else {
                internalCopyFile(srcFile, destFile, isOverride);
            }
        }
    }

    /**
     * 拷贝文件，只用于内部，不做任何安全检查
     *
     * @param src        源文件，必须为文件
     * @param dest       目标文件，必须为文件
     * @param isOverride 是否覆盖已有文件
     * @throws IOException
     */
    private static void internalCopyFile(File src, File dest, boolean isOverride) throws IOException {
        // copy
        if (false == dest.exists()) {
            // 目标不存在，默认做为文件创建
            touch(dest);
        } else if (dest.isDirectory()) {
            // 目标为目录，则在这个目录下创建同名文件
            dest = new File(dest, src.getName());
        } else if (false == isOverride) {
            // 如果已经存在目标文件，切为不覆盖模式，跳过之
            // StaticLog.debug("File [{}] already isExist, ignore it.", dest);
            return;
        }

        // do copy file
        FileInputStream input = new FileInputStream(src);
        FileOutputStream output = new FileOutputStream(dest);
        try {
            IoCommand.copy(input, output);
        } finally {
            IoCommand.close(output);
            IoCommand.close(input);
        }

        // 验证
        if (src.length() != dest.length()) {
            throw new IOException("Copy file failed of '" + src + "' to '" + dest + "' due to different sizes");
        }
    }

    /**
     * 移动文件或者目录
     *
     * @param src        源文件或者目录
     * @param dest       目标文件或者目录
     * @param isOverride 是否覆盖目标，只有目标为文件才覆盖
     * @throws IOException
     */
    public static void move(File src, File dest, boolean isOverride) throws IOException {
        // check
        if (!src.exists()) {
            throw new FileNotFoundException("File already isExist: " + src);
        }

        // 来源为文件夹，目标为文件
        if (src.isDirectory() && dest.isFile()) {
            throw new IOException(StringCommand.format("Can not move directory [{}] to file [{}]", src, dest));
        }

        if (isOverride && dest.isFile()) {
            // 只有目标为文件的情况下覆盖之
            dest.delete();
        }

        // 来源为文件，目标为文件夹
        if (src.isFile() && dest.isDirectory()) {
            dest = new File(dest, src.getName());
        }

        if (src.renameTo(dest) == false) {
            // 在文件系统不同的情况下，renameTo会失败，此时使用copy，然后删除原文件
            try {
                copy(src, dest, isOverride);
                src.delete();
            } catch (Exception e) {
                throw new IOException(StringCommand.format("Move [{}] to [{}] failed!", src, dest), e);
            }

        }
    }

    /**
     * 获取绝对路径<br/>
     * 此方法不会判定给定路径是否有效（文件或目录存在）
     *
     * @param path      相对路径
     * @param baseClass 相对路径所相对的类
     * @return 绝对路径
     */
    public static String getAbsolutePath(String path, Class<?> baseClass) {
        if (path == null) {
            path = StringCommand.EMPTY;
        }
        if (baseClass == null) {
            return getAbsolutePath(path);
        }
        // return baseClass.getResource(path).getPath();
        return StringCommand.removePrefix(PATH_FILE_PRE, baseClass.getResource(path).getPath());
    }

    /**
     * 获取绝对路径，相对于ClassPath的目录<br>
     * 如果给定就是绝对路径，则返回原路径，原路径把所有\替换为/<br>
     * 兼容Spring风格的路径表示，例如：classpath:config/example.setting也会被识别后转换
     *
     * @param path 相对路径
     * @return 绝对路径
     */
    public static String getAbsolutePath(String path) {
        if (path == null) {
            path = StringCommand.EMPTY;
        } else {
            path = normalize(path);

            if (StringCommand.C_SLASH == path.charAt(0) || path.matches("^[a-zA-Z]:/.*")) {
                // 给定的路径已经是绝对路径了
                return path;
            }
        }

        // 兼容Spring风格的ClassPath路径，去除前缀，不区分大小写
        path = StringCommand.removePrefixIgnoreCase(path, "classpath:");
        path = StringCommand.removePrefix(path, StringCommand.SLASH);

        // 相对于ClassPath路径
        ClassLoader classLoader = ClassCommand.getClassLoader();
        URL url = classLoader.getResource(path);
        String reultPath = url != null ? url.getPath() : ClassCommand.getClassPath() + path;
        // return StringCommand.removePrefix(reultPath, PATH_FILE_PRE);
        return reultPath;
    }

    /**
     * 获取标准的绝对路径
     *
     * @param file 文件
     * @return 绝对路径
     */
    public static String getAbsolutePath(File file) {
        if (file == null) {
            return null;
        }

        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            return file.getAbsolutePath();
        }
    }

    /**
     * 判断是否为目录，如果path为null，则返回false
     *
     * @param path 文件路径
     * @return 如果为目录true
     */
    public static boolean isDirectory(String path) {
        return (path == null) ? false : file(path).isDirectory();
    }

    /**
     * 判断是否为目录，如果file为null，则返回false
     *
     * @param file 文件
     * @return 如果为目录true
     */
    public static boolean isDirectory(File file) {
        return (file == null) ? false : file.isDirectory();
    }

    /**
     * 判断是否为文件，如果path为null，则返回false
     *
     * @param path 文件路径
     * @return 如果为文件true
     */
    public static boolean isFile(String path) {
        return (path == null) ? false : file(path).isFile();
    }

    /**
     * 判断是否为文件，如果file为null，则返回false
     *
     * @param file 文件
     * @return 如果为文件true
     */
    public static boolean isFile(File file) {
        return (file == null) ? false : file.isFile();
    }

    /**
     * 检查两个文件是否是同一个文件
     *
     * @param file1 文件1
     * @param file2 文件2
     * @return 是否相同
     */
    public static boolean equals(File file1, File file2) {
        try {
            file1 = file1.getCanonicalFile();
            file2 = file2.getCanonicalFile();
        } catch (IOException ignore) {
            return false;
        }
        return file1.equals(file2);
    }

    /**
     * 获得最后一个文件路径分隔符的位置
     *
     * @param filePath 文件路径
     * @return 最后一个文件路径分隔符的位置
     */
    public static int indexOfLastSeparator(String filePath) {
        if (filePath == null) {
            return -1;
        }
        int lastUnixPos = filePath.lastIndexOf(UNIX_SEPARATOR);
        int lastWindowsPos = filePath.lastIndexOf(WINDOWS_SEPARATOR);
        return (lastUnixPos >= lastWindowsPos) ? lastUnixPos : lastWindowsPos;
    }

    /**
     * 判断文件是否被改动<br>
     * 如果文件对象为 null 或者文件不存在，被视为改动
     *
     * @param file           文件对象
     * @param lastModifyTime 上次的改动时间
     * @return 是否被改动
     */
    public static boolean isModifed(File file, long lastModifyTime) {
        if (null == file || false == file.exists()) {
            return true;
        }
        return file.lastModified() != lastModifyTime;
    }

    /**
     * 修复路径<br>
     * 1. 统一用 / <br>
     * 2. 多个 / 转换为一个 3. 去除两边空格 4. .. 和 . 转换为绝对路径 5. 去掉前缀，例如file:
     *
     * @param path 原路径
     * @return 修复后的路径
     */
    public static String normalize(String path) {
        if (path == null) {
            return null;
        }
        String pathToUse = path.replaceAll("[/\\\\]{1,}", "/").trim();

        int prefixIndex = pathToUse.indexOf(StringCommand.COLON);
        String prefix = "";
        if (prefixIndex != -1) {
            prefix = pathToUse.substring(0, prefixIndex + 1);
            if (prefix.contains("/")) {
                prefix = "";
            } else {
                pathToUse = pathToUse.substring(prefixIndex + 1);
            }
        }
        if (pathToUse.startsWith(StringCommand.SLASH)) {
            prefix = prefix + StringCommand.SLASH;
            pathToUse = pathToUse.substring(1);
        }

        List<String> pathList = StringCommand.split(pathToUse, StringCommand.C_SLASH);
        List<String> pathElements = new LinkedList<String>();
        int tops = 0;

        for (int i = pathList.size() - 1; i >= 0; i--) {
            String element = pathList.get(i);
            if (StringCommand.DOT.equals(element)) {
                // 当前目录，丢弃
            } else if (StringCommand.DOUBLE_DOT.equals(element)) {
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
            pathElements.add(0, StringCommand.DOUBLE_DOT);
        }

        return prefix + CollectionCommand.join(pathElements, StringCommand.SLASH);
    }

    /**
     * 获得相对子路径
     *
     * @param rootDir  绝对父路径
     * @param filePath 文件路径
     * @return 相对子路径
     */
    public static String subPath(String rootDir, String filePath) {
        return subPath(rootDir, file(filePath));
    }

    // -------------------------------------------------------------------------------------------- in end

    /**
     * 获得相对子路径
     *
     * @param rootDir 绝对父路径
     * @param file    文件
     * @return 相对子路径
     */
    public static String subPath(String rootDir, File file) {
        if (StringCommand.isEmpty(rootDir)) {
        }

        String subPath = null;
        try {
            subPath = file.getCanonicalPath();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }

        if (StringCommand.isNotEmpty(rootDir) && StringCommand.isNotEmpty(subPath)) {
            rootDir = normalize(rootDir);
            subPath = normalize(subPath);

            if (subPath != null && subPath.toLowerCase().startsWith(subPath.toLowerCase())) {
                subPath = subPath.substring(rootDir.length() + 1);
            }
        }
        return subPath;
    }

    /**
     * 返回主文件名
     *
     * @param file 文件
     * @return 主文件名
     */
    public static String mainName(File file) {
        if (file.isDirectory()) {
            return file.getName();
        }
        return mainName(file.getName());
    }

    /**
     * 返回主文件名
     *
     * @param fileName 完整文件名
     * @return 主文件名
     */
    public static String mainName(String fileName) {
        if (StringCommand.isBlank(fileName) || false == fileName.contains(StringCommand.DOT)) {
            return fileName;
        }
        return StringCommand.subPre(fileName, fileName.lastIndexOf(StringCommand.DOT));
    }

    /**
     * 获取文件扩展名，扩展名不带“.”
     *
     * @param file 文件
     * @return 扩展名
     */
    public static String extName(File file) {
        if (null == file) {
            return null;
        }
        if (file.isDirectory()) {
            return null;
        }
        return extName(file.getName());
    }

    /**
     * 获得文件的扩展名，扩展名不带“.”
     *
     * @param fileName 文件名
     * @return 扩展名
     */
    public static String extName(String fileName) {
        if (fileName == null) {
            return null;
        }
        int index = fileName.lastIndexOf(StringCommand.DOT);
        if (index == -1) {
            return StringCommand.EMPTY;
        } else {
            String ext = fileName.substring(index + 1);
            // 扩展名中不能包含路径相关的符号
            return (ext.contains(String.valueOf(UNIX_SEPARATOR)) || ext.contains(String.valueOf(WINDOWS_SEPARATOR)))
                    ? StringCommand.EMPTY
                    : ext;
        }
    }

    /**
     * Extract the filename from the given Java resource path, e.g. {@code "mypath/myfile.txt" -> "myfile.txt"}.
     *
     * @param path the file path (may be {@code null})
     * @return the extracted filename, or {@code null} if none
     */
    @Nullable
    public static String filename(@Nullable String path) {
        if (path == null) {
            return null;
        }

        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
    }

    /**
     * Extract the filename extension from the given Java resource path, e.g. "mypath/myfile.txt" -> "txt".
     *
     * @param path the file path (may be {@code null})
     * @return the extracted filename extension, or {@code null} if none
     */
    @Nullable
    public static String fileExtension(@Nullable String path) {
        if (path == null) {
            return null;
        }

        int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return null;
        }

        int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (folderIndex > extIndex) {
            return null;
        }

        return path.substring(extIndex + 1);
    }

    /**
     * Strip the filename extension from the given Java resource path, e.g. "mypath/myfile.txt" -> "mypath/myfile".
     *
     * @param path the file path
     * @return the path with stripped filename extension
     */
    public static String stripFilenameExtension(String path) {
        int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return path;
        }

        int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (folderIndex > extIndex) {
            return path;
        }

        return path.substring(0, extIndex);
    }

    /**
     * 判断文件路径是否有指定后缀，忽略大小写<br>
     * 常用语判断扩展名
     *
     * @param file   文件或目录
     * @param suffix 后缀
     * @return 是否有指定后缀
     */
    public static boolean pathEndsWith(File file, String suffix) {
        return file.getPath().toLowerCase().endsWith(suffix);
    }

    /**
     * 根据文件流的头部信息获得文件类型
     *
     * @param file 文件 {@link File}
     * @return 类型，文件的扩展名，未找到为<code>null</code>
     * @throws IORuntimeException
     * @see FileTypeUtil#getType(File)
     */
    public static String fileExtension(File file) {
        try {
            return FileTypeUtil.getType(file);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static InputStream inputStream(File file) throws FileNotFoundException {
        return bufferedInputStream(file);
    }

    public static InputStream inputStream(URL url) throws IOException {
        return url.openStream();
    }

    public static InputStream inputStream(String path) throws RuntimeException {
        try {
            return inputStream(new URL(path));
        } catch (Exception e) {
            try {
                return inputStream(FileCommand.file(path));
            } catch (FileNotFoundException e1) {
                throw new RuntimeException("unknown path exception, did not know path " + path);
            }
        }
    }

    /**
     * 获得输入流
     *
     * @param file 文件
     * @return 输入流
     * @throws FileNotFoundException
     */
    public static BufferedInputStream bufferedInputStream(File file) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(file));
    }

    /**
     * 获得输入流
     *
     * @param path 文件路径
     * @return 输入流
     * @throws FileNotFoundException
     */
    public static BufferedInputStream bufferedInputStream(String path) throws FileNotFoundException {
        return bufferedInputStream(file(path));
    }

    // -------------------------------------------------------------------------------------------- out start

    /**
     * 获得BOM输入流，用于处理带BOM头的文件
     *
     * @param file 文件
     * @return 输入流
     * @throws FileNotFoundException
     */
    public static BOMInputStream bomInputStream(File file) throws FileNotFoundException {
        return new BOMInputStream(new FileInputStream(file));
    }

    /**
     * 获得一个文件读取器
     *
     * @param file 文件
     * @return BufferedReader对象
     * @throws IOException
     */
    public static BufferedReader bufferedReaderWithUTF8(File file) throws IOException {
        return bufferedReader(file, CharsetCommand.CHARSET_UTF_8);
    }

    /**
     * 获得一个文件读取器
     *
     * @param path 文件路径
     * @return BufferedReader对象
     * @throws IOException
     */
    public static BufferedReader bufferedReaderWithUTF8(String path) throws IOException {
        return bufferedReader(path, CharsetCommand.CHARSET_UTF_8);
    }

    /**
     * 获得一个文件读取器
     *
     * @param file        文件
     * @param charsetName 字符集
     * @return BufferedReader对象
     * @throws IOException
     */
    public static BufferedReader bufferedReader(File file, String charsetName) throws IOException {
        return IoCommand.getReader(bufferedInputStream(file), charsetName);
    }

    /**
     * 获得一个文件读取器
     *
     * @param file    文件
     * @param charset 字符集
     * @return BufferedReader对象
     * @throws IOException
     */
    public static BufferedReader bufferedReader(File file, Charset charset) throws IOException {
        return IoCommand.getReader(bufferedInputStream(file), charset);
    }

    /**
     * 获得一个文件读取器
     *
     * @param path        绝对路径
     * @param charsetName 字符集
     * @return BufferedReader对象
     * @throws IOException
     */
    public static BufferedReader bufferedReader(String path, String charsetName) throws IOException {
        return bufferedReader(file(path), charsetName);
    }

    /**
     * 获得一个文件读取器
     *
     * @param path    绝对路径
     * @param charset 字符集
     * @return BufferedReader对象
     * @throws IOException
     */
    public static BufferedReader bufferedReader(String path, Charset charset) throws IOException {
        return bufferedReader(file(path), charset);
    }

    /**
     * 读取文件所有数据<br>
     * 文件的长度不能超过Integer.MAX_VALUE
     *
     * @param file 文件
     * @return 字节码
     * @throws IORuntimeException
     */
    public static byte[] readBytes(File file) throws IORuntimeException {
        return FileReader.create(file).readBytes();
    }

    // -------------------------------------------------------------------------------------------- out end

    /**
     * 读取文件内容
     *
     * @param file 文件
     * @return 内容
     * @throws IOException
     */
    public static String readUtf8String(File file) {
        return readString(file, CharsetCommand.CHARSET_UTF_8);
    }

    public static String readUtfResource(String name) {
        String data = null;
        try {
            data = readUtf8String(LocationCommand.pathValue(name));
        } catch (Exception e) {
            readUtfResource(name, ClassLoader.getSystemClassLoader());
        }
        return data;
    }

    public static String readUtfResource(String name, ClassLoader classLoader) {
        String data = null;
        try {
            data = readUtf8String(LocationCommand.pathValue(name));
        } catch (Exception e) {
            logger.warn("try to used class loader to find resource.");
        }
        if (data == null) {
            InputStream inputStream = classLoader.getSystemResourceAsStream(name);
            try {
                data = StreamCommand.loadText(inputStream);
            } catch (Exception e) {
                try {
                    data = StringCommand.string(inputStream);
                } catch (Exception ee) {
                    URL url = Resources.getResource(name);
                    try {
                        data = Resources.toString(url, Charsets.UTF_8);
                        return data;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        logger.error("did not found " + name + " resource file.", ee);
                        return null;
                    }
                }
            }
        }
        return data;
    }

    /**
     * 读取文件内容
     *
     * @param path 文件路径
     * @return 内容
     * @throws IORuntimeException
     */
    public static String readUtf8String(String path) throws IORuntimeException {
        String data = readString(path, CharsetCommand.CHARSET_UTF_8);
        return data;
    }

    /**
     * 读取文件内容
     *
     * @param file        文件
     * @param charsetName 字符集
     * @return 内容
     * @throws IORuntimeException
     */
    public static String readString(File file, String charsetName) throws IORuntimeException {
        return readString(file, CharsetCommand.charset(charsetName));
    }

    /**
     * 读取文件内容
     *
     * @param file    文件
     * @param charset 字符集
     * @return 内容
     * @throws IORuntimeException
     */
    public static String readString(File file, Charset charset) throws IORuntimeException {
        String data = FileReader.create(file, charset).readString();
        return data;
    }

    /**
     * 读取文件内容
     *
     * @param path        文件路径
     * @param charsetName 字符集
     * @return 内容
     * @throws IORuntimeException
     */
    public static String readString(String path, String charsetName) throws IORuntimeException {
        return readString(file(path), charsetName);
    }

    /**
     * 读取文件内容
     *
     * @param path    文件路径
     * @param charset 字符集
     * @return 内容
     * @throws IORuntimeException
     */
    public static String readString(String path, Charset charset) throws IORuntimeException {
        return readString(file(path), charset);
    }

    /**
     * 读取文件内容
     *
     * @param url     文件URL
     * @param charset 字符集
     * @return 内容
     * @throws IOException
     */
    public static String readString(URL url, String charset) throws IORuntimeException {
        if (url == null) {
            throw new RuntimeException("Empty url provided!");
        }

        InputStream in = null;
        try {
            in = url.openStream();
            return IoCommand.read(in, charset);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            IoCommand.close(in);
        }
    }

    /**
     * 从文件中读取每一行数据
     *
     * @param path       文件路径
     * @param charset    字符集
     * @param collection 集合
     * @return 文件中的每行内容的集合
     * @throws IORuntimeException
     */
    public static <T extends Collection<String>> T readLines(String path, String charset, T collection)
            throws IORuntimeException {
        return readLines(file(path), charset, collection);
    }

    /**
     * 从文件中读取每一行数据
     *
     * @param file       文件路径
     * @param charset    字符集
     * @param collection 集合
     * @return 文件中的每行内容的集合
     * @throws IORuntimeException
     */
    public static <T extends Collection<String>> T readLines(File file, String charset, T collection)
            throws IORuntimeException {
        return FileReader.create(file, CharsetCommand.charset(charset)).readLines(collection);
    }

    /**
     * 从文件中读取每一行数据
     *
     * @param url        文件的URL
     * @param charset    字符集
     * @param collection 集合
     * @return 文件中的每行内容的集合
     * @throws IOException
     */
    public static <T extends Collection<String>> T readLines(URL url, String charset, T collection)
            throws IORuntimeException {
        InputStream in = null;
        try {
            in = url.openStream();
            return IoCommand.readLines(in, charset, collection);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            IoCommand.close(in);
        }
    }

    /**
     * 从文件中读取每一行数据
     *
     * @param url     文件的URL
     * @param charset 字符集
     * @return 文件中的每行内容的集合List
     * @throws IORuntimeException
     */
    public static List<String> readLines(URL url, String charset) throws IORuntimeException {
        return readLines(url, charset, new ArrayList<String>());
    }

    /**
     * 从文件中读取每一行数据
     *
     * @param path    文件路径
     * @param charset 字符集
     * @return 文件中的每行内容的集合List
     * @throws IORuntimeException
     */
    public static List<String> readLines(String path, String charset) throws IORuntimeException {
        return readLines(path, charset, new ArrayList<String>());
    }

    /**
     * 从文件中读取每一行数据
     *
     * @param file    文件
     * @param charset 字符集
     * @return 文件中的每行内容的集合List
     * @throws IORuntimeException
     */
    public static List<String> readLines(File file, String charset) throws IORuntimeException {
        return readLines(file, charset, new ArrayList<String>());
    }

    /**
     * 按照给定的readerHandler读取文件中的数据
     *
     * @param readerHandler Reader处理类
     * @param path          文件的绝对路径
     * @param charset       字符集
     * @return 从文件中load出的数据
     * @throws IORuntimeException
     */
    public static <T> T load(FileReader.ReaderHandler<T> readerHandler, String path, String charset)
            throws IORuntimeException {
        return FileReader.create(file(path), CharsetCommand.charset(charset)).read(readerHandler);
    }

    /**
     * 获得一个输出流对象
     *
     * @param file 文件
     * @return 输出流对象
     * @throws IOException
     */
    public static BufferedOutputStream bufferedOutputStream(File file) throws IOException {
        return new BufferedOutputStream(new FileOutputStream(touch(file)));
    }

    /**
     * 获得一个输出流对象
     *
     * @param path 输出到的文件路径，绝对路径
     * @return 输出流对象
     * @throws IOException
     */
    public static BufferedOutputStream bufferedOutputStream(String path) throws IOException {
        return bufferedOutputStream(touch(path));
    }

    /**
     * 获得一个带缓存的写入对象
     *
     * @param path        输出路径，绝对路径
     * @param charsetName 字符集
     * @param isAppend    是否追加
     * @return BufferedReader对象
     * @throws IOException
     */
    public static BufferedWriter bufferedWriter(String path, String charsetName, boolean isAppend) throws IOException {
        return bufferedWriter(touch(path), Charset.forName(charsetName), isAppend);
    }

    /**
     * 获得一个带缓存的写入对象
     *
     * @param path     输出路径，绝对路径
     * @param charset  字符集
     * @param isAppend 是否追加
     * @return BufferedReader对象
     * @throws IOException
     */
    public static BufferedWriter bufferedWriter(String path, Charset charset, boolean isAppend) throws IOException {
        return bufferedWriter(touch(path), charset, isAppend);
    }

    /**
     * 获得一个带缓存的写入对象
     *
     * @param file        输出文件
     * @param charsetName 字符集
     * @param isAppend    是否追加
     * @return BufferedReader对象
     * @throws IOException
     */
    public static BufferedWriter bufferedWriter(File file, String charsetName, boolean isAppend) throws IOException {
        return bufferedWriter(file, Charset.forName(charsetName), isAppend);
    }

    /**
     * 获得一个带缓存的写入对象
     *
     * @param file     输出文件
     * @param charset  字符集
     * @param isAppend 是否追加
     * @return BufferedReader对象
     * @throws IOException
     */
    public static BufferedWriter bufferedWriter(File file, Charset charset, boolean isAppend) throws IOException {
        return FileWriter.create(file, charset).getWriter(isAppend);
    }

    /**
     * 获得一个打印写入对象，可以有print
     *
     * @param path     输出路径，绝对路径
     * @param charset  字符集
     * @param isAppend 是否追加
     * @return 打印对象
     * @throws IOException
     */
    public static PrintWriter printWriter(String path, String charset, boolean isAppend) throws IOException {
        return new PrintWriter(bufferedWriter(path, charset, isAppend));
    }

    /**
     * 获得一个打印写入对象，可以有print
     *
     * @param file     文件
     * @param charset  字符集
     * @param isAppend 是否追加
     * @return 打印对象
     * @throws IOException
     */
    public static PrintWriter printWriter(File file, String charset, boolean isAppend) throws IOException {
        return new PrintWriter(bufferedWriter(file, charset, isAppend));
    }

    /**
     * 将String写入文件，覆盖模式，字符集为UTF-8
     *
     * @param content 写入的内容
     * @param path    文件路径
     * @return 写入的文件
     * @throws IORuntimeException
     */
    public static File writeUtf8String(String content, String path) throws IORuntimeException {
        return writeString(content, path, CharsetCommand.UTF_8);
    }

    /**
     * 将String写入文件，覆盖模式，字符集为UTF-8
     *
     * @param content 写入的内容
     * @param file    文件
     * @return 写入的文件
     * @throws IORuntimeException
     */
    public static File writeUtf8String(String content, File file) throws IORuntimeException {
        return writeString(content, file, CharsetCommand.UTF_8);
    }

    /**
     * 将String写入文件，覆盖模式
     *
     * @param content 写入的内容
     * @param path    文件路径
     * @param charset 字符集
     * @return 写入的文件
     * @throws IORuntimeException
     */
    public static File writeString(String content, String path, String charset) throws IORuntimeException {
        return writeString(content, touch(path), charset);
    }

    /**
     * 将String写入文件，覆盖模式
     *
     * @param content 写入的内容
     * @param file    文件
     * @param charset 字符集
     * @throws IORuntimeException
     */
    public static File writeString(String content, File file, String charset) throws IORuntimeException {
        return FileWriter.create(file, CharsetCommand.charset(charset)).write(content);
    }

    /**
     * 将String写入文件，追加模式
     *
     * @param content 写入的内容
     * @param path    文件路径
     * @param charset 字符集
     * @return 写入的文件
     * @throws IORuntimeException
     */
    public static File appendString(String content, String path, String charset) throws IORuntimeException {
        return appendString(content, touch(path), charset);
    }

    /**
     * 将String写入文件，追加模式
     *
     * @param content 写入的内容
     * @param file    文件
     * @param charset 字符集
     * @return 写入的文件
     * @throws IORuntimeException
     */
    public static File appendString(String content, File file, String charset) throws IORuntimeException {
        return appendString(content, file, CharsetCommand.charset(charset));
    }

    /**
     * 将String写入文件，追加模式
     *
     * @param content 写入的内容
     * @param file    文件
     * @param charset 字符集
     * @return 写入的文件
     * @throws IORuntimeException
     */
    public static File appendString(String content, File file, Charset charset) throws IORuntimeException {
        return FileWriter.create(file, charset).append(content);
    }

    /**
     * 将列表写入文件，追加模式
     *
     * @param list    列表
     * @param path    绝对路径
     * @param charset 字符集
     * @throws IORuntimeException
     */
    public static <T> void appendLines(Collection<T> list, String path, String charset) throws IORuntimeException {
        writeLines(list, path, charset, true);
    }

    public static <T> void appendLines(Collection<T> list, String path, Charset charset) throws IORuntimeException {
        writeLines(list, FileCommand.file(path), charset, true);
    }

    /**
     * 将String写入文件，覆盖模式
     *
     * @param content 写入的内容
     * @param path    文件路径
     * @param charset 字符集
     * @return 写入的文件
     * @throws IORuntimeException
     */
    public static File writeLine(String content, String path, String charset) throws IORuntimeException {
        return writeString(content, touch(path), charset);
    }

    /**
     * 将String写入文件，覆盖模式
     *
     * @param content 写入的内容
     * @param file    文件
     * @param charset 字符集
     * @throws IORuntimeException
     */
    public static File writeLine(String content, File file, String charset) throws IORuntimeException {
        return FileWriter.create(file, CharsetCommand.charset(charset)).writeLine(content);
    }

    /**
     * 将列表写入文件，覆盖模式
     *
     * @param list    列表
     * @param path    绝对路径
     * @param charset 字符集
     * @throws IORuntimeException
     */
    public static <T> void writeLines(Collection<T> list, String path, String charset) throws IORuntimeException {
        writeLines(list, path, charset, false);
    }

    /**
     * 将列表写入文件
     *
     * @param list     列表
     * @param path     文件路径
     * @param charset  字符集
     * @param isAppend 是否追加
     * @throws IORuntimeException
     */
    public static <T> File writeLines(Collection<T> list, String path, String charset, boolean isAppend)
            throws IORuntimeException {
        return writeLines(list, file(path), charset, isAppend);
    }

    /**
     * 将列表写入文件
     *
     * @param list     列表
     * @param file     文件
     * @param charset  字符集
     * @param isAppend 是否追加
     * @throws IORuntimeException
     */
    public static <T> File writeLines(Collection<T> list, File file, String charset, boolean isAppend)
            throws IORuntimeException {
        return writeLines(list, file, CharsetCommand.charset(charset), isAppend);
    }

    public static <T> File writeLines(Collection<T> list, File file, Charset charset, boolean isAppend)
            throws IORuntimeException {
        FileWriter fileWriter = FileWriter.create(file, charset);
        if (isAppend) {
            return fileWriter.appendLines(list);
        } else {
            return fileWriter.writeLines(list);
        }
    }

    /**
     * 写数据到文件中
     *
     * @param data 数据
     * @param path 目标文件
     * @return File
     * @throws IORuntimeException
     */
    public static File writeBytes(byte[] data, String path) throws IORuntimeException {
        return writeBytes(data, touch(path));
    }

    /**
     * 写数据到文件中
     *
     * @param dest 目标文件
     * @param data 数据
     * @return dest
     * @throws IORuntimeException
     */
    public static File writeBytes(byte[] data, File dest) throws IORuntimeException {
        return writeBytes(data, dest, 0, data.length, false);
    }

    /**
     * 写入数据到文件
     *
     * @param data   数据
     * @param dest   目标文件
     * @param off    数据开始位置
     * @param len    数据长度
     * @param append 是否追加模式
     * @return File
     * @throws IORuntimeException
     */
    public static File writeBytes(byte[] data, File dest, int off, int len, boolean append) throws IORuntimeException {
        return FileWriter.create(dest).write(data, off, len);
    }

    /**
     * 将String写入文件，追加模式
     *
     * @param content 写入的内容
     * @param path    文件路径
     * @param charset 字符集
     * @return 写入的文件
     * @throws IORuntimeException
     */
    public static File appendLine(String content, String path, String charset) throws IORuntimeException {
        return appendString(content, touch(path), charset);
    }

    /**
     * 将String写入文件，追加模式
     *
     * @param content 写入的内容
     * @param file    文件
     * @param charset 字符集
     * @return 写入的文件
     * @throws IORuntimeException
     */
    public static File appendLine(String content, File file, String charset) throws IORuntimeException {
        return appendString(content, file, CharsetCommand.charset(charset));
    }

    /**
     * 将String写入文件，追加模式
     *
     * @param content 写入的内容
     * @param file    文件
     * @param charset 字符集
     * @return 写入的文件
     * @throws IORuntimeException
     */
    public static File appendLine(String content, File file, Charset charset) throws IORuntimeException {
        return FileWriter.create(file, charset).appendLine(content);
    }

    /**
     * 将流的内容写入文件<br>
     *
     * @param dest 目标文件
     * @param in   输入流
     * @return dest
     * @throws IORuntimeException
     */
    public static File writeFromStream(InputStream in, File dest) throws IORuntimeException {
        return FileWriter.create(dest).writeFromStream(in);
    }

    /**
     * 将流的内容写入文件<br>
     *
     * @param in           输入流
     * @param fullFilePath 文件绝对路径
     * @return dest
     * @throws IORuntimeException
     */
    public static File writeFromStream(InputStream in, String fullFilePath) throws IORuntimeException {
        return writeFromStream(in, touch(fullFilePath));
    }

    /**
     * 将文件写入流中
     *
     * @param file 文件
     * @param out  流
     * @return File
     * @throws IORuntimeException
     */
    public static File writeToStream(File file, OutputStream out) throws IORuntimeException {
        return FileReader.create(file).writeToStream(out);
    }

    /**
     * 将流的内容写入文件<br>
     *
     * @param fullFilePath 文件绝对路径
     * @param out          输出流
     * @throws IORuntimeException
     */
    public static void writeToStream(String fullFilePath, OutputStream out) throws IORuntimeException {
        writeToStream(touch(fullFilePath), out);
    }

    /**
     * 可读的文件大小
     *
     * @param file 文件
     * @return 大小
     */
    public static String readableFileSize(File file) {
        return readableFileSize(file.length());
    }

    /**
     * 可读的文件大小<br>
     * 参考 http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc
     *
     * @param size Long类型大小
     * @return 大小
     */
    public static String readableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[] {"B", "kB", "MB", "GB", "TB", "EB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private static boolean initWindowsOs() {
        // initialize once as System.getProperty is not fast
        String osName = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        return osName.contains("windows");
    }

    public static File getUserDir() {
        return USER_DIR;
    }

    /**
     * Returns true, if the OS is windows
     */
    public static boolean isWindows() {
        return windowsOs;
    }

    /**
     * Compacts a path by stacking it and reducing <tt>..</tt>, and uses OS specific file separators (eg
     * {@link java.io.File#separator}).
     */
    public static String compactPath(String path) {
        return compactPath(path, "" + File.separatorChar);
    }

    /**
     * Normalizes the path to cater for Windows and other platforms
     */
    public static String normalizePath(String path) {
        if (path == null) {
            return null;
        }

        if (isWindows()) {
            // special handling for Windows where we need to convert / to \\
            return path.replace('/', '\\');
        } else {
            // for other systems make sure we use / as separators
            return path.replace('\\', '/');
        }
    }

    /**
     * Compacts a path by stacking it and reducing <tt>..</tt>, and uses the given separator.
     */
    public static String compactPath(String path, char separator) {
        return compactPath(path, "" + separator);
    }

    /**
     * Compacts a path by stacking it and reducing <tt>..</tt>, and uses the given separator.
     */
    public static String compactPath(String path, String separator) {
        if (path == null) {
            return null;
        }

        // only normalize if contains a path separator
        if (path.indexOf('/') == -1 && path.indexOf('\\') == -1) {
            return path;
        }

        // need to normalize path before compacting
        path = normalizePath(path);

        // preserve ending slash if given in input path
        boolean endsWithSlash = path.endsWith("/") || path.endsWith("\\");

        // preserve starting slash if given in input path
        boolean startsWithSlash = path.startsWith("/") || path.startsWith("\\");

        Deque<String> stack = new ArrayDeque<>();

        // separator can either be windows or unix style
        String separatorRegex = "\\\\|/";
        String[] parts = path.split(separatorRegex);
        for (String part : parts) {
            if (part.equals("..") && !stack.isEmpty() && !"..".equals(stack.peek())) {
                // only pop if there is a previous path, which is not a ".." path either
                stack.pop();
            } else if (part.equals(".") || part.isEmpty()) {
                // do nothing because we don't want a path like foo/./bar or foo//bar
            } else {
                stack.push(part);
            }
        }

        // build path based on stack
        StringBuilder sb = new StringBuilder();

        if (startsWithSlash) {
            sb.append(separator);
        }

        // now we build back using FIFO so need to use descending
        for (Iterator<String> it = stack.descendingIterator(); it.hasNext(); ) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(separator);
            }
        }

        if (endsWithSlash && stack.size() > 0) {
            sb.append(separator);
        }

        return sb.toString();
    }

    private static File createNewTempDir() {
        String s = System.getProperty("java.io.tmpdir");
        File checkExists = new File(s);
        if (!checkExists.exists()) {
            throw new RuntimeException("The directory "
                    + checkExists.getAbsolutePath()
                    + " does not exist, please set java.io.tempdir"
                    + " to an existing directory");
        }

        if (!checkExists.canWrite()) {
            throw new RuntimeException("The directory "
                    + checkExists.getAbsolutePath()
                    + " is not writable, please set java.io.tempdir"
                    + " to a writable directory");
        }

        // create a sub folder with a random number
        Random ran = new Random();
        int x = ran.nextInt(1000000);
        File f = new File(s, "camel-tmp-" + x);
        int count = 0;
        // Let us just try 100 times to avoid the infinite loop
        while (!f.mkdir()) {
            count++;
            if (count >= 100) {
                throw new RuntimeException("Camel cannot a temp directory from"
                        + checkExists.getAbsolutePath()
                        + " 100 times , please set java.io.tempdir"
                        + " to a writable directory");
            }
            x = ran.nextInt(1000000);
            f = new File(s, "camel-tmp-" + x);
        }

        return f;
    }

    private static synchronized File getDefaultTempDir() {
        if (defaultTempDir != null && defaultTempDir.exists()) {
            return defaultTempDir;
        }

        defaultTempDir = createNewTempDir();

        // create shutdown hook to remove the temp dir
        shutdownHook = new Thread() {
            @Override
            public void run() {
                removeDir(defaultTempDir);
            }
        };
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        return defaultTempDir;
    }

    public static File createTempFile(String prefix, String suffix, File parentDir) throws IOException {
        // TODO: parentDir should be mandatory
        File parent = (parentDir == null) ? getDefaultTempDir() : parentDir;

        if (suffix == null) {
            suffix = ".tmp";
        }
        if (prefix == null) {
            prefix = "camel";
        } else if (prefix.length() < 3) {
            prefix = prefix + "camel";
        }

        // create parent folder
        parent.mkdirs();

        return File.createTempFile(prefix, suffix, parent);
    }

    /**
     * Strip any leading separators
     */
    public static String stripLeadingSeparator(String name) {
        if (name == null) {
            return null;
        }
        while (name.startsWith("/") || name.startsWith(File.separator)) {
            name = name.substring(1);
        }
        return name;
    }

    /**
     * Does the name start with a leading separator
     */
    public static boolean hasLeadingSeparator(String name) {
        if (name == null) {
            return false;
        }
        if (name.startsWith("/") || name.startsWith(File.separator)) {
            return true;
        }
        return false;
    }

    /**
     * Strip first leading separator
     */
    public static String stripFirstLeadingSeparator(String name) {
        if (name == null) {
            return null;
        }
        if (name.startsWith("/") || name.startsWith(File.separator)) {
            name = name.substring(1);
        }
        return name;
    }

    /**
     * Strip any trailing separators
     */
    public static String stripTrailingSeparator(String name) {
        if (ObjectCommand.isEmpty(name)) {
            return name;
        }

        String s = name;

        // there must be some leading text, as we should only remove trailing separators
        while (s.endsWith("/") || s.endsWith(File.separator)) {
            s = s.substring(0, s.length() - 1);
        }

        // if the string is empty, that means there was only trailing slashes, and no leading text
        // and so we should then return the original name as is
        if (ObjectCommand.isEmpty(s)) {
            return name;
        } else {
            // return without trailing slashes
            return s;
        }
    }

    /**
     * Strips any leading paths
     */
    public static String stripPath(String name) {
        if (name == null) {
            return null;
        }
        int posUnix = name.lastIndexOf('/');
        int posWin = name.lastIndexOf('\\');
        int pos = Math.max(posUnix, posWin);

        if (pos != -1) {
            return name.substring(pos + 1);
        }
        return name;
    }

    public static String stripExt(String name) {
        return stripExt(name, false);
    }

    public static String stripExt(String name, boolean singleMode) {
        if (name == null) {
            return null;
        }

        // the name may have a leading path
        int posUnix = name.lastIndexOf('/');
        int posWin = name.lastIndexOf('\\');
        int pos = Math.max(posUnix, posWin);

        if (pos > 0) {
            String onlyName = name.substring(pos + 1);
            int pos2 = singleMode ? onlyName.lastIndexOf('.') : onlyName.indexOf('.');
            if (pos2 > 0) {
                return name.substring(0, pos + pos2 + 1);
            }
        } else {
            // if single ext mode, then only return last extension
            int pos2 = singleMode ? name.lastIndexOf('.') : name.indexOf('.');
            if (pos2 > 0) {
                return name.substring(0, pos2);
            }
        }

        return name;
    }

    public static String onlyExt(String name) {
        return onlyExt(name, false);
    }

    public static String onlyExt(String name, boolean singleMode) {
        if (name == null) {
            return null;
        }
        name = stripPath(name);

        // extension is the first dot, as a file may have double extension such as .tar.gz
        // if single ext mode, then only return last extension
        int pos = singleMode ? name.lastIndexOf('.') : name.indexOf('.');
        if (pos != -1) {
            return name.substring(pos + 1);
        }
        return null;
    }

    /**
     * Returns only the leading path (returns <tt>null</tt> if no path)
     */
    public static String onlyPath(String name) {
        if (name == null) {
            return null;
        }

        int posUnix = name.lastIndexOf('/');
        int posWin = name.lastIndexOf('\\');
        int pos = Math.max(posUnix, posWin);

        if (pos > 0) {
            return name.substring(0, pos);
        } else if (pos == 0) {
            // name is in the root path, so extract the path as the first char
            return name.substring(0, 1);
        }
        // no path in name
        return null;
    }

    public static void removeDir(File d) {
        String[] list = d.list();
        if (list == null) {
            list = new String[0];
        }
        for (String s : list) {
            File f = new File(d, s);
            if (f.isDirectory()) {
                removeDir(f);
            } else {
                delete(f);
            }
        }
        delete(d);
    }

    private static void delete(File f) {
        if (!f.delete()) {
            if (isWindows()) {
                System.gc();
            }
            try {
                Thread.sleep(RETRY_SLEEP_MILLIS);
            } catch (InterruptedException ex) {
                // Ignore Exception
            }
            if (!f.delete()) {
                f.deleteOnExit();
            }
        }
    }

    /**
     * Renames a file.
     *
     * @param from                      the from file
     * @param to                        the to file
     * @param copyAndDeleteOnRenameFail whether to fallback and do copy and delete, if renameTo fails
     * @return <tt>true</tt> if the file was renamed, otherwise <tt>false</tt>
     * @throws java.io.IOException is thrown if error renaming file
     */
    public static boolean renameFile(File from, File to, boolean copyAndDeleteOnRenameFail) throws IOException {
        // do not try to rename non existing files
        if (!from.exists()) {
            return false;
        }

        // some OS such as Windows can have problem doing rename IO operations so we may need to
        // retry a couple of times to let it work
        boolean renamed = false;
        int count = 0;
        while (!renamed && count < 3) {

            renamed = from.renameTo(to);
            if (!renamed && count > 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
            count++;
        }

        // we could not rename using renameTo, so lets fallback and do a copy/delete approach.
        // for example if you move files between different file systems (linux -> windows etc.)
        if (!renamed && copyAndDeleteOnRenameFail) {
            // now do a copy and delete as all rename attempts failed
            renamed = renameFileUsingCopy(from, to);
        }
        return renamed;
    }

    /**
     * Rename file using copy and delete strategy. This is primarily used in environments where the regular rename
     * operation is unreliable.
     *
     * @param from the file to be renamed
     * @param to   the new target file
     * @return <tt>true</tt> if the file was renamed successfully, otherwise <tt>false</tt>
     * @throws IOException If an I/O error occurs during copy or delete operations.
     */
    public static boolean renameFileUsingCopy(File from, File to) throws IOException {
        // do not try to rename non existing files
        if (!from.exists()) {
            return false;
        }
        copyFile(from, to);
        if (!deleteFile(from)) {
            throw new IOException("Renaming file from '" + from + "' to '" + to + "' failed: Cannot delete file '"
                    + from + "' after copy succeeded");
        }

        return true;
    }

    /**
     * Copies the file
     *
     * @param from the source file
     * @param to   the destination file
     * @throws IOException If an I/O error occurs during copy operation
     */
    public static void copyFile(File from, File to) throws IOException {
        Files.copy(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Deletes the file.
     * <p/>
     * This implementation will attempt to delete the file up till three times with one second delay, which can mitigate
     * problems on deleting files on some platforms such as Windows.
     *
     * @param file the file to delete
     */
    public static boolean deleteFile(File file) {
        // do not try to delete non existing files
        if (!file.exists()) {
            return false;
        }

        // some OS such as Windows can have problem doing delete IO operations so we may need to
        // retry a couple of times to let it work
        boolean deleted = false;
        int count = 0;
        while (!deleted && count < 3) {
            deleted = file.delete();
            if (!deleted && count > 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
            count++;
        }
        return deleted;
    }

    /**
     * Is the given file an absolute file.
     * <p/>
     * Will also work around issue on Windows to consider files on Windows starting with a \ as absolute files. This
     * makes the logic consistent across all OS platforms.
     *
     * @param file the file
     * @return <tt>true</ff> if its an absolute path, <tt>false</tt> otherwise.
     */
    public static boolean isAbsolute(File file) {
        if (isWindows()) {
            // special for windows
            String path = file.getPath();
            if (path.startsWith(File.separator)) {
                return true;
            }
        }
        return file.isAbsolute();
    }

    /**
     * Creates a new file.
     *
     * @param file the file
     * @return <tt>true</tt> if created a new file, <tt>false</tt> otherwise
     * @throws IOException is thrown if error creating the new file
     */
    public static boolean createNewFile(File file) throws IOException {
        // need to check first
        if (file.exists()) {
            return false;
        }
        try {
            return file.createNewFile();
        } catch (IOException e) {
            // and check again if the file was created as createNewFile may create the file
            // but throw a permission error afterwards when using some NAS
            if (file.exists()) {
                return true;
            } else {
                throw e;
            }
        }
    }

    public static boolean downloader(String url) {
        String path = LocationCommand.classPath() + File.separator + url.substring(url.lastIndexOf('/') + 1);
        return downloader(url, path);
    }

    public static boolean downloader(DirectDownloader directDownloader, DownloadTask downloadTask) {
        Thread t = new Thread(directDownloader);
        try {
            directDownloader.download(downloadTask);
            t.start();
        } catch (Exception e) {
            logger.error("did not download task:{}", downloadTask, e);
            if (t.isAlive()) {
                t.interrupt();
            }
            return false;
        }
        return true;
    }

    public static boolean downloaderWithOutListener(DirectDownloader directDownloader, String url,
                                                    String path) {
        try {
            DownloadTask downloadTask = new DownloadTask(new URL(url), new FileOutputStream(path));
            downloader(directDownloader, downloadTask);
        } catch (Exception e) {
            logger.error("did not download file:{}, path:{}", url, path, e);
            return false;
        }
        return true;
    }

    public static boolean downloader(DirectDownloader directDownloader, DownloadListener downloadListener, String url,
                                     String path) {
        try {
            DownloadTask downloadTask = new DownloadTask(new URL(url), new FileOutputStream(path), downloadListener);
            directDownloader.download(downloadTask);
        } catch (Exception e) {
            logger.error("did not download url:{}, path:{}", url, path, e);
            return false;
        }
        return true;
    }

    public static boolean downloaderWithListener(DirectDownloader directDownloader, String url, String path) {
        try {
            DownloadTask downloadTask =
                    new DownloadTask(new URL(url), new FileOutputStream(path)).addListener(new DownloadListener() {
                        int size;

                        @Override
                        public void onStart(String fname, int size) {
                            logger.info("starting to downloading " + fname + " of size " + size);
                            this.size = size;
                            directDownloader.updateProgress(0);
                        }

                        @Override
                        public void onUpdate(int bytes, int totalDownloaded) {
                            // (double) totalDownloaded / size
                            logger.debug(Thread.currentThread() + " downloader is downloading {} percent.",
                                    (double) totalDownloaded / size);
                        }

                        @Override
                        public void onComplete() {
                            logger.debug(Thread.currentThread() + " downloader downloaded {} success.", path);
                        }

                        @Override
                        public void onCancel() {
                            logger.debug(Thread.currentThread() + " downloader undownloaded {} success.", path);
                        }
                    });
            return downloader(directDownloader, downloadTask);
        } catch (Exception e) {
            logger.error("did not download url:{}, path:{}", url, path, e);
        }
        return false;
    }

    public static boolean downloader(String url, String path) {
        DirectDownloader directDownloader = new DirectDownloader();
        return downloaderWithOutListener(directDownloader, url, path);
    }

    /**
     * Return whether the given resource location is a URL: either a special "classpath" pseudo URL or a standard URL.
     *
     * @param resourceLocation the location String to check
     * @return whether the location qualifies as a URL
     * @see #CLASSPATH_URL_PREFIX
     * @see java.net.URL
     */
    public static boolean isUrl(@Nullable String resourceLocation) {
        if (resourceLocation == null) {
            return false;
        }
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            return true;
        }
        try {
            new URL(resourceLocation);
            return true;
        } catch (MalformedURLException ex) {
            return false;
        }
    }

    /**
     * Resolve the given resource location to a {@code java.net.URL}.
     * <p>
     * Does not check whether the URL actually exists; simply returns the URL that the given location would correspond
     * to.
     *
     * @param resourceLocation the resource location to resolve: either a "classpath:" pseudo URL, a "file:" URL, or a
     *                         plain file path
     * @return a corresponding URL object
     * @throws FileNotFoundException if the resource cannot be resolved to a URL
     */
    public static URL getURL(String resourceLocation) throws FileNotFoundException {
        AssertCommand.notNull(resourceLocation, "Resource location must not be null");
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
            ClassLoader cl = ClassCommand.getDefaultClassLoader();
            URL url = (cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path));
            if (url == null) {
                String description = "class path resource [" + path + "]";
                throw new FileNotFoundException(description +
                        " cannot be resolved to URL because it does not exist");
            }
            return url;
        }
        try {
            // try URL
            return new URL(resourceLocation);
        } catch (MalformedURLException ex) {
            // no URL -> treat as file path
            try {
                return new File(resourceLocation).toURI().toURL();
            } catch (MalformedURLException ex2) {
                throw new FileNotFoundException("Resource location [" + resourceLocation +
                        "] is neither a URL not a well-formed file path");
            }
        }
    }

    /**
     * Resolve the given resource location to a {@code java.io.File}, i.e. to a file in the file system.
     * <p>
     * Does not check whether the file actually exists; simply returns the File that the given location would correspond
     * to.
     *
     * @param resourceLocation the resource location to resolve: either a "classpath:" pseudo URL, a "file:" URL, or a
     *                         plain file path
     * @return a corresponding File object
     * @throws FileNotFoundException if the resource cannot be resolved to a file in the file system
     */
    public static File getFile(String resourceLocation) throws FileNotFoundException {
        AssertCommand.notNull(resourceLocation, "Resource location must not be null");
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
            String description = "class path resource [" + path + "]";
            ClassLoader cl = ClassCommand.getDefaultClassLoader();
            URL url = (cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path));
            if (url == null) {
                throw new FileNotFoundException(description +
                        " cannot be resolved to absolute file path because it does not exist");
            }
            return getFile(url, description);
        }
        try {
            // try URL
            return getFile(new URL(resourceLocation));
        } catch (MalformedURLException ex) {
            // no URL -> treat as file path
            return new File(resourceLocation);
        }
    }

    /**
     * Resolve the given resource URL to a {@code java.io.File}, i.e. to a file in the file system.
     *
     * @param resourceUrl the resource URL to resolve
     * @return a corresponding File object
     * @throws FileNotFoundException if the URL cannot be resolved to a file in the file system
     */
    public static File getFile(URL resourceUrl) throws FileNotFoundException {
        return getFile(resourceUrl, "URL");
    }

    /**
     * Resolve the given resource URL to a {@code java.io.File}, i.e. to a file in the file system.
     *
     * @param resourceUrl the resource URL to resolve
     * @param description a description of the original resource that the URL was created for (for example, a class path
     *                    location)
     * @return a corresponding File object
     * @throws FileNotFoundException if the URL cannot be resolved to a file in the file system
     */
    public static File getFile(URL resourceUrl, String description) throws FileNotFoundException {
        AssertCommand.notNull(resourceUrl, "Resource URL must not be null");
        if (!URL_PROTOCOL_FILE.equals(resourceUrl.getProtocol())) {
            throw new FileNotFoundException(
                    description + " cannot be resolved to absolute file path " +
                            "because it does not reside in the file system: " + resourceUrl);
        }
        try {
            return new File(toURI(resourceUrl).getSchemeSpecificPart());
        } catch (URISyntaxException ex) {
            // Fallback for URLs that are not valid URIs (should hardly ever happen).
            return new File(resourceUrl.getFile());
        }
    }

    /**
     * Resolve the given resource URI to a {@code java.io.File}, i.e. to a file in the file system.
     *
     * @param resourceUri the resource URI to resolve
     * @return a corresponding File object
     * @throws FileNotFoundException if the URL cannot be resolved to a file in the file system
     * @since 2.5
     */
    public static File getFile(URI resourceUri) throws FileNotFoundException {
        return getFile(resourceUri, "URI");
    }

    /**
     * Resolve the given resource URI to a {@code java.io.File}, i.e. to a file in the file system.
     *
     * @param resourceUri the resource URI to resolve
     * @param description a description of the original resource that the URI was created for (for example, a class path
     *                    location)
     * @return a corresponding File object
     * @throws FileNotFoundException if the URL cannot be resolved to a file in the file system
     * @since 2.5
     */
    public static File getFile(URI resourceUri, String description) throws FileNotFoundException {
        AssertCommand.notNull(resourceUri, "Resource URI must not be null");
        if (!URL_PROTOCOL_FILE.equals(resourceUri.getScheme())) {
            throw new FileNotFoundException(
                    description + " cannot be resolved to absolute file path " +
                            "because it does not reside in the file system: " + resourceUri);
        }
        return new File(resourceUri.getSchemeSpecificPart());
    }

    /**
     * Determine whether the given URL points to a resource in the file system, i.e. has protocol "file", "vfsfile" or
     * "vfs".
     *
     * @param url the URL to check
     * @return whether the URL has been identified as a file system URL
     */
    public static boolean isFileURL(URL url) {
        String protocol = url.getProtocol();
        return (URL_PROTOCOL_FILE.equals(protocol) || URL_PROTOCOL_VFSFILE.equals(protocol) ||
                URL_PROTOCOL_VFS.equals(protocol));
    }

    /**
     * Determine whether the given URL points to a resource in a jar file. i.e. has protocol "jar", "war, ""zip",
     * "vfszip" or "wsjar".
     *
     * @param url the URL to check
     * @return whether the URL has been identified as a JAR URL
     */
    public static boolean isJarURL(URL url) {
        String protocol = url.getProtocol();
        return (URL_PROTOCOL_JAR.equals(protocol) || URL_PROTOCOL_WAR.equals(protocol) ||
                URL_PROTOCOL_ZIP.equals(protocol) || URL_PROTOCOL_VFSZIP.equals(protocol) ||
                URL_PROTOCOL_WSJAR.equals(protocol));
    }

    /**
     * Determine whether the given URL points to a jar file itself, that is, has protocol "file" and ends with the
     * ".jar" extension.
     *
     * @param url the URL to check
     * @return whether the URL has been identified as a JAR file URL
     * @since 4.1
     */
    public static boolean isJarFileURL(URL url) {
        return (URL_PROTOCOL_FILE.equals(url.getProtocol()) &&
                url.getPath().toLowerCase().endsWith(JAR_FILE_EXTENSION));
    }

    /**
     * Extract the URL for the actual jar file from the given URL (which may point to a resource in a jar file or to a
     * jar file itself).
     *
     * @param jarUrl the original URL
     * @return the URL for the actual jar file
     * @throwss MalformedURLException if no valid jar file URL could be extracted
     */
    public static URL extractJarFileURL(URL jarUrl) throws MalformedURLException {
        String urlFile = jarUrl.getFile();
        int separatorIndex = urlFile.indexOf(JAR_URL_SEPARATOR);
        if (separatorIndex != -1) {
            String jarFile = urlFile.substring(0, separatorIndex);
            try {
                return new URL(jarFile);
            } catch (MalformedURLException ex) {
                // Probably no protocol in original jar URL, like "jar:C:/mypath/myjar.jar".
                // This usually indicates that the jar file resides in the file system.
                if (!jarFile.startsWith("/")) {
                    jarFile = "/" + jarFile;
                }
                return new URL(FILE_URL_PREFIX + jarFile);
            }
        } else {
            return jarUrl;
        }
    }

    /**
     * Extract the URL for the outermost archive from the given jar/war URL (which may point to a resource in a jar file
     * or to a jar file itself).
     * <p>
     * In the case of a jar file nested within a war file, this will return a URL to the war file since that is the one
     * resolvable in the file system.
     *
     * @param jarUrl the original URL
     * @return the URL for the actual jar file
     * @throwss MalformedURL Exception if no valid jar file URL could be extracted
     * @see #extractJarFileURL(URL)
     * @since 4.1.8
     */
    public static URL extractArchiveURL(URL jarUrl) throws MalformedURLException {
        String urlFile = jarUrl.getFile();

        int endIndex = urlFile.indexOf(WAR_URL_SEPARATOR);
        if (endIndex != -1) {
            // Tomcat's "war:file:...mywar.war*/WEB-INF/lib/myjar.jar!/myentry.txt"
            String warFile = urlFile.substring(0, endIndex);
            if (URL_PROTOCOL_WAR.equals(jarUrl.getProtocol())) {
                return new URL(warFile);
            }
            int startIndex = warFile.indexOf(WAR_URL_PREFIX);
            if (startIndex != -1) {
                return new URL(warFile.substring(startIndex + WAR_URL_PREFIX.length()));
            }
        }

        // Regular "jar:file:...myjar.jar!/myentry.txt"
        return extractJarFileURL(jarUrl);
    }

    /**
     * Create a URI instance for the given URL, replacing spaces with "%20" URI encoding first.
     *
     * @param url the URL to convert into a URI instance
     * @return the URI instance
     * @throws URISyntaxException if the URL wasn't a valid URI
     * @see java.net.URL#toURI()
     */
    public static URI toURI(URL url) throws URISyntaxException {
        return toURI(url.toString());
    }

    /**
     * Create a URI instance for the given location String, replacing spaces with "%20" URI encoding first.
     *
     * @param location the location String to convert into a URI instance
     * @return the URI instance
     * @throwss URISyntaxException if the location wasn't a valid URI
     */
    public static URI toURI(String location) throws URISyntaxException {
        return new URI(StringCommand.replace(location, " ", "%20"));
    }

    /**
     * Set the {@link URL Connection # setUseCaches "useCaches"} flag on the given connection, preferring {@code false}
     * but leaving the flag at {@code true} for JNLP based resources.
     *
     * @param con the URLConnection to set the flag on
     */
    public static void useCachesIfNecessary(URLConnection con) {
        con.setUseCaches(con.getClass().getSimpleName().startsWith("JNLP"));
    }

    public static boolean downloadInDirectory(String src, String directory) {
        UriBuilder uriBuilder = UriCommand.uriBuilder(src);
        URL srcUrl = uriBuilder.buildURL();
        String filename = uriBuilder.fileName();
        String path = directory + File.separator + filename;
        FileCommand.mkdir(directory);
        try {
            OutputStream outputStream = new FileOutputStream(path);
            return download(srcUrl, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean download(String src, String dec) {
        dec = LocationCommand.pathValue(dec);
        return downloadWithDirectory(src, dec);
    }

    public static boolean downloadWithDirectory(String src, String dec) {
        UriBuilder uriBuilder = UriCommand.uriBuilder(src);
        URL srcUrl = uriBuilder.buildURL();
        if (srcUrl == null) {
            throw new UnsupportedOperationException("should not be null for src URL object url when download:{}" + src);
        }
        String realPath = LocationCommand.valuePath(dec);
        FileCommand.mkParentDirs(realPath);
        try {
            OutputStream outputStream = new FileOutputStream(realPath);
            return download(srcUrl, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean download(URL src, OutputStream dsc) {
        HttpConnector httpConnector = new HttpConnector();
        HttpURLConnection conn = null;
        try {
            conn = httpConnector.getSecureConnection(src, null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        conn.setReadTimeout(15000);
        conn.setDoOutput(true);
        InputStream is = null;
        try {
            conn.connect();
            is = conn.getInputStream();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        OutputStream os = dsc;
        byte[] buff = new byte[2048];
        int res;
        while (true) {
            try {
                if (!((res = is.read(buff)) != -1)) {
                    break;
                }
                os.write(buff, 0, res);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try {
            is.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
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
        if (isExist(filename)) {
            try {
                inputStream = inputStream(filename);
            } catch (RuntimeException e) {
                e.printStackTrace();
                inputStream = null;
            }
        } else {
            inputStream = tryUserSystemExternal(filename);
        }
        if (inputStream == null) {
            try {
                inputStream = inputStream(LocationCommand.pathValue(filename));
            } catch (RuntimeException e) {
                try {
                    inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(filename);
                } catch (Exception eeeee) {

                } finally {
                    if (inputStream == null) {
                        try {
                            inputStream = inputStream(new File(System.getProperty(filename)));
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

}
