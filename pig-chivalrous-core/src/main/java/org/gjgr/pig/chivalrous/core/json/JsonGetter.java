package org.gjgr.pig.chivalrous.core.json;

import org.gjgr.pig.chivalrous.core.getter.OptNullBasicTypeFromObjectGetter;
import org.gjgr.pig.chivalrous.core.json.bean.ListJson;
import org.gjgr.pig.chivalrous.core.json.bean.MapJson;

/**
 * 用于JSON的Getter类，提供各种类型的Getter方法
 *
 * @param <K>
 * @author Looly
 */
public abstract class JsonGetter<K> extends OptNullBasicTypeFromObjectGetter<K> {

    /**
     * 获得JSONArray对象
     *
     * @param key KEY
     * @return JSONArray对象，如果值为null或者非JSONArray类型，返回null
     */
    public ListJson getJSONArray(K key) {
        Object o = this.getObj(key);
        return o instanceof ListJson ? (ListJson) o : null;
    }

    /**
     * 获得JSONObject对象
     *
     * @param key KEY
     * @return JSONArray对象，如果值为null或者非JSONObject类型，返回null
     */
    public MapJson getJSONObject(K key) {
        Object object = this.getObj(key);
        return object instanceof MapJson ? (MapJson) object : null;
    }
}
