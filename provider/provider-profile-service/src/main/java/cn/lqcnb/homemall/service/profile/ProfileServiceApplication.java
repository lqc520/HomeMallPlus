package cn.lqcnb.homemall.service.profile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lqc520
 * @Description: 资源服务器
 * @date 2020/3/17 22:42
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ProfileServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProfileServiceApplication.class);
    }
}
