package org.gjgr.pig.chivalrous.redis;

import redis.clients.jedis.JedisPoolConfig;

/**
 * redisPoolProxy配置文件
 *
 * @author zhangchuang
 */
public class RedisPoolProxyConfig extends JedisPoolConfig {
    /**
     * ksarch redisClient 提供的cluster名称
     */
    private String proxyCluster;

    /**
     * 获取ip列表失败后重试次数
     */
    private int clusterRetryTimes;

    /**
     * 获取ip列表失败后重试时间间隔 单位：毫秒
     */
    private long clusterTimeout;

    /**
     * redisClient 直连ip地址
     */
    private String proxyHost;

    /**
     * redisClient 直连port端口
     */
    private Integer proxyPort;

    /**
     * redis连接池创建超时时间
     */
    private int poolTimeOut;

    /**
     * redis连接密码
     */
    private String password;

    /**
     * redis连接池更新间隔 单位:毫秒
     */
    private long redisPoolRefreshInterval;

    /**
     * 0： 使用默认cluster端口 1： 使用cluster的 proxy_port
     */
    private int proxyClusterType = 0;

    /**
     * proxy连接池数量,默认为5个
     */
    private int proxyPoolCount = 5;

    /**
     * 指定优先选择的机房
     */
    private String hostRoom;

    /**
     * 测试key
     */
    private String testKey = "fsg:fb:redisClient:bns:test";

    /**
     * 判断为不可用的重试次数
     */
    private int failCount = 3;

    public int getProxyPoolCount() {
        return proxyPoolCount;
    }

    public void setProxyPoolCount(int proxyPoolCount) {
        this.proxyPoolCount = proxyPoolCount;
    }

    public String getHostRoom() {
        return hostRoom;
    }

    public void setHostRoom(String hostRoom) {
        this.hostRoom = hostRoom;
    }

    public String getTestKey() {
        return testKey;
    }

    public void setTestKey(String testKey) {
        this.testKey = testKey;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoolTimeOut() {
        return poolTimeOut;
    }

    public void setPoolTimeOut(int poolTimeOut) {
        this.poolTimeOut = poolTimeOut;
    }

    public String getProxyCluster() {
        return proxyCluster;
    }

    public void setProxyCluster(String proxyCluster) {
        this.proxyCluster = proxyCluster;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public long getRedisPoolRefreshInterval() {
        return redisPoolRefreshInterval;
    }

    public void setRedisPoolRefreshInterval(long redisPoolRefreshInterval) {
        this.redisPoolRefreshInterval = redisPoolRefreshInterval;
    }

    public int getClusterRetryTimes() {
        return clusterRetryTimes;
    }

    public void setClusterRetryTimes(int clusterRetryTimes) {
        this.clusterRetryTimes = clusterRetryTimes;
    }

    public long getClusterTimeout() {
        return clusterTimeout;
    }

    public void setClusterTimeout(long clusterTimeout) {
        this.clusterTimeout = clusterTimeout;
    }

    public int getProxyClusterType() {
        return proxyClusterType;
    }

    public void setProxyClusterType(int proxyClusterType) {
        this.proxyClusterType = proxyClusterType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProducerConfig [");
        sb.append("proxyCluster=").append(proxyCluster);
        sb.append(", clusterRetryTimes=").append(clusterRetryTimes);
        sb.append(", clusterTimeout=").append(clusterTimeout);
        sb.append(", proxyHost=").append(proxyHost);
        sb.append(", proxyPort=").append(proxyPort);
        sb.append(", redisPoolRefreshInterval=").append(redisPoolRefreshInterval);
        sb.append(", proxyClusterType=").append(proxyClusterType);
        sb.append(", proxyPoolCount=").append(proxyPoolCount);
        sb.append("]");
        return sb.toString();
    }

}