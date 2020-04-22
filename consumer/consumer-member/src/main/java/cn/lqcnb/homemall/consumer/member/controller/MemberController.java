package cn.lqcnb.homemall.consumer.member.controller;


import cn.lqcnb.homemall.consumer.member.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lqc520
 * @Description: 用户服务
 * @date 2020/3/15 22:23
 */
@RestController
public class MemberController {

    @Resource
    EmailService emailService;

//    @Resource
//    EmailService emailService;

    @GetMapping("sent")
    public String test(){
        return emailService.sentHtml();
    }

//    @GetMapping("sent1")
//    public String test1(){
//        return emailService.sentHtml();
//    }
}
