package cn.lqcnb.homemall.service.cache.service;

import cn.lqcnb.homemall.service.cache.api.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author lqc520
 * @Description: 缓存服务
 * @date 2020/3/23 11:47
 * @see cn.lqcnb.homemall.service.cache.service
 */
@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void put(String key, Object val) {
        redisTemplate.opsForValue().set(key, val);
    }

    @Override
    public void put(String key, Object val, int seconds) {
        redisTemplate.opsForValue().set(key, val, seconds, TimeUnit.SECONDS);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }
}
