package cn.lqcnb.mall.api.controller;

import cn.lqcnb.mall.api.entity.Email;
import cn.lqcnb.mall.api.service.EmailService;
import cn.lqcnb.mall.common.entity.R;
import cn.lqcnb.mall.common.utils.EmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

/**
 * @author lqc520
 * @Description: 邮件管理
 * @date 2020/3/4 16:53
 */
@RestController
@Api(tags = "邮件系统管理")
@CrossOrigin
@RequestMapping("api/email")
public class EmailController {
    @Autowired
    EmailUtils emailUtils;
    @Autowired
    private EmailService emailService;

    @GetMapping("/send")
    public void sendSimpleMail(){
        emailUtils.sendSimpleMail("1402548358@qq.com","这是一封简单文本邮件","明天很美好，今天很重要");
    }

    @GetMapping("/sendHtml")
    public void sendHtmlMailTest() throws MessagingException {
        String content="<html>\n"+
                "<body>\n"+
                "<h3>hello 明天要加油哦！</h3>\n"+
                "</body>\n"+
                "</html>";
        StringBuffer sb = new StringBuffer();
        sb.append("<h1>大标题-h1</h1>")
                .append("<p style='color:#F00'>红色字</p>")
                .append("<p style='text-align:right'>右对齐</p>");
        emailUtils.sendHtmlMail("1402548358@qq.com","这是一封html邮件",sb.toString()+content);
    }


    @ApiOperation("获取邮件配置")
    @GetMapping("get")
    public Email get(){
        return emailService.get();
    }

    @ApiOperation("修改邮件配置信息")
    @PatchMapping("update")
    @ApiImplicitParam(name = "webSite" ,value = "网站信息",paramType="query",dataType = "email",required = true)
    public R update(Email email){
        if(emailService.update(email)){
            R.ok();
        }
        return R.error();
    }

}
