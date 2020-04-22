package cn.lqcnb.homemall.service.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author lqc520
 * @Description: 认证服务器
 * ({"cn.lqcnb.homemall.api.feign","cn.lqcnb.homemall.service.oauth2"})
 * (scanBasePackages = {"cn.lqcnb.homemall.api.feign","cn.lqcnb.homemall.service.oauth2"})
 * @date 2020/3/16 21:52
 */
@SpringBootApplication(scanBasePackages = {"cn.lqcnb.homemall.service.oauth2","cn.lqcnb.homemall.service.profile"})
@EnableDiscoveryClient
@EnableFeignClients
public class Oauth2ServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2ServiceApplication.class);
    }
}
