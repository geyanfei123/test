package com.xieke.test.tyqxcms.service.impl;

import javax.mail.MessagingException;

import javax.mail.internet.MimeMessage;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.xieke.test.tyqxcms.service.IMailService;

@Component
@Service(version = "1.0.0", timeout = 60000)
public class MailServiceImpl implements IMailService{

	 private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
    private JavaMailSender mailSender;
    /**
     * 配置文件中我的qq邮箱
     */
    @Value("${spring.mail.from}")
    private String from;

    /**
     * 发送HTML邮件
     * @param to 收件者
     * @param subject 邮件主题
     * @param content 文本内容
     */
    @Async
    @Override
    public void sendHtmlMail(String to,String subject,String content) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setTo(to);
            helper.setText(content, true);
            mailSender.send(message);
            //日志信息
            logger.info("邮件已经发送。");
        } catch (MessagingException e) {
            logger.error("发送邮件时发生异常！", e);
        }
    }
}
