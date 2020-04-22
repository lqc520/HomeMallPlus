package cn.lqcnb.homemall.service.email.service;


import cn.lqcnb.homemall.api.email.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author lqc520
 * @Description: 邮件服务
 * @date 2020/3/15 0:06
 */
@Service
public class EmailServiceImpl implements EmailService {

//    @Resource
//    EmailUtils emailUtils;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    @Override
    @Async
    public void sendSimpleMail(String to,String subject,String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(from);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    @Override
    @Async
    public void sendHtmlMail(String to ,String subject,String content){
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setText(content,true);
            mailSender.send(message);
            logger.info("发送给: "+to+" 邮件成功");
        } catch (MessagingException e) {
            logger.error("发送静态邮件失败：",e);
        }

    }
}
