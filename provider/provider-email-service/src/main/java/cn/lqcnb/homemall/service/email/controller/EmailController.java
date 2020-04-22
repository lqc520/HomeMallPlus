package cn.lqcnb.homemall.service.email.controller;

import cn.lqcnb.homemall.commons.dto.ResponseResult;
import cn.lqcnb.homemall.service.email.producer.MessageProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lqc520
 * @Description: test
 * @date 2020/3/15 19:47
 */
@RestController
public class EmailController {
//    @Resource
//    EmailService emailService;
//    @Value("${server.port}")
//    String port;
//
//    @GetMapping("sendHtml")
//    public String sendHtmlMail(){
//        String content="<html>\n"+
//                "<body>\n"+
//                "<h3>hello 明天要加油哦！</h3>\n"+
//                "</body>\n"+
//                "</html>";
//        StringBuffer sb = new StringBuffer();
//        sb.append("<h1>大标题-h1</h1>")
//                .append("<p style='color:#F00'>红色字</p>")
//                .append("<p style='text-align:right'>右对齐</p>");
//        emailService.sendHtmlMail("1402548358@qq.com","这是一封html邮件",sb.toString()+content);
//        return port;
//    };


    @Resource
    private MessageProducer messageProducer;

    @GetMapping("sendHtml")
    public ResponseResult<Void> sendHtmlMail(){

        String content="<html>\n"+
                "<body>\n"+
                "<h3>hello RocketMq ！</h3>\n"+
                "</body>\n"+
                "</html>";
        StringBuffer sb = new StringBuffer();
        sb.append("<h1>你好 RocketMq</h1>")
                .append("<p style='color:#F00'>红色字</p>")
                .append("<p style='text-align:right'>右对齐</p>");

        messageProducer.sendHtmlMail("1402548358@qq.com","这是一封html邮件",sb.toString()+content);;

        // 发送成功
        return new ResponseResult<Void>(ResponseResult.CodeStatus.OK, "发送成功");

    }
}
