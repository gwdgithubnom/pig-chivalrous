package org.gjgr.pig.chivalrous.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.event.ServerListener;
import java.util.ArrayList;
import java.util.List;
import org.bson.codecs.configuration.CodecRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gongwendong
 * @time 12-07-2020  星期一
 * @description: miparent:
 * @target:
 * @more: mongodb://localhost:27017
 */
public class MongoOperation {
    private static Logger logger = LoggerFactory.getLogger(MongoOperation.class);
    MongoConfiguration mongoConfiguration;
    MongoClient mongoClient;

    public MongoOperation(MongoConfiguration mongoConfiguration) {
        this.mongoConfiguration = mongoConfiguration;
        // mongoConfiguration.getProperties()
        loadConfig();
    }

    public void loadConfig() {
        MongoClientOptions.Builder builder = mongoConfiguration.getMongoClientOptionsBuilder();
        builder.requiredReplicaSetName(mongoConfiguration.getOrDefault("requiredReplicaSetName", ""))
            // description
            .applicationName(mongoConfiguration.getOrDefault("replicaSetName", ""))
            // .description(description)
            // connection
            .minConnectionsPerHost(mongoConfiguration.get(ConnectionPoolSettings.class).getMinSize())//The minimum number of connections. Those connections will be kept in the mongo when idle, and the mongo will ensure that it contains at least this minimum number.
            .connectionsPerHost(mongoConfiguration.get(ConnectionPoolSettings.class).getMaxSize())
            .threadsAllowedToBlockForConnectionMultiplier(
                mongoConfiguration.get(ConnectionPoolSettings.class).getThreadsAllowedToBlock())
            .maxWaitTime(mongoConfiguration.get(ConnectionPoolSettings.class)
                .getMaxWaitTimeMS()) // The maximum time that a thread may wait for a connection to become available.
            .maxConnectionIdleTime(mongoConfiguration.get(ConnectionPoolSettings.class).getMaxConnectionIdleTimeMS())
            .maxConnectionLifeTime(mongoConfiguration.get(ConnectionPoolSettings.class).getMaxConnectionLifeTimeMS())
            // heartbeat
            .heartbeatConnectTimeout(mongoConfiguration.get(HeartbeatSettings.class).getHeartbeatConnectTimeoutMS())
            .heartbeatSocketTimeout(mongoConfiguration.get(HeartbeatSettings.class).getHeartbeatReadTimeoutMS())
            .minHeartbeatFrequency(mongoConfiguration.get(HeartbeatSettings.class).getHeartbeatMinFrequency())
            .heartbeatFrequency(mongoConfiguration.get(HeartbeatSettings.class).getHeartbeatMaxFrequency())
            // socket
            .connectTimeout(mongoConfiguration.get(SocketSettings.class).getConnectTimeoutMS())
            .socketTimeout(mongoConfiguration.get(SocketSettings.class).getReadTimeoutMS())
            .socketKeepAlive(mongoConfiguration.get(SocketSettings.class).isKeepAlive())
            .serverSelectionTimeout(mongoConfiguration.get(SocketSettings.class).getServerSelectionTimeoutMS())
            // other
            .localThreshold(15)
            .readPreference(mongoConfiguration.get(ReadPreference.class))
            // listener
            .addServerListener(mongoConfiguration.get(ServerListener.class));
        //write concern
        WriteConcern writeConcernCfg = mongoConfiguration.get(WriteConcern.class);
        if (writeConcernCfg != null) {
            builder.writeConcern(writeConcernCfg);
        }
        MongoClientOptions options = builder.build();
        if (logger.isDebugEnabled()) {
            logger.debug("original MongoClient info：{}", mongoClient);
        }
        logger.info("current configuration：" + options.toString());
        List<ServerAddress> nodes = (List<ServerAddress>) mongoConfiguration.getList(ServerAddress.class);
        if (mongoConfiguration.get(CredentialSettings.class).isAuth()) {
            List<MongoCredential> credentialList = new ArrayList<MongoCredential>() {{
                add(MongoCredential.createScramSha1Credential(mongoConfiguration.get(CredentialSettings.class).getUsername(),
                    mongoConfiguration.get(CredentialSettings.class).getAuthDatabase(),
                    mongoConfiguration.get(CredentialSettings.class).getPassword().toCharArray()));
            }};
            mongoClient = new MongoClient(nodes, credentialList, options);
            logger.info("Connect Mongo with auth");
        } else {
            mongoClient = new MongoClient(nodes, options);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("update MongoClient info： {}", mongoClient);
        }
    }

}
