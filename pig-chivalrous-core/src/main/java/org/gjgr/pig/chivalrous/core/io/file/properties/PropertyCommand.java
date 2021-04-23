package org.gjgr.pig.chivalrous.core.io.file.properties;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.gjgr.pig.chivalrous.core.io.IoCommand;
import org.gjgr.pig.chivalrous.core.io.file.FileCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author gwd
 * @Time 10-29-2018 Monday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public class PropertyCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyCommand.class);

    public static Map<String, String> loadProperties(String... paths) {
        Map<String, String> propertiesHolder = new HashMap<>();
        for (Map.Entry<Object, Object> entry : readProperties(paths).entrySet()) {
            propertiesHolder.put((String) entry.getKey(), (String) entry.getValue());
        }
        return propertiesHolder;
    }

    public static Properties readProperties(String... paths) {
        Properties p = new Properties();
        for (String path : paths) {
            try {
                p.load(IoCommand.inputStream(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return p;
    }

    /**
     * 比较两个properties文件是否不同
     *
     * @param first      第一个properties文件
     * @param firstName  第一个properties文件名
     * @param second     第二个properties文件
     * @param secondName 第二个properties文件名
     * @return 不同返回true，相同返回false
     */
    public boolean propertiesDiff(Properties first, String firstName, Properties second, String secondName) {
        Validate.notNull(first, "first");
        Validate.notNull(second, "second");
        boolean isDiff = false;
        Collection sub1 = CollectionUtils.subtract(first.entrySet(), second.entrySet());
        Collection sub2 = CollectionUtils.subtract(second.entrySet(), first.entrySet());
        if (!sub1.isEmpty()) {
            LOGGER.error("{}.properties is diff with {}.properties:\n{}", new Object[] {
                    firstName, secondName, StringUtils.join(sub1, "\n")
            });
            isDiff = true;
        }
        if (!sub2.isEmpty()) {
            LOGGER.error("{}.properties is diff with {}.properties:\n{}", new Object[] {
                    secondName, firstName, StringUtils.join(sub2, "\n")
            });
            isDiff = true;
        }
        return isDiff;
    }

    /**
     * Load Properties from resource specified by {@ pathInResource}, and put all of them to system properties.
     *
     * @param clazz          class to load resource.
     * @param pathInResource path of the property file in resource. see {@link Class#getResourceAsStream(String)}
     * @return true if load successfully. otherwise false.
     */
    public boolean loadPropertiesFromResource(Class<?> clazz, String pathInResource) {
        return getPropertiesFromStream(clazz.getResourceAsStream(pathInResource), true) != null;
    }

    /**
     * Get Properties from resource specified by {@ pathInResource}.
     *
     * @param clazz          class to load resource.
     * @param pathInResource path of the property file in resource. see {@link Class#getResourceAsStream(String)}
     * @return the properties instance or null if load failed.
     */
    public Properties getPropertiesFromResource(Class<?> clazz, String pathInResource) {
        return getPropertiesFromStream(clazz.getResourceAsStream(pathInResource), false);
    }

    public Properties getPropertiesFromFile(String filePath) {
        try {
            return getPropertiesFromStream(new FileInputStream(filePath), false);
        } catch (FileNotFoundException e) {
            LOGGER.error("Could not open file: " + filePath, e);
            return null;
        }
    }

    /**
     * Load Properties from file specified by {@ filePath}, and put all of them to system properties.
     *
     * @param filePath path of the property file.
     * @return true if load successfully. otherwise false.
     */
    public boolean isProperties(String filePath) {
        try {
            return getPropertiesFromStream(new FileInputStream(filePath), true) != null;
        } catch (FileNotFoundException e) {
            LOGGER.error("Could not open file: " + filePath, e);
            return false;
        }
    }

    /**
     * Load properties from stream, and return the {@link Properties} instance. <br>
     * The input stream will be closed by this method.
     *
     * @param stream
     * @param putToSystemProperties
     * @return the Properties instance, or null if failed.
     */
    private Properties getPropertiesFromStream(InputStream stream, boolean putToSystemProperties) {
        try {
            Properties properties = new Properties();
            properties.load(stream);

            if (putToSystemProperties) {
                System.getProperties().putAll(properties);
            }
            return properties;
        } catch (IOException e) {
            LOGGER.error("Could not open properties.", e);
            return null;
        } finally {
            IoCommand.closeIgnoreException(stream);
        }
    }

    public int getPropertyInt(Properties prop, String key, int defaultValue) {
        String value = prop.getProperty(key);
        return StringUtils.isEmpty(value) ? defaultValue : Integer.parseInt(value.trim());
    }

    public int getPropertyInt(String key, int defaultValue) {
        return getPropertyInt(System.getProperties(), key, defaultValue);
    }

    public long getPropertyLong(Properties prop, String key, long defaultValue) {
        String value = prop.getProperty(key);
        return StringUtils.isEmpty(value) ? defaultValue : Long.parseLong(value.trim());
    }

    public long getPropertyLong(String key, long defaultValue) {
        return getPropertyLong(System.getProperties(), key, defaultValue);
    }

    public boolean getPropertyBoolean(Properties prop, String key, boolean defaultValue) {
        String value = prop.getProperty(key);
        return StringUtils.isEmpty(value) ? defaultValue : Boolean.parseBoolean(value.trim());
    }

    public boolean getPropertyBoolean(String key, boolean defaultValue) {
        return getPropertyBoolean(System.getProperties(), key, defaultValue);
    }

    public String dumpProperties(Properties p, boolean sort) {
        return dumpProperties(p, "[[\n    ", "\n    ", "]]\n", sort);
    }

    public String dumpProperties(Properties p, String head, String separator, String tail, boolean sort) {
        Collection<Map.Entry<Object, Object>> entries = p.entrySet();

        if (sort) {
            ArrayList<Map.Entry<Object, Object>> list = new ArrayList<Map.Entry<Object, Object>>(p.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<Object, Object>>() {
                @Override
                public int compare(Map.Entry<Object, Object> o1, Map.Entry<Object, Object> o2) {
                    return o1.getKey().toString().compareTo(o2.getKey().toString());
                }
            });
            entries = list;
        }
        return head + StringUtils.join(entries, separator) + tail;
    }
}
