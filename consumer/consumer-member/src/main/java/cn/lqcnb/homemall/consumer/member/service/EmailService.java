package cn.lqcnb.homemall.consumer.member.service;

import cn.lqcnb.homemall.consumer.member.service.fallback.EmailServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author lqc520
 * @Description: 邮件服务
 * @date 2020/3/15 19:49
 */
@FeignClient(value = "email-service-provider",fallback = EmailServiceFallback.class)
public interface EmailService {

    @GetMapping("sendHtml")
    String sentHtml();
}
