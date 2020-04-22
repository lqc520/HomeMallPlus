package cn.lqcnb.homemall.consumer.email.consumer;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

/**
 * @author lqc520
 * @Description: test
 * @date 2020/3/20 23:31
 * @see cn.lqcnb.homemall.consumer.email.service
 */
@Service
public class EmailReceive {

    @StreamListener("email-service-topic")
    public void receiveEmailState(String message) {
        System.out.println(message);
    }
}
