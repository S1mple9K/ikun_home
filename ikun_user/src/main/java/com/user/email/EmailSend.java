package com.user.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * 邮箱发送
 *
 * @author 9K
 * @create: 2023-10-14 14:30
 */

@Component
public class EmailSend {

    /**
     * 短信发送API
     */
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private String you_email;

    /**
     * 短信发送方法
     *
     * @param userId   用户id
     * @param nickName 用户名称
     * @param opinion  建议
     */
    public boolean sendEmail(String userId, String nickName, String opinion) {
        SimpleMailMessage message = new SimpleMailMessage();

        try {
            // 设置邮件内容
            message.setFrom(fromEmail);
            message.setTo(you_email);
            message.setSubject("用户建议-用户:"+nickName+"("+userId+")");
            message.setText(opinion);
            System.out.println("message.getFrom() = " + message.getFrom());
            System.out.println("message.getTo() = " + message.getTo());
            System.out.println("message.getSubject() = " + message.getSubject());
            System.out.println("message.getText() = " + message.getText());
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            // 处理发送邮件异常
            e.printStackTrace();
        }
        return false;
    }
}
