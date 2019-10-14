package io.concurrent.ioconcurrent.site.redisson;

import io.concurrent.ioconcurrent.config.RedisRunner;
import io.concurrent.ioconcurrent.config.redisson.RedissonDistributedLocker;
import io.concurrent.ioconcurrent.entity.Response;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ProjectName: concurrent
 * @Package: io.concurrent.ioconcurrent.site.redisson
 * @Description:
 * @Author: AMGuo
 * @CreateDate: 2019-10-09 16:49
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/stock")
public class StockController {

    @Resource
    private RedisRunner redisRunner;

    @Resource
    private RedissonDistributedLocker redisson;

    @Resource
    private AmqpTemplate amqpTemplate;

    private String REDIS_STRING_KEY = "test:string";

    private String REDIS_ATOM_KEY = "test:atom";

    private String REDIS_REDISSON_KEY = "test:redisson";

    private String REDIS_REDISSON_LOCK_KEY = "test:redisson:lock";

    private String REDIS_REDIS_KEY = "test:redis";

    /**  利用redis string存储库存，有超发的可能 **/

    /**
     * 增加库存
     *
     * @return
     */
    @RequestMapping(value = "/increase/v1")
    public Response increaseV1() {
        String valueStr = redisRunner.cacheGet(REDIS_STRING_KEY);
        Integer value = (StringUtils.isEmpty(valueStr) ? 0 : Integer.valueOf(valueStr)) + 1;
        redisRunner.cacheAddUpdate(REDIS_STRING_KEY, value, 3600);
        return Response.buildSuccessResponse();
    }

    /**
     * 减少库存
     *
     * @return
     */
    @RequestMapping(value = "/decrease/v1")
    public Response decreaseV1() {
        String valueStr = redisRunner.cacheGet(REDIS_STRING_KEY);
        Integer value = (StringUtils.isEmpty(valueStr) ? 0 : Integer.valueOf(valueStr)) - 1;
        redisRunner.cacheAddUpdate(REDIS_STRING_KEY, value, 3600);
        return Response.buildSuccessResponse();
    }

    /**  利用redis incr/decr原子性操作，无超发的可能，有少发的可能 **/

    /**
     * 增加库存
     *
     * @return
     */
    @RequestMapping(value = "/increase/v2")
    public Response increaseV2() {
        Long value = redisRunner.incAdd(REDIS_ATOM_KEY);
        return Response.buildSuccessResponse();
    }

    /**
     * 减少库存
     *
     * @return
     */
    @RequestMapping(value = "/decrease/v2")
    public Response decreaseV2() {
        long value = redisRunner.decr(REDIS_ATOM_KEY);
        if (value < 0) {
            System.out.println("库存为空");
            return Response.buildErrorResponse("库存为空");
        }
        return Response.buildSuccessResponse();
    }

    /**  利用redisson分布式锁，无超发的可能，性能比较差 **/

    /**
     * 增加库存
     *
     * @return
     */
    @RequestMapping(value = "/increase/v3")
    public Response increaseV3() {
        boolean lock = redisson.tryLock(REDIS_REDISSON_LOCK_KEY, 100, 20);
        if (lock) {
            try {
                String valueStr = redisRunner.cacheGet(REDIS_REDISSON_KEY);
                Integer value = (StringUtils.isEmpty(valueStr) ? 0 : Integer.valueOf(valueStr)) + 1;
                redisRunner.cacheAddUpdate(REDIS_REDISSON_KEY, value, 3600);
                return Response.buildSuccessResponse();
            } catch (Exception e) {
                System.err.println(e);
                return Response.buildErrorResponse("异常");
            } finally {
                redisson.unlock(REDIS_REDISSON_LOCK_KEY);
            }
        } else {
            return Response.buildErrorResponse("获取库存锁失败");
        }
    }

    /**
     * 减少库存
     *
     * @return
     */
    @RequestMapping(value = "/decrease/v3")
    public Response decreaseV3() {
        boolean lock = redisson.tryLock(REDIS_REDISSON_LOCK_KEY, 100, 20);
        if (lock) {
            try {

                String valueStr = redisRunner.cacheGet(REDIS_REDISSON_KEY);
                Integer value = (StringUtils.isEmpty(valueStr) ? 0 : Integer.valueOf(valueStr)) - 1;
                redisRunner.cacheAddUpdate(REDIS_REDISSON_KEY, value, 3600);
                return Response.buildSuccessResponse();
            } catch (Exception e) {
                System.err.println(e);
                return Response.buildErrorResponse("异常");
            } finally {
                redisson.unlock(REDIS_REDISSON_LOCK_KEY);
            }
        } else {
            return Response.buildErrorResponse("获取库存锁失败");
        }
    }

    /**  利用redis队列结构（list），加仓入列，减仓出列，无超发可能，有少发可能 **/


    /**
     * 增加库存
     *
     * @return
     */
    @RequestMapping(value = "/increase/v4")
    public Response increaseV4() {
        redisRunner.lPush(REDIS_REDIS_KEY, 480000, "1");
        return Response.buildSuccessResponse();
    }

    /**
     * 减少库存
     *
     * @return
     */
    @RequestMapping(value = "/decrease/v4")
    public Response decreaseV4() {
        String s = redisRunner.lPop(REDIS_REDIS_KEY);
        System.out.println(s);
        return Response.buildSuccessResponse();
    }
}
