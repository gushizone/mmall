package org.mmall.common;

import org.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by geely
 *
 * 对redis连接池进行封装 - jedis
 */
@Deprecated
public class RedisPool {
    //jedis连接池
    private static JedisPool pool;
    //最大连接数
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20"));
    //在jedispool中最大的idle状态(空闲的)的jedis实例的个数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "20"));
    //在jedispool中最小的idle状态(空闲的)的jedis实例的个数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle", "20"));
    // TODO 在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到的jedis实例肯定是可以用的。
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));
    // TODO 在return一个jedis实例的时候，是否要进行验证操作，如果赋值true。则放回jedispool的jedis实例肯定是可以用的。
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "true"));

    // redis - ip
    private static String redisIp = PropertiesUtil.getProperty("redis.ip");
    // redis - port
    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));


    /**
     * 初始化连接池
     */
    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();

        // 最大连接
        config.setMaxTotal(maxTotal);
        // 最大空闲
        config.setMaxIdle(maxIdle);
        // 最小空闲
        config.setMinIdle(minIdle);

        // 引用时是否验证可用
        config.setTestOnBorrow(testOnBorrow);
        // 返回时是否验证可用
        config.setTestOnReturn(testOnReturn);

        // 连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时。（默认为true）
        config.setBlockWhenExhausted(true);

        pool = new JedisPool(config, redisIp, redisPort, 1000 * 2);
    }

    static {
        initPool();
    }

    /**
     * 获取连接
     * @return
     */
    public static Jedis getJedis() {
        return pool.getResource();
    }

    /**
     * 放回坏连接
     * @param jedis
     */
    public static void returnBrokenResource(Jedis jedis) {
        pool.returnBrokenResource(jedis);
    }


    /**
     * 放回连接
     * @param jedis
     */
    public static void returnResource(Jedis jedis) {
        pool.returnResource(jedis);
    }


    // TODO
    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("geelykey", "geelyvalue");
        returnResource(jedis);

        pool.destroy();//临时调用，销毁连接池中的所有连接
        System.out.println("program is end");


    }


}
