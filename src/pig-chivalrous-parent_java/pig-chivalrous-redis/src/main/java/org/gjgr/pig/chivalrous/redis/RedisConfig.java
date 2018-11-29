package org.gjgr.pig.chivalrous.redis;

import java.util.stream.Stream;

/**
 * Created by zhangchuang on 18/7/19.
 */
public class RedisConfig {
    // 通过Cluseter访问redis
    private String cluster;
    // 通过ip访问redis
    private String host;
    // 通过ip访问redis的端口号
    private Integer port;

    private String password;

    public RedisConfig append(String cluster) {
        if (this.cluster == null) {
            this.cluster = cluster;
        } else {
            this.cluster = this.cluster + "," + cluster;
        }
        return this;
    }

    public RedisConfig append(String... clusters) {
        String cluster = Stream.of(clusters).reduce((c1, c2) -> c1 == null ? c2 : c2 == null ? c1 : c1 + "," + c2).get();
        return append(cluster);
    }

    public RedisConfig append(String clusterIp, String clusterPort) {
        if (this.cluster == null) {
            this.cluster = clusterIp + ":" + clusterPort;
        } else {
            this.cluster = this.cluster + "," + clusterIp + ":" + clusterPort;
        }
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setPort(String port) {
        this.port = Integer.parseInt(port);
    }
}
