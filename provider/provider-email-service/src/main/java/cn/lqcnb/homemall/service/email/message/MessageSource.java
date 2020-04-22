package cn.lqcnb.homemall.service.email.message;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author lqc520
 * @Description: 自定义 Binding
 * @date 2020/3/20 23:01
 * @see cn.lqcnb.homemall.service.email.service
 */
public interface MessageSource {
    @Output("email-service-topic")
    MessageChannel htmlMail();
}
