package cn.lqcnb.homemall.consumer.email.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author lqc520
 * @Description: 自定义 Bindings
 * @date 2020/3/20 23:28
 * @see cn.lqcnb.homemall.consumer.email.service
 */
public interface EmailServiceSink {
    @Input("email-service-topic")
    SubscribableChannel sendHtmlMail();
}
