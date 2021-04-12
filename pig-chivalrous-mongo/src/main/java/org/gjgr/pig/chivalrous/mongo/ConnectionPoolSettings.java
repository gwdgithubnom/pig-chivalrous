package org.gjgr.pig.chivalrous.mongo;

import java.io.Serializable;
import org.gjgr.pig.chivalrous.core.json.GsonObject;

/**
 * @author gongwendong
 * @time 12-07-2020  星期一
 * @description: miparent:
 * @target:
 * @more:
 */
public class ConnectionPoolSettings implements Serializable {
    protected static final long serialVersionUID = 1024L;

    private final String FIELD_MAX_SIZE = "maxSize";
    private final String FIELD_MIN_SIZE = "minSize";
    private final String FIELD_THREADS_ALLOWED_BLOCK = "threadsAllowedToBlock";
    private final String FIELD_MAX_WAIT_TIME = "maxWaitTime";
    private final String FIELD_MAX_CONN_LIFE_TIME = "maxConnLifeTime";
    private final String FIELD_MAX_CONN_IDLE_TIME = "maxConnIdleTime";

    private final int DEFAULT_MAX_SIZE = 100;
    private final int DEFAULT_MIN_SIZE = 50;
    private final int DEFAULT_THREADS_ALLOWED_BLOCK = 5;
    private final int DEFAULT_MAX_WAIT_TIME = 120 * 1000;
    private final int DEFAULT_MAX_CONNECTION_LIFT_TIME = 30 * 60 * 1000;
    private final int DEFAULT_MAX_CONNECTION_IDLE_TIME = 5 * 60 * 1000;

    private int maxSize;
    private int minSize;
    private int threadsAllowedToBlock;
    private int maxWaitTimeMS;
    private int maxConnectionLifeTimeMS;
    private int maxConnectionIdleTimeMS;
    //private long maintenanceInitialDelayMS;
    //private long maintenanceFrequencyMS;

    public ConnectionPoolSettings() {
        this.maxSize = DEFAULT_MAX_SIZE;
        this.minSize = DEFAULT_MIN_SIZE;
        this.threadsAllowedToBlock = DEFAULT_THREADS_ALLOWED_BLOCK;
        this.maxWaitTimeMS = DEFAULT_MAX_WAIT_TIME;
        this.maxConnectionLifeTimeMS = DEFAULT_MAX_CONNECTION_LIFT_TIME;
        this.maxConnectionIdleTimeMS = DEFAULT_MAX_CONNECTION_IDLE_TIME;
    }

    public void loadConfig(GsonObject settingNode) {
        if (null == settingNode || settingNode.isJsonNull()) {
            return;
        }
        this.maxSize = settingNode.getAsNumber(FIELD_MAX_SIZE, DEFAULT_MAX_SIZE).intValue();
        this.minSize = settingNode.getAsNumber(FIELD_MIN_SIZE, DEFAULT_MIN_SIZE).intValue();
        this.threadsAllowedToBlock = settingNode.getAsNumber(FIELD_THREADS_ALLOWED_BLOCK, DEFAULT_THREADS_ALLOWED_BLOCK).intValue();
        this.maxWaitTimeMS = settingNode.getAsNumber(FIELD_MAX_WAIT_TIME, DEFAULT_MAX_WAIT_TIME).intValue();
        this.maxConnectionIdleTimeMS = settingNode.getAsNumber(FIELD_MAX_CONN_IDLE_TIME, DEFAULT_MAX_CONNECTION_IDLE_TIME).intValue();
        this.maxConnectionLifeTimeMS = settingNode.getAsNumber(FIELD_MAX_CONN_LIFE_TIME, DEFAULT_MAX_CONNECTION_LIFT_TIME).intValue();
       /* JsonNode maxSizeNode = settingNode.get(FIELD_MAX_SIZE);
        JsonNode minSizeNode = settingNode.get(FIELD_MIN_SIZE);
        JsonNode threadsAllowedToBlockNode = settingNode.get(FIELD_THREADS_ALLOWED_BLOCK);
        JsonNode maxWaitTimeNode = settingNode.get(FIELD_MAX_WAIT_TIME);
        JsonNode maxConnLifeTimeNode = settingNode.get(FIELD_MAX_CONN_LIFE_TIME);
        JsonNode maxConnIdleTimeNode = settingNode.get(FIELD_MAX_CONN_IDLE_TIME);*/

        /*this.maxSize = null != maxSizeNode ? maxSizeNode.getIntValue() : DEFAULT_MAX_SIZE;
        this.minSize = null != minSizeNode ? minSizeNode.getIntValue() : 50;
        this.threadsAllowedToBlock = null != threadsAllowedToBlockNode ? threadsAllowedToBlockNode.getIntValue() : 5;
        this.maxWaitTimeMS = null != maxWaitTimeNode ? maxWaitTimeNode.getLongValue() : 120*1000;
        this.maxConnectionLifeTimeMS = null != maxConnLifeTimeNode ? maxConnLifeTimeNode.getLongValue() : 30*60*1000;
        this.maxConnectionIdleTimeMS = null != maxConnIdleTimeNode ? maxConnIdleTimeNode.getLongValue() : 5*60*1000;*/
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getMinSize() {
        return minSize;
    }

    public int getThreadsAllowedToBlock() {
        return threadsAllowedToBlock;
    }

    public int getMaxWaitTimeMS() {
        return maxWaitTimeMS;
    }

    public int getMaxConnectionLifeTimeMS() {
        return maxConnectionLifeTimeMS;
    }

    public int getMaxConnectionIdleTimeMS() {
        return maxConnectionIdleTimeMS;
    }

    @Override
    public String toString() {
        return "ConnectionPoolSettings{" +
            "maxSize=" + maxSize +
            ", minSize=" + minSize +
            ", threadsAllowedToBlock=" + threadsAllowedToBlock +
            ", maxWaitTimeMS=" + maxWaitTimeMS +
            ", maxConnectionLifeTimeMS=" + maxConnectionLifeTimeMS +
            ", maxConnectionIdleTimeMS=" + maxConnectionIdleTimeMS +
            '}';
    }
}
