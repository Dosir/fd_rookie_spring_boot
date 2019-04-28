package com.fd.rookie.spring.boot.config.redis;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * redis锁的封装类
 */
@Configuration
//springboot为我们自动装配了整合redis需要的用到的各种bean，我们只需要从spring容器中直接获取就可以了。
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisLockHelper {
    private static final String DELIMITER = "|";

    //线程池
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newScheduledThreadPool(10);

    private final StringRedisTemplate stringRedisTemplate;

    public RedisLockHelper(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 获取锁
     * @param lockKey   lockKey
     * @param uuid      UUID
     * @param timeout   超时时间
     * @param unit      时间单位
     * @return
     */
    public boolean lock(String lockKey, final String uuid, long timeout, final TimeUnit unit) {
        final long milliseconds = Expiration.from(timeout, unit).getExpirationTimeInMilliseconds();
        // setIfAbsent方法：如果key不存在，则设置值，返回true；否则，不设置，返回false
        // 如果返回true：则客户端获取锁，接下类，我们就能通过expire方法给该锁设置一个超时时间，避免死锁。
        // 如果返回false：表示该锁已被其他客户端取得，接下来，我们可以先返回或进行重试等对方完成或等待锁超时。
        boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, (System.currentTimeMillis() + milliseconds) + DELIMITER + uuid);
        if (flag) {
            stringRedisTemplate.expire(lockKey, timeout, TimeUnit.SECONDS);
        } else {
            //getAndSet方法：以原子方式设置给定值，并返回以前的值【原子操作】
            String oldVal = stringRedisTemplate.opsForValue().getAndSet(lockKey, (System.currentTimeMillis() + milliseconds) + DELIMITER + uuid);
            final String[] oldValues = oldVal.split(Pattern.quote(DELIMITER));
            //如果已经过了之前设置的超时时间，说明锁已经被释放
            if (Long.parseLong(oldValues[0]) <= System.currentTimeMillis()) {
                return true;
            }
        }
        return flag;
    }

    public void unlock(String lockKey, String value) {
        unlock(lockKey, value, 0, TimeUnit.MILLISECONDS);
    }

    public void unlock(final String lockKey, final String uuid, long delayTime, TimeUnit unit) {
        if (StringUtils.isEmpty(lockKey)) {
            return;
        }
        if (delayTime <= 0) {
            doUnLock(lockKey, uuid);
        } else {
            EXECUTOR_SERVICE.schedule(() -> doUnLock(lockKey, uuid), delayTime, unit);
        }
    }

    private void doUnLock(final String lockKey, final String uuid) {
        String val = stringRedisTemplate.opsForValue().get(lockKey);
        final String[] values = val.split(Pattern.quote(DELIMITER));
        if (values.length <= 0) {
            return;
        }
        if (uuid.equals(values[1])) {
            stringRedisTemplate.delete(lockKey);
        }
    }

}
