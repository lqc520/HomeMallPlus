package cn.lqcnb.homemall.consumer.email.controller;


import cn.lqcnb.homemall.consumer.email.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lqc520
 * @Description: test
 * @date 2020/3/15 1:33
 */

@RestController
public class EmailController {
    //@Reference(version = "1.0.0")
    @Resource
    EmailService emailService;

    @GetMapping("send")
    public String sendHtmlMail(){
        return emailService.sentHtml();
    };
}
