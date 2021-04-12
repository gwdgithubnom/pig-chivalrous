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
public class SocketSettings implements Serializable {
    protected static final long serialVersionUID = 1024L;

    private final String FIELD_CONN_TIMEOUT = "connectTimeout";
    private final String FIELD_READ_TIMEOUT = "readTimeout";
    private final String FIELD_KEEP_ALIVE = "keepAlive";
    private final String SERVER_SELECTION_TIMEOUT = "serverSelectionTimeout";

    private final int DEFAULT_CONNECT_TIMEOUT = 10 * 1000;
    private final int DEFAULT_READ_TIMEOUT = 10 * 1000;
    private final int DEFAULT_SERVER_SELECTION_TIMEOUT = 500;

    private int connectTimeoutMS;
    private int readTimeoutMS;
    private boolean keepAlive;
    private int serverSelectionTimeoutMS;
    //private int receiveBufferSize;
    //private int sendBufferSize;

    public SocketSettings() {
        this.connectTimeoutMS = DEFAULT_CONNECT_TIMEOUT;
        this.readTimeoutMS = DEFAULT_READ_TIMEOUT;
        this.keepAlive = false;
        this.serverSelectionTimeoutMS = DEFAULT_SERVER_SELECTION_TIMEOUT;
    }

    public void init(GsonObject settingNode) {
        if (null == settingNode || settingNode.isJsonNull()) {
            return;
        }
        this.connectTimeoutMS = settingNode.getAsInt(FIELD_CONN_TIMEOUT, DEFAULT_CONNECT_TIMEOUT);
        this.readTimeoutMS = settingNode.getAsInt(FIELD_READ_TIMEOUT, DEFAULT_READ_TIMEOUT);
        this.keepAlive = settingNode.getAsBoolean(FIELD_KEEP_ALIVE, false);
        this.serverSelectionTimeoutMS = settingNode.getAsInt(SERVER_SELECTION_TIMEOUT, DEFAULT_SERVER_SELECTION_TIMEOUT);
    }

    public int getConnectTimeoutMS() {
        return connectTimeoutMS;
    }

    public int getReadTimeoutMS() {
        return readTimeoutMS;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public int getServerSelectionTimeoutMS() {
        return serverSelectionTimeoutMS;
    }

}
