package cn.lqcnb.homemall.service.email.producer;

import cn.lqcnb.homemall.api.email.EmailService;
import cn.lqcnb.homemall.service.email.message.MessageSource;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lqc520
 * @Description: 消息生产者
 * @date 2020/3/20 23:03
 * @see cn.lqcnb.homemall.service.email.service
 */
@Service
public class MessageProducer implements EmailService {
    @Resource
    private MessageSource source;
    @Resource
    private EmailService emailService;

    /**
     * 发送邮件
     * @return
     */
    public boolean sentEmailHtml(String playLoad){
        return source.htmlMail().send(MessageBuilder.withPayload(playLoad).build());
    }

    /**
     * 发送简单邮件
     *
     * @param to
     * @param subject
     * @param content
     */
    @Override
    public void sendSimpleMail(String to, String subject, String content) {

    }

    /**
     * 发送html邮件
     *
     * @param to
     * @param subject
     * @param content
     */
    @Override
    public void sendHtmlMail(String to, String subject, String content) {
        emailService.sendHtmlMail(to,subject,content);
        source.htmlMail().send(MessageBuilder.withPayload("发送给"+to+"邮件成功").build());
    }
}
