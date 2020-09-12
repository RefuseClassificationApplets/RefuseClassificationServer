package com.hsspace.hs.manage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Email类
 * Git to： http://hs.mccspace.com:3000/Qing_ning/HSPasswordManagerServer/
 *
 * @TIME 2020/7/22 22:10
 * @AUTHOR 韩硕~
 */

public class Email {

    public static Session getSession(){
        // 服务器地址:
        String smtp = "smtp.office365.com";
        String username = "jxsmtp101@outlook.com";
        String password = "********";
        Properties props = new Properties();
        props.put("mail.smtp.host", smtp); // SMTP主机名
        props.put("mail.smtp.port", "587"); // 主机端口号
        props.put("mail.smtp.auth", "true"); // 是否需要用户认证
        props.put("mail.smtp.starttls.enable", "true"); // 启用TLS加密
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        session.setDebug(true);
        return session;
    }

}
