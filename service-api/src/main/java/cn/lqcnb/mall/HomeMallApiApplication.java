package cn.lqcnb.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import tk.mybatis.spring.annotation.MapperScan;
/**
 * @author lqc520
 */
@MapperScan("cn.lqcnb.mall.api.mapper")
@SpringBootApplication
@EnableAsync
@EnableDiscoveryClient
@EnableFeignClients
public class HomeMallApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(HomeMallApiApplication.class, args);
    }


}
