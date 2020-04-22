package cn.lqcnb.homemall.service.email;

import cn.lqcnb.homemall.service.email.message.MessageSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author lqc520
 * @Description: 邮件服务
 * @date 2020/3/15 0:01
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
@EnableBinding({MessageSource.class})
public class EmailServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmailServiceApplication.class);
    }
}
