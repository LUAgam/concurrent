package io.concurrent.ioconcurrent.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.util.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPubSub;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis操作类，封装了 缓存、队列、计数 等相关方法
 *
 * @author 17324
 */
public class RedisRunner {

    /**
     * 重定向次数
     **/

    public static int maxRedirections = 6;

    /**
     * 超时时间
     **/
    public static int timeout = 2000;

    public static JedisCluster jedis = null;

    /**
     * 集群方案初始化
     *
     * @param address 集群地址
     */
    public RedisRunner(String address, String password) {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        // 链接池中最大连接数
        poolConfig.setMaxTotal(32);
        // 链接池中最大空闲的连接数
        poolConfig.setMaxIdle(16);
        // 连接池中最少空闲的连接数
        poolConfig.setMinIdle(0);
        // 当连接池资源耗尽时，等待时间，超出则抛异常，默认为-1即永不超时
        poolConfig.setMaxWaitMillis(1000);
        // borrow的时候检测是有有效，如果无效则从连接池中移除，并尝试获取继续获取
        poolConfig.setTestOnBorrow(false);
        // return的时候检测是有有效，如果无效则从连接池中移除，并尝试获取继续获取
        poolConfig.setTestOnReturn(false);
        // 在evictor线程里头，当evictionPolicy.evict方法返回false时，而且testWhileIdle为true的时候则检测是否有效，如果无效则移除
        poolConfig.setTestWhileIdle(true);
        // 设置连接被回收前的最大空闲时间
        poolConfig.setMinEvictableIdleTimeMillis(5 * 60000);
        // 设置检测线程的运行时间间隔
        poolConfig.setTimeBetweenEvictionRunsMillis(60000);
        // 设置检测线程每次检测的对象数
        poolConfig.setNumTestsPerEvictionRun(-1);
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        //address = "rediscl.dev.rs.com:6379,rediscl.dev.rs.com:6380,rediscl.dev.rs.com:6381,rediscl.dev.rs.com:6382,rediscl.dev.rs.com:6383,rediscl.dev.rs.com:6384";
        String[] ss = address.split(",");
        for (String s : ss) {
            String[] uri = s.split(":");
            jedisClusterNodes.add(new HostAndPort(uri[0], Integer.parseInt(uri[1])));
        }
        if (StringUtils.isEmpty(password)) {
            jedis = new JedisCluster(
                    jedisClusterNodes,
                    RedisRunner.timeout,
                    RedisRunner.maxRedirections,
                    poolConfig);
        } else {
            jedis = new JedisCluster(
                    jedisClusterNodes,
                    RedisRunner.timeout,
                    RedisRunner.timeout,
                    RedisRunner.maxRedirections,
                    password,
                    poolConfig);
        }
    }

    /**
     * 停止Redis连接池方法（需在容器销毁时调用）
     */
    public static void stop() {
        if (jedis != null) {
            try {
                jedis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 缓存 添加/更新方法
     *
     * @param key    键
     * @param value  值
     * @param expiry 过期时间，单位秒（-1 = 永久有效）
     */
    public void cacheAddUpdate(String key, Object value, Integer expiry) {
        if (value == null) {
            return;
        }
        String v = getString(value);
        if (expiry.equals(-1)) {
            jedis.set(key, v);
        } else {
            String setex = jedis.setex(key, expiry, v);
        }
    }

    /**
     * 缓存 添加/更新方法
     *
     * @param key    键
     * @param value  值
     * @param expiry 过期时间，单位毫秒（-1 = 永久有效）
     */
    public void cacheAddUpdate(String key, Object value, Long expiry) {
        if (value == null) {
            return;
        }
        String v = getString(value);
        if (expiry.equals(-1)) {
            jedis.set(key, v);
        } else {
            jedis.set(key, v);
            jedis.pexpire(key, expiry);
        }
    }

    /**
     * 缓存 获取方法（重载方法，使用json反序列化）
     *
     * @param key  键
     * @param type 对象类型引用
     * @return 内容
     */
    public <T> T cacheGet(String key, TypeReference<T> type) {
        String s = cacheGet(key);
        return s == null ? null : JSON.parseObject(s, type);
    }

    /**
     * 缓存 获取方法（重载方法，使用json反序列化）
     *
     * @param key  键
     * @param type 对象类型
     * @return 内容
     */
    public <T> T cacheGet(String key, Class<T> type) {
        String s = cacheGet(key);
        return s == null ? null : JSON.parseObject(s, type);
    }

    /**
     * 缓存 获取方法
     *
     * @param key 键
     * @return 内容
     */
    public String cacheGet(String key) {
        return get(key);
    }

    /**
     * 缓存 更改过期时间方法
     *
     * @param key    键
     * @param expiry 过期时间，单位秒
     */
    public void cacheExpiry(String key, Integer expiry) {
        jedis.expire(key, expiry);
    }

    /**
     * 缓存 移除方法
     *
     * @param key 键
     */
    public void cacheRemove(String key) {
        remove(key);
    }

    /**
     * 缓存 判断是否存在方法
     *
     * @param key 键
     * @return 是否存在
     */
    public Boolean cacheExists(String key) {
        return exists(key);
    }

    /**
     * 计数 自增1方法
     *
     * @param key 键
     * @return 自增后数字
     */
    public Long incAdd(String key) {
        Long result = jedis.incr(key);
        return result;
    }

    /**
     * 计数 自增n方法
     *
     * @param key 键
     * @return 自增后数字
     */
    public Long incAdd(String key, int number) {
        Long result = jedis.incrBy(key, number);
        return result;
    }

    /**
     * 计数 获取方法
     *
     * @param key 键
     * @return 当前数字
     */
    public Long incGet(String key) {
        String result = get(key);
        return result == null ? null : Long.valueOf(result);
    }

    private Boolean exists(String key) {
        Boolean result = jedis.exists(key);
        return result;
    }

    private void remove(String key) {
        jedis.del(key);
    }

    private String get(String key) {
        String result = jedis.get(key);
        return result;
    }

    private String getString(Object value) {
        if (value instanceof String) {
            return (String) value;
        } else {
            return JSON.toJSON(value).toString();
        }
    }

    /**
     * hash操作
     *
     * @param key
     * @param map
     */
    public void hmset(String key, Map<String, String> map) {
        if (StringUtils.isEmpty(map)) {
            return;
        }
        jedis.hmset(key, map);
    }


    /**
     * hash操作
     *
     * @param key
     * @param map
     * @param milliseconds
     */
    public void hmset(String key, Map<String, String> map, long milliseconds) {
        if (StringUtils.isEmpty(map)) {
            return;
        }
        jedis.hmset(key, map);
        jedis.pexpire(key, milliseconds);
    }

    /**
     * hset
     *
     * @param key
     * @param field
     * @param value
     */
    public void hset(String key, String field, String value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field) || StringUtils.isEmpty(value)) {
            return;
        }
        jedis.hset(key, field, value);
    }

    /**
     * hset
     *
     * @param key
     * @param field
     * @param value
     * @param milliseconds
     */
    public void hset(String key, String field, String value, long milliseconds) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field) || StringUtils.isEmpty(value)) {
            return;
        }
        jedis.hset(key, field, value);
        jedis.pexpire(key, milliseconds);
    }

    /**
     * hexists
     *
     * @param key
     * @param field
     */
    public Boolean hexists(String key, String field) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field)) {
            return null;
        }
        Boolean flag = jedis.hexists(key, field);
        return flag;

    }

    /**
     * hkeys
     *
     * @param key
     * @return
     */
    public Set<String> hkeys(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        Set<String> hkeys = jedis.hkeys(key);
        return hkeys;
    }

    /**
     * hkeys
     *
     * @param key
     * @return
     */
    public List getFieldValue(String key, String field) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        List<String> value = jedis.hmget(key, field);
        return value;
    }

    /**
     * 订阅发布消息
     *
     * @param channel 频道
     * @param message 消息
     */
    public void publish(String channel, String message) {
        jedis.publish(channel, message);
    }


    /**
     * 订阅注册方法
     *
     * @param listener 监听器
     * @param channel  频道
     */
    public void subscribe(final JedisPubSub listener, final String channel) {
        new Thread() {
            @Override
            public void run() {
                jedis.subscribe(listener, channel);
            }
        }.start();
    }

    /**
     * hash getall
     *
     * @param key
     * @return
     */
    public Map<String, String> hgetAll(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        return jedis.hgetAll(key);
    }

    /**
     * hash get
     *
     * @param key
     * @return
     */
    public String hget(String key, String field) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field)) {
            return null;
        }
        return jedis.hget(key, field);
    }

    /**
     * hsetNX
     *
     * @param key
     * @param field
     * @param value
     */
    public void hsetNX(String key, String field, String value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field) || StringUtils.isEmpty(value)) {
            return;
        }
        jedis.hsetnx(key, field, value);
    }

    /**
     * setnx
     *
     * @param key
     * @param value
     * @return
     */
    public Long setnx(String key, String value, int seconds) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return null;
        }
        Long setnx = jedis.setnx(key, value);
        jedis.expire(key, seconds);
        return setnx;
    }

    public void sadd(String key, String... value) {
        jedis.sadd(key, value);
    }

    public boolean sismember(String key, String value) {
        return jedis.sismember(key, String.valueOf(value));
    }

    public void srem(String key, String... value) {
        jedis.srem(key, value);
    }

    public Long scard(String key) {
        return jedis.scard(key);
    }

    public Set<String> smembers(String key) {
        return jedis.smembers(key);
    }


    public void expire(String key, int seconds) {
        jedis.expire(key, seconds);
    }

    public void sadd(String key, long milliseconds, String... value) {
        jedis.sadd(key, value);
        jedis.pexpire(key, milliseconds);
    }

    /**
     * List头部追加记录
     *
     * @param key
     * @param milliseconds
     * @param value
     * @return
     */
    public long lPush(String key, long milliseconds, String... value) {
        long resultStatus = jedis.lpush(key, value);
        jedis.pexpire(key, milliseconds);
        return resultStatus;
    }

    /**
     * List尾部追加记录
     *
     * @param key
     * @param value
     * @return
     */
    public long rPush(String key, long milliseconds, String... value) {
        long resultStatus = jedis.rpush(key, value);
        jedis.pexpire(key, milliseconds);
        return resultStatus;
    }

    /**
     * 移除列表第一个值
     *
     * @param key
     * @return
     */
    public String lPop(String key) {
        String result = jedis.lpop(key);
        return result;
    }

    /**
     * 移除列表最后一个值
     *
     * @param key
     * @return
     */
    public String rPop(String key) {
        String result = jedis.rpop(key);
        return result;
    }

    /**
     * 根据 list的 key
     * 返回 list的 长度
     *
     * @param key
     * @return
     */
    public long length(String key) {
        Long listLength = jedis.llen(key);
        return listLength;
    }

    /**
     * 获取指定范围的记录
     * lrange 下标从0开始 -1表示最后一个元素
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> lRange(String key, long start, long end) {
        List<String> result = jedis.lrange(key, start, end);
        return result;
    }

    /**
     * 让列表只保留指定区间内的元素
     * ltrim 让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除
     * start和end为0时，即清空list
     *
     * @param key
     * @return
     */
    public String ltrim(String key, long start, long end) {
        String result = jedis.ltrim(key, start, end);
        return result;
    }

    /**
     * 缓存减1
     *
     * @param key
     * @return
     */
    public long decr(String key) {
        Long listLength = jedis.decr(key);
        return listLength;
    }


    /**
     * 计数 自增1方法
     *
     * @param key          键
     * @param milliseconds
     * @return 自增后数字
     */
    public Long incAdd(String key, long milliseconds) {
        Long result = jedis.incr(key);
        jedis.pexpire(key, milliseconds);
        return result;
    }

    /**
     * sorted set 集合
     *
     * @param key
     * @param score
     * @param member
     * @param milliseconds
     * @return
     */
    public Long zadd(String key, double score, String member, long milliseconds) {
        Long result = jedis.zadd(key, score, member);
        jedis.pexpire(key, milliseconds);
        return result;
    }

    /**
     * 返回有序集中，指定区间内的成员
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zrange(String key, long start, long end) {
        Set<String> zrange = jedis.zrange(key, start, end);
        return zrange;
    }

    /**
     * 执行lua脚本
     *
     * @param script lua脚本
     * @param num    参数个数
     * @param param  参数
     * @return
     */
    public Object evel(String script, int num, String... param) {
        return jedis.eval(script, num, param);
    }

}