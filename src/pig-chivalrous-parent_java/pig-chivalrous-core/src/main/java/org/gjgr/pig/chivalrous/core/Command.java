package org.gjgr.pig.chivalrous.core;

import java.util.Map;

/**
 * @Author gwd
 * @Time 07-31-2018  Tuesday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public interface Command {
    public Object execute(String... params);

    public Object execute(Map.Entry... map);
}
