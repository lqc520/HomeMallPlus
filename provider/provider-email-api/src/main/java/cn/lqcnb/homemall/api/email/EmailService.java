package cn.lqcnb.homemall.api.email;

/**
 * @author lqc520
 * @Description: 邮件服务api
 * @date 2020/3/15 14:42
 */
public interface EmailService {


    /**
     * 发送简单邮件
     * @param to
     * @param subject
     * @param content
     */
    void sendSimpleMail(String to, String subject, String content);


    /**
     * 发送html邮件
     * @param to
     * @param subject
     * @param content
     */
    void sendHtmlMail(String to, String subject, String content);

}
