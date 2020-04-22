package cn.lqcnb.homemall.service.cache.controller;

import cn.lqcnb.homemall.service.cache.api.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lqc520
 * @Description: 缓存服务
 * @date 2020/3/23 11:49
 * @see cn.lqcnb.homemall.service.cache.controller
 */

@RestController
@RequestMapping(value = "cache")
public class RedisController {
    @Autowired
    private RedisService redisService;

    @PostMapping("redis/{key}/{val}")
    public String put(@PathVariable String key, @PathVariable String val) {
        redisService.put(key, val);
        return "ok";
    }

    @GetMapping(value = "redis/{key}")
    public String get(@PathVariable String key) {
        return (String) redisService.get(key);
    }
}
