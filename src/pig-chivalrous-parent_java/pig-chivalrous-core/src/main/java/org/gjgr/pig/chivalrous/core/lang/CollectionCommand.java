package org.gjgr.pig.chivalrous.core.lang;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author gwd
 * @Time 10-29-2018  Monday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public class CollectionCommand {
    public static String convertToString(Collection<?> collection) {
        Validate.noNullElements(collection, "colletion is null, can not convert to a String");
        return StringUtils.join(collection, StringConstants.MILIAO_STANDARD_SEPERATOR);
    }

    public static List<String> convertToList(String listString) {
        Validate.notNull(listString, "String is null can not convert to a collection");
        return Arrays.asList(listString.split(StringConstants.MILIAO_STANDARD_SEPERATOR));
    }

    public static <K, V, T> void collectionToMap(Collection<T> collection, Map<K, V> map, MapAdder<K, V, T> adder) {
        if (collection != null && map != null && adder != null) {
            for (T element : collection) {
                adder.put(map, element);
            }
        }
    }

    /**
     * Build map from array like: <pre><code>
     * String[][] array = {
     *      { "name", "Mike" },
     *      { "age", "13" },
     * }
     * </code></pre>
     *
     * @param mapArray
     * @return
     */
    public Map<String, String> buildMap(String[][] mapArray) {
        Validate.notNull(mapArray, "mapArray");
        Map<String, String> map = new HashMap<String, String>();
        for (String[] array : mapArray) {
            Validate.isTrue(array != null && array.length == 2, "Each row should contain and only contain 2 elements.");
            map.put(array[0], array[1]);
        }
        return map;
    }

    static interface StringConstants {
        String MILIAO_STANDARD_SEPERATOR = ",";
    }

    public static interface MapAdder<K, V, T> {
        void put(Map<K, V> map, T element);
    }
}
