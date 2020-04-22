package cn.lqcnb.homemall.consumer.email;

import cn.lqcnb.homemall.consumer.email.sink.EmailServiceSink;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * @author lqc520
 * @Description: test
 * @date 2020/3/15 1:29
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableBinding({EmailServiceSink.class})
public class EmailConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmailConsumerApplication.class);
    }
}
