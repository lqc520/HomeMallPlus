package cn.lqcnb.mall.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author lqc520
 * @Description: 发送邮件
 * @date 2020/3/4 16:43
 */
@Component
@Async
public class EmailUtils {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    /**
     * 发送简单文本的邮件方法
     * @param to
     * @param subject
     * @param content
     */
    public void sendSimpleMail(String to,String subject,String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(from);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }

    /**
     * 发送HTML邮件的方法
     * @param to
     * @param subjecr
     * @param content
     */
    public void sendHtmlMail(String to ,String subjecr,String content){
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject(subjecr);
            helper.setText(content,true);
            mailSender.send(message);
            logger.info("发送给: "+to+" 邮件成功");
        } catch (MessagingException e) {
            logger.error("发送静态邮件失败：",e);
        }

    }






}
