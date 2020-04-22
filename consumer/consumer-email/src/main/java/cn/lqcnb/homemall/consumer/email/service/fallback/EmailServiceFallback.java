package cn.lqcnb.homemall.consumer.email.service.fallback;

import cn.lqcnb.homemall.consumer.email.service.EmailService;
import org.springframework.stereotype.Component;

/**
 * @author lqc520
 * @Description: 邮件服务
 * @date 2020/3/15 19:50
 */
@Component
public class EmailServiceFallback implements EmailService {
    @Override
    public String sentHtml() {
        return "邮件发送错误";
    }
}
