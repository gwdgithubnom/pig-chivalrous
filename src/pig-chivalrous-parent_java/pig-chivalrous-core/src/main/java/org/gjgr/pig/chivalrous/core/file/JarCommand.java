package org.gjgr.pig.chivalrous.core.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * @Author gwd
 * @Time 08-22-2018  Wednesday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public final class JarCommand {
    private String jarFileName;
    private Map<String, Long> hashSizes = new HashMap<String, Long>();
    private Map<String, Object> hashJarContents = new HashMap<String, Object>();

    public JarCommand(String jarFileName) throws Exception {
        this.jarFileName = jarFileName;
        ZipFile zipFile = new ZipFile(this.jarFileName);

        Enumeration<ZipEntry> e = (Enumeration<ZipEntry>) zipFile.entries();
        while (e.hasMoreElements()) {
            ZipEntry zipEntry = e.nextElement();
            if (!zipEntry.isDirectory()) {
                hashSizes.put(getSimpleName(zipEntry.getName()), zipEntry.getSize());
            }
        }
        zipFile.close();

        // extract resources and put them into the hashMap.
        FileInputStream fis = new FileInputStream(jarFileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ZipInputStream zis = new ZipInputStream(bis);
        ZipEntry ze = null;

        while ((ze = zis.getNextEntry()) != null) {
            if (ze.isDirectory()) {
                continue;
            } else {
                long size = (int) ze.getSize();
                // -1 means unknown size.
                if (size == -1) {
                    size = hashSizes.get(ze.getName());
                }

                byte[] b = new byte[(int) size];
                int rb = 0;
                int chunk = 0;
                while (((int) size - rb) > 0) {
                    chunk = zis.read(b, rb, (int) size - rb);
                    if (chunk == -1) {
                        break;
                    }
                    rb += chunk;
                }

                hashJarContents.put(ze.getName(), b);
            }
        }
        zis.close();
    }

    public static void read(URL jarUrl, InputStreamCallback callback) throws IOException {
        if (!"jar".equals(jarUrl.getProtocol())) {
            throw new IllegalArgumentException("Jar protocol is expected but get " + jarUrl.getProtocol());
        }
        if (callback == null) {
            throw new IllegalArgumentException("Callback must not be null");
        }
        String jarPath = jarUrl.getPath().substring(5);
        String[] paths = jarPath.split("!");
        FileInputStream jarFileInputStream = new FileInputStream(paths[0]);
        readStream(jarFileInputStream, paths[0], 1, paths, callback);
    }

    private static void readStream(InputStream jarFileInputStream, String name, int i, String[] paths, InputStreamCallback callback)
            throws IOException {
        if (i == paths.length) {
            callback.onFile(name, jarFileInputStream);
            return;
        }
        ZipInputStream jarInputStream = new ZipInputStream(jarFileInputStream);
        try {
            ZipEntry jarEntry = null;
            while ((jarEntry = jarInputStream.getNextEntry()) != null) {
                String jarEntryName = "/" + jarEntry.getName();
                if (!jarEntry.isDirectory() && jarEntryName.startsWith(paths[i])) {
                    byte[] byteArray = copyStream(jarInputStream, jarEntry);
                    readStream(new ByteArrayInputStream(byteArray), jarEntryName, i + 1, paths, callback);
                }
            }
        } finally {
            jarInputStream.close();
        }
    }

    private static byte[] copyStream(InputStream in, ZipEntry entry)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        long size = entry.getSize();
        if (size > -1) {
            byte[] buffer = new byte[1024 * 4];
            int n = 0;
            long count = 0;
            while (-1 != (n = in.read(buffer)) && count < size) {
                baos.write(buffer, 0, n);
                count += n;
            }
        } else {
            while (true) {
                int b = in.read();
                if (b == -1) {
                    break;
                }
                baos.write(b);
            }
        }
        baos.close();
        return baos.toByteArray();
    }

    public byte[] getResource(String name) {
        return (byte[]) hashJarContents.get(name);
    }

    private String getSimpleName(String entryName) {
        // Remove ".jar" extension
        int index = entryName.indexOf("/");
        String fileNameWithoutExt = entryName.substring(index, entryName.length());
        // FileSystem fileSystem = FileSystems.newFileSystem(entryName, Collections.<String, Object>emptyMap());
        return fileNameWithoutExt;
    }

    public static interface InputStreamCallback {
        void onFile(String name, InputStream is) throws IOException;
    }

}
