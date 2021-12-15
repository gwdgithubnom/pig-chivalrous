package org.gjgr.pig.chivalrous.redis;

import redis.clients.jedis.commands.JedisCommands;

/**
 * @author gongwendong
 * @time 10-05-2021  星期二
 * @description: pig-chivalrous-parent:
 * @target:
 * @more:
 */
@FunctionalInterface
public interface RedisOperation<T> {
    /**
     * use self to define the redis operation
     * with object type may be jedis or jediscluster
     * for jedis would get a new jedis
     * for cluster just use cluster
     * @param commands
     * @return
     */
    T jedisCommandsOperation(Object commands);
}
