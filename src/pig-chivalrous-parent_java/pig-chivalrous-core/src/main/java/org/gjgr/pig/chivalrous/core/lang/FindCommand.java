package org.gjgr.pig.chivalrous.core.lang;

import org.gjgr.pig.chivalrous.core.io.resource.LocationCommand;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author gwd
 * @Time 01-24-2019 Thursday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public class FindCommand {

    public static File[] findFiles(File file, String filename) {
        List<File> files = new LinkedList<>();
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                if (f.getName().equalsIgnoreCase(filename)) {
                    files.add(f);
                }
            } else if (f.isDirectory()) {
                File[] ff = findFiles(f, filename);
                if (ff.length != 0) {
                    files.addAll(CollectionCommand.newArrayList(ff));
                }
            } else {
                continue;
            }
        }
        if (files.size() == 0) {
            return new File[0];
        } else {
            return files.toArray(new File[1]);
        }
    }

    public static File[] findFiles(String path, String filename) {
        File dir = new File(path);
        if (!dir.exists()) {
            return null;
        }
        File[] matches = findFiles(dir, filename);
        return matches;
    }

    public static File findFile(String path, String filename) {
        File[] files = findFiles(path, filename);
        if (files.length == 0) {
            return null;
        } else {
            return files[0];
        }
    }

    public static File[] findFiles(String filename) {
        String path = LocationCommand.userDir();
        return findFiles(path, filename);
    }

    public static File findFile(String filename) {
        File[] files = findFiles(filename);
        if (files.length == 0) {
            return null;
        } else {
            return files[0];
        }
    }
}
