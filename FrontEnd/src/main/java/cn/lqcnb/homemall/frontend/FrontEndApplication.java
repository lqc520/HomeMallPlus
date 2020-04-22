package cn.lqcnb.homemall.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lqc520
 * @Description: 前端
 * @date 2020/3/30 23:35
 * @see cn.lqcnb.homemall.frontend
 */

@SpringBootApplication
@EnableDiscoveryClient
public class FrontEndApplication {
    public static void main(String[] args) {
        SpringApplication.run(FrontEndApplication.class);
    }
}
