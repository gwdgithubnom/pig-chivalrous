package org.gjgr.pig.chivalrous.redis;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * this is a config file about redis status value is low zero would take as @see JedisPool and big then zero would taken
 * as @see JedisCluster
 *
 * @author gwd
 */
public final class RedisConfig {

    private Set<HostAndPort> base = new HashSet<>();
    private int soTimeout = 3000;
    private int timeout = 2000;
    private int maxAttempts = 5;
    private int connectionTimeout = 10000;
    private String password;

    /**
     * default is JedisPool
     */
    private int status = 1;

    private GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();

    public int status() {
        return status;
    }

    public int setNoCluster() {
        status = 0;
        genericObjectPoolConfig.setTestWhileIdle(true);
        genericObjectPoolConfig.setMinEvictableIdleTimeMillis(60000);
        genericObjectPoolConfig.setTimeBetweenEvictionRunsMillis(30000);
        genericObjectPoolConfig.setNumTestsPerEvictionRun(-1);
        genericObjectPoolConfig.setTestWhileIdle(true);
        return status;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public RedisConfig getCluster() {
        setCluster();
        return this;
    }

    public RedisConfig getNoCluster() {
        setNoCluster();
        return this;
    }

    public int setCluster() {
        status = 1;
        return status;
    }

    public boolean add(HostAndPort hostAndPort) {
        status = base.size();
        return base.add(hostAndPort);
    }

    public boolean remove(HostAndPort hostAndPort) {
        return base.remove(hostAndPort);
    }

    public Set<HostAndPort> getBase() {
        return base;
    }

    public void setBase(Set<HostAndPort> base) {
        this.base = base;
    }

    public GenericObjectPoolConfig getGenericObjectPoolConfig() {
        return genericObjectPoolConfig;
    }

    public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {
        this.genericObjectPoolConfig = genericObjectPoolConfig;
    }

    public boolean add(String url, String port) {
        int p = 6379;
        try {
            if (port != null) {
                p = Integer.parseInt(port);
            }
        } catch (Exception e) {

        }
        return add(p, url);
    }

    public boolean put(String url, String port) {
        int p = 6379;
        try {
            if (port != null) {
                p = Integer.parseInt(port);
            }
        } catch (Exception e) {

        }
        return put(url, port);
    }

    public boolean add(int port, String url) {
        HostAndPort hostAndPort = new HostAndPort(url, port);
        setCluster();
        return add(hostAndPort);
    }

    public boolean add(String[] urls, int port) {
        int[] ports = new int[1];
        ports[0] = port;
        return add(urls, ports);
    }

    public boolean add(String[] urls, String[] ports) {
        if (urls.length == ports.length) {
            for (int i = 0; i < urls.length; i++) {
                add(urls[i], ports[i]);
            }
        } else if (ports.length == 1) {
            for (int i = 0; i < urls.length; i++) {
                add(urls[i], ports[0]);
            }
        } else {
            throw new RuntimeException(String.format("urls and port order is not consist urls size:%d, port size:%d",
                    urls.length, ports.length));
        }
        return true;
    }

    public boolean add(String[] urls, int[] port) {
        if (urls.length == port.length) {
            for (int i = 0; i < urls.length; i++) {
                add(port[i], urls[i]);
            }
        } else if (port.length == 1) {
            for (int i = 0; i < urls.length; i++) {
                add(port[0], urls[i]);
            }
        } else {
            throw new RuntimeException(String.format("urls and port order is not consist urls size:%d, port size:%d",
                    urls.length, port.length));
        }
        return true;
    }

    public boolean put(String url, int port) {
        HostAndPort hostAndPort = new HostAndPort(url, port);
        setNoCluster();
        return add(hostAndPort);
    }

    public RedisClient build() {
        return new RedisClientBuilder().redisClient();
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    @Override
    public int hashCode() {
        StringBuffer stringBuffer = new StringBuffer();
        for (HostAndPort hostAndPort : base) {
            stringBuffer.append(hostAndPort.getHost());
            stringBuffer.append(hostAndPort.getPort());
        }
        return Objects.hash(stringBuffer.toString(), password, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RedisConfig that = (RedisConfig) o;
        return that.hashCode() == this.hashCode();
    }

    public String hasString() {
        return hashCode() + "";
    }

    protected RedisConfig me() {
        return this;
    }

    private class RedisClientBuilder {

        public RedisClient redisClient() {
            String id = hasString();
            RedisClient redisClient = new RedisClient(id);
            if (status > 0) {
                JedisCluster jedisCluster = null;
                if (password == null) {
                    jedisCluster =
                            new JedisCluster(base, connectionTimeout, soTimeout, maxAttempts, genericObjectPoolConfig);
                } else {
                    jedisCluster = new JedisCluster(base, connectionTimeout, soTimeout, maxAttempts, password,
                            genericObjectPoolConfig);
                }
                redisClient.put(me(), jedisCluster);
            } else {
                JedisPool jedisPool = null;
                HostAndPort hostAndPort = base.iterator().next();
                if (password == null) {
                    jedisPool = new JedisPool(genericObjectPoolConfig, hostAndPort.getHost(), hostAndPort.getPort(),
                            timeout, password);
                } else {
                    jedisPool = new JedisPool(genericObjectPoolConfig, hostAndPort.getHost(), hostAndPort.getPort(),
                            timeout);
                }
                redisClient.put(me(), jedisPool);
            }
            return redisClient;
        }
    }
}