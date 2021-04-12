package org.gjgr.pig.chivalrous.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.MapConfiguration;
import org.apache.commons.lang.StringUtils;
import org.bson.codecs.configuration.CodecRegistry;
import org.gjgr.pig.chivalrous.core.json.GsonObject;
import org.gjgr.pig.chivalrous.core.json.JsonCommand;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.gjgr.pig.chivalrous.core.net.UriBuilder;
import org.gjgr.pig.chivalrous.core.net.UriCommand;

/**
 * @author gongwendong
 * @time 12-07-2020  星期一
 * @description: miparent:
 * @target:
 * @more:
 */
public class MongoConfiguration extends MapConfiguration implements Serializable {
    protected static final long serialVersionUID = 1024L;

    public MongoConfiguration(Map<String, ?> map) {
        super(map);
        initConfig();
    }

    public MongoConfiguration(Properties props) {
        super(props);
        initConfig();
    }

    /**
     * need to finished the request url
     * to convert to the UriBuilder
     *
     * @param url
     * @param nodeUrl
     */
    public MongoConfiguration(String url, String nodeUrl) {
        // UriBuilder uriBuilder = UriCommand.uriBuilder(url);
        // Map<String,String> query = UriCommand.param(nodeUrl);
        // super(parseUriConfig(url,nodeUrl));
        super(new HashMap<>());
        getMap().putAll(parseUriConfig(url, nodeUrl));
        //do parse operation
        initConfig();
    }

    private String getReplicaSetName() {
        return get(String.class, "replicaSetName", "");
    }

    public MongoClientOptions.Builder getMongoClientOptionsBuilder() {
        return get(MongoClientOptions.Builder.class, MongoClientOptions.Builder.class.getName());
    }

    private void initConfig() {
        map.put(CredentialSettings.class.getName(), new CredentialSettings());
        map.put(ConnectionPoolSettings.class.getName(), new ConnectionPoolSettings());
        map.put(HeartbeatSettings.class.getName(), new HeartbeatSettings());
        map.put(SocketSettings.class.getName(), new SocketSettings());
        map.put(CodecRegistry.class.getName(), MongoClient.getDefaultCodecRegistry());
        map.put(ServerAddress.class.getName(), new ArrayList<ServerAddress>());
        map.put(MongoClientOptions.Builder.class.getName(), MongoClientOptions.builder());
    }

    public String getOrDefault(String key, String defaultValue) {
        return get(String.class, key, defaultValue);
    }

    public <T> T get(Class<T> t) {
        String key = t.getName();
        if (containsKey(key)) {
            return get(t, key);
        } else {
            try {
                return t.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private synchronized List<ServerAddress> getServerAddress() {
        List<ServerAddress> serverAddresses = get(new ArrayList<>().getClass(), ServerAddress.class.getName());
        return serverAddresses;
    }

    public synchronized List<?> getList(Class<?> t) {
        List<?> ts = get(new ArrayList<>().getClass(), t.getName());
        return ts;
    }

    /**
     * node url would split with , and ;
     *
     * @param url
     * @param nodeUrl
     * @return
     */
    private HashMap<String, ?> parseUriConfig(String url, String nodeUrl) {
        HashMap hashMap;
        if (nodeUrl == null) {
            hashMap = parseUriConfig(UriCommand.uriBuilder(url), new ArrayList<>());
        } else {
            hashMap = parseUriConfig(url, Arrays.asList(nodeUrl.split(",|;")));
        }
        return hashMap;
    }

    private HashMap<String, ?> parseUriConfig(String url, List<String> nodeUrls) {
        return parseUriConfig(UriCommand.uriBuilder(url), nodeUrls.parallelStream().map(u -> UriCommand.uriBuilder(u)).collect(Collectors.toList()));
    }

    private HashMap<String, ?> parseUriConfig(UriBuilder uriBuilder, List<UriBuilder> uriBuilderList) {
        HashMap hashMap = new HashMap<>();
        List<ServerAddress> serverAddresses = getServerAddress();
        serverAddresses.add(buildServerAddress(uriBuilder.getHost(), uriBuilder.getPort()));
        uriBuilderList.forEach((u) -> {
            serverAddresses.add(buildServerAddress(u.getHost(), u.getPort()));
        });
        return hashMap;
    }

    private ReadPreference parseReadPrefer(String readPrefer) {
        if (StringCommand.isBlank(readPrefer) || "SECONDARY_PREFERRED".equals(readPrefer)) {
            return ReadPreference.secondaryPreferred();
        }
        if ("PRIMARY".equals(readPrefer)) {
            return ReadPreference.primary();
        }
        if ("PRIMARY_PREFERRED".equals(readPrefer)) {
            return ReadPreference.primaryPreferred();
        }
        if ("SECONDARY".equals(readPrefer)) {
            return ReadPreference.secondary();
        }
        if ("NEAREST".equals(readPrefer)) {
            return ReadPreference.nearest();
        }
        return ReadPreference.secondaryPreferred();
    }

    /**
     * 读取writeConcern配置
     * writeConcern: https://docs.mongodb.com/v3.6/reference/write-concern/
     *
     * @param writeConcern json string eg: {"w":"1","j":true,"timeout":1000}
     */
    private WriteConcern parseWriteConcern(String writeConcern) {
        if (StringUtils.isBlank(writeConcern)) {
            return null;
        }
        try {
            GsonObject config = JsonCommand.gsonObject(writeConcern);
            if (!config.isJsonNull()) {
                String w = config.getAsString("w");
                String j = config.getAsString("j");
                Long timeout = config.getAsLong("timeout", -1L);
                WriteConcern concern = new WriteConcern(w);
                if ("true".equals(j)) {
                    concern = concern.withJournal(true);
                } else if ("false".equals(j)) {
                    concern = concern.withJournal(false);
                }
                if (!timeout.equals(-1L)) {
                    concern = concern.withWTimeout(timeout, TimeUnit.MILLISECONDS);
                }
                return concern;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private ServerAddress buildServerAddress(String host, int port) {
        ServerAddress serverAddress = new ServerAddress(host, port);
        return serverAddress;
    }

}
