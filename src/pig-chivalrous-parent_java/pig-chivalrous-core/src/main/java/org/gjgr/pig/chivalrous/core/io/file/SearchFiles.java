package org.gjgr.pig.chivalrous.core.io.file;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

/**
 * @Author gwd
 * @Time 01-24-2019 Thursday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public class SearchFiles {

    private Map<String, Boolean> map = new HashMap<String, Boolean>();

    private File root;

    public SearchFiles(File root) {
        this.root = root;
    }

    /**
     * List eligible files on current path
     * 
     * @param directory The directory to be searched
     * @return Eligible files
     */
    private String[] getTargetFiles(File directory) {
        if (directory == null) {
            return null;
        }

        String[] files = directory.list(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                // TODO Auto-generated method stub
                return name.startsWith("Temp") && name.endsWith(".txt");
            }

        });

        return files;
    }

    /**
     * Print all eligible files
     */
    private void printFiles(String[] targets) {
        for (String target : targets) {
            System.out.println(target);
        }
    }

    /**
     * How many files in the parent directory and its subdirectory <br>
     * depends on how many files in each subdirectory and their subdirectory
     */
    private void recursive(File path) {

        printFiles(getTargetFiles(path));
        for (File file : path.listFiles()) {
            if (file.isDirectory()) {
                recursive(file);
            }
        }
        if (path.isDirectory()) {
            printFiles(getTargetFiles(path));
        }
    }

    private void dfs() {
        if (root == null) {
            return;
        }

        Stack<File> stack = new Stack<File>();
        stack.push(root);
        map.put(root.getAbsolutePath(), true);
        while (!stack.isEmpty()) {
            File node = stack.peek();
            File child = getUnvisitedChild(node);

            if (child != null) {
                stack.push(child);
                printFiles(getTargetFiles(child));
                map.put(child.getAbsolutePath(), true);
            } else {
                stack.pop();
            }

        }
    }

    /**
     * Get unvisited node of the node
     *
     */
    private File getUnvisitedChild(File node) {

        File[] childs = node.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                // TODO Auto-generated method stub
                return pathname.isDirectory();

            }

        });

        if (childs == null) {
            return null;
        }

        for (File child : childs) {
            if (map.containsKey(child.getAbsolutePath()) == false) {
                map.put(child.getAbsolutePath(), false);
            }
            if (map.get(child.getAbsolutePath()) == false) {
                return child;
            }
        }

        return null;
    }

    /**
     * Search the node's neighbors firstly before moving to the next level neighbors
     */
    private void bfs() {
        if (root == null) {
            return;
        }

        Queue<File> queue = new LinkedList<File>();
        queue.add(root);

        while (!queue.isEmpty()) {
            File node = queue.remove();
            printFiles(getTargetFiles(node));
            File[] childs = node.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    // TODO Auto-generated method stub
                    return pathname.isDirectory();

                }

            });

            if (childs != null) {
                for (File child : childs) {
                    queue.add(child);
                }
            }
        }
    }

}
