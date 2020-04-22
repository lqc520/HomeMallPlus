package cn.lqcnb.homemall.consumer.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author lqc520
 * @Description: 用户服务
 * @date 2020/3/15 22:21
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MemberConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MemberConsumerApplication.class);
    }
}
