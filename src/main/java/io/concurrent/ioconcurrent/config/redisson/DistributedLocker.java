package io.concurrent.ioconcurrent.config.redisson;

import org.redisson.api.RBucket;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

public interface DistributedLocker {
    RLock lock(String lockKey);

    RLock lock(String lockKey, long timeout);

    RLock lock(String lockKey, TimeUnit unit, long timeout);

    boolean tryLock(String lockKey, long waitTime, long leaseTime);

    boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime);

    void unlock(String lockKey);

    void unlock(RLock lock);

    RBucket<Integer> getBucket(String lockKey);

}
