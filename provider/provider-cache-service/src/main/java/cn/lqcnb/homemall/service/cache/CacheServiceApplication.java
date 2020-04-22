package cn.lqcnb.homemall.service.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lqc520
 * @Description: 缓存
 * @date 2020/3/23 11:24
 * @see cn.lqcnb.homemall.service.cache
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CacheServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CacheServiceApplication.class);
    }
}
