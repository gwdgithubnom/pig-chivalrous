package org.gjgr.pig.chivalrous.redis;

import org.gjgr.pig.chivalrous.log.SystemLogger;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    private int status = 0;

    private JedisPoolConfig genericObjectPoolConfig = new JedisPoolConfig();

    public int status() {
        return status;
    }

    public int setNoCluster() {
        status = -1;
        genericObjectPoolConfig.setTestWhileIdle(true);
        genericObjectPoolConfig.setMinEvictableIdleTimeMillis(60000);
        genericObjectPoolConfig.setTimeBetweenEvictionRunsMillis(30000);
        genericObjectPoolConfig.setNumTestsPerEvictionRun(-1);
        genericObjectPoolConfig.setTestWhileIdle(true);
        return status;
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

    public JedisPoolConfig getGenericObjectPoolConfig() {
        return genericObjectPoolConfig;
    }

    public void setGenericObjectPoolConfig(JedisPoolConfig genericObjectPoolConfig) {
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
        return add(url, p);
    }

    public boolean put(String url, String port) {
        int p = 6379;
        try {
            if (port != null) {
                p = Integer.parseInt(port);
            }
        } catch (Exception e) {

        }
        return put(url, p);
    }

    public boolean add(int port, String url) {
        HostAndPort hostAndPort = new HostAndPort(url, port);
        return add(hostAndPort);
    }

    public boolean add(String url, int port) {
        HostAndPort hostAndPort = new HostAndPort(url, port);
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
        setCluster();
        return add(url, port);
    }

    public boolean put(int port, String url) {
        setNoCluster();
        return add(port, url);
    }

    public synchronized RedisClient build() {
        if (RedisClient.get(hashString()) == null) {
            return new RedisClientBuilder().redisClient();
        } else {
            return new RedisClient(hashString());
        }
    }

    public RedisClient newBuild() {
        return new RedisClientBuilder().newRedisClient();
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
        List<HostAndPort> hostAndPortList = new ArrayList<HostAndPort>(base);
        hostAndPortList.sort(new Comparator<HostAndPort>() {
            @Override
            public int compare(HostAndPort o1, HostAndPort o2) {
                if (o1.getHost().equalsIgnoreCase(o2.getHost())) {
                    return o1.getPort() - o2.getPort();
                } else {
                    return o1.getHost().compareTo(o2.getHost());
                }
            }
        });
        for (HostAndPort hostAndPort : hostAndPortList) {
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

    public String hashString() {
        return hashCode() + "";
    }

    protected RedisConfig me() {
        return this;
    }

    private class RedisClientBuilder {

        public synchronized JedisPool buildJedisPool(RedisClient redisClient) {
            JedisPool jedisPool = null;
            HostAndPort hostAndPort = base.iterator().next();
            if (password == null) {
                jedisPool = new JedisPool(genericObjectPoolConfig, hostAndPort.getHost(), hostAndPort.getPort(),
                        timeout, password);
            } else {
                jedisPool = new JedisPool(genericObjectPoolConfig, hostAndPort.getHost(), hostAndPort.getPort(),
                        timeout);
            }
            return jedisPool;
        }


        public synchronized JedisCluster buildJedisCluster(RedisClient redisClient) {
            JedisCluster jedisCluster = null;
            if (password == null) {
                jedisCluster =
                        new JedisCluster(base, connectionTimeout, soTimeout, maxAttempts, genericObjectPoolConfig);
            } else {
                jedisCluster = new JedisCluster(base, connectionTimeout, soTimeout, maxAttempts, password,
                        genericObjectPoolConfig);
            }
            return jedisCluster;
        }

        private synchronized RedisClient newRedisClient(RedisClient redisClient, Class clazz) {
            if (clazz.isAssignableFrom(JedisPool.class)) {
                JedisPool jedisPool = buildJedisPool(redisClient);
                redisClient.put(me(), jedisPool);
            } else {
                JedisCluster jedisCluster = buildJedisCluster(redisClient);
                redisClient.put(me(), jedisCluster);
            }
            return redisClient;
        }

        private synchronized RedisClient redisClient(RedisClient redisClient, Class clazz) {
            if (clazz.isAssignableFrom(JedisPool.class)) {
                JedisPool jedisPool = buildJedisPool(redisClient);
                try {
                    redisClient.add(me(), jedisPool);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                JedisCluster jedisCluster = buildJedisCluster(redisClient);
                try {
                    redisClient.add(me(), jedisCluster);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return redisClient;
        }


        public synchronized RedisClient buildClusterClient(RedisClient rc) {
            try {
                return redisClient(rc, JedisCluster.class);
            } catch (Exception e) {
                SystemLogger.error("parse Cluster Client error {}", e);
            }
            return rc;
        }

        public synchronized RedisClient buildNoClusterClient(RedisClient rc) {
            try {
                return redisClient(rc, JedisPool.class);
            } catch (Exception e) {
                SystemLogger.error("parse NoCluster Client error {}", e);
            }
            return rc;
        }

        public synchronized RedisClient buildClient(RedisClient rc) {
            try {
                try {
                    redisClient(rc, JedisPool.class);
                } catch (Exception e) {
                    try {
                        redisClient(rc, JedisCluster.class);
                    } catch (Exception ee) {
                        redisClient(rc, JedisPool.class);
                    }
                }
            } catch (Exception e) {
                SystemLogger.error("parse NoCluster Client error {}", e);
            }
            return rc;
        }

        public synchronized RedisClient buildNewRedisClient(RedisClient rc) {
            try {
                try {
                    newRedisClient(rc, JedisPool.class);
                } catch (Exception e) {
                    try {
                        newRedisClient(rc, JedisCluster.class);
                    } catch (Exception ee) {
                        newRedisClient(rc, JedisPool.class);
                    }
                }
            } catch (Exception e) {
                SystemLogger.error("parse NoCluster Client error {}", e);
            }
            return rc;
        }

        public synchronized RedisClient redisClient(String id) {
            RedisClient redisClient = new RedisClient(id);
            if (status == 0) {
                buildClient(redisClient);
            } else if (status > 0) {
                buildClusterClient(redisClient);
            } else {
                buildNoClusterClient(redisClient);
            }
            return redisClient;
        }


        public synchronized RedisClient redisClient() {
            String id = hashString();
            return redisClient(id);
        }

        public synchronized RedisClient newRedisClient() {
            String id = hashString();
            RedisClient redisClient = new RedisClient(id);
            return buildNewRedisClient(redisClient);
        }
    }
}