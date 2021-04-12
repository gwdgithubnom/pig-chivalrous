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
public class HeartbeatSettings implements Serializable {
    protected static final long serialVersionUID = 1024L;

    private final String FIELD_HB_CONN_TIMEOUT = "hbConnectTimeout";
    private final String FIELD_HB_READ_TIMEOUT = "hbReadTimeout";
    private final String FIELD_HB_MAX_FREQUENCY = "hbMaxFrequency";
    private final String FIELD_HB_MIN_FREQUENCY = "hbMinFrequency";

    private final int DEFAULT_HB_CONNECT_TIMEOUT = 20 * 1000;
    private final int DEFAULT_HB_READ_TIMEOUT = 20 * 1000;
    private final int DEFAULT_HB_MAX_FREQUENCY = 10 * 1000;
    private final int DEFAULT_HB_MIN_FREQUENCY = 500;

    private int heartbeatConnectTimeoutMS;
    private int heartbeatReadTimeoutMS;
    private int heartbeatMaxFrequency;
    private int heartbeatMinFrequency;

    public HeartbeatSettings() {
        this.heartbeatConnectTimeoutMS = DEFAULT_HB_CONNECT_TIMEOUT;
        this.heartbeatReadTimeoutMS = DEFAULT_HB_READ_TIMEOUT;
        this.heartbeatMaxFrequency = DEFAULT_HB_MAX_FREQUENCY;
        this.heartbeatMinFrequency = DEFAULT_HB_MIN_FREQUENCY;
    }

    public void loadConfig(GsonObject settingNode) {
        if (null == settingNode || settingNode.isJsonNull()) {
            return;
        }
        this.heartbeatConnectTimeoutMS = settingNode.getAsInt(FIELD_HB_CONN_TIMEOUT, DEFAULT_HB_CONNECT_TIMEOUT);
        this.heartbeatReadTimeoutMS = settingNode.getAsInt(FIELD_HB_READ_TIMEOUT, DEFAULT_HB_READ_TIMEOUT);
        this.heartbeatMaxFrequency = settingNode.getAsInt(FIELD_HB_MAX_FREQUENCY, DEFAULT_HB_MAX_FREQUENCY);
        this.heartbeatMinFrequency = settingNode.getAsInt(FIELD_HB_MIN_FREQUENCY, DEFAULT_HB_MIN_FREQUENCY);
    }

    public int getHeartbeatConnectTimeoutMS() {
        return heartbeatConnectTimeoutMS;
    }

    public int getHeartbeatReadTimeoutMS() {
        return heartbeatReadTimeoutMS;
    }

    public int getHeartbeatMaxFrequency() {
        return heartbeatMaxFrequency;
    }

    public int getHeartbeatMinFrequency() {
        return heartbeatMinFrequency;
    }

    @Override
    public String toString() {
        return "HeartbeatSettings{" +
            "heartbeatConnectTimeoutMS=" + heartbeatConnectTimeoutMS +
            ", heartbeatReadTimeoutMS=" + heartbeatReadTimeoutMS +
            ", heartbeatMaxFrequency=" + heartbeatMaxFrequency +
            ", heartbeatMinFrequency=" + heartbeatMinFrequency +
            '}';
    }
}
