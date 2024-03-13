package org.nap.diabuddy_companion_server.service.impl;


import jakarta.annotation.Resource;
import org.apache.commons.lang.RandomStringUtils;
import org.nap.diabuddy_companion_server.service.EmailService;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class EmailServiceImpl implements EmailService {

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${mail.reply-to}")
    String replyTo;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;



    public void sendSimpleMessage(String to, String subject, String text) {
        try {


            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            helper.setReplyTo(replyTo);
            helper.setSentDate(new Date());

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }



    //向Redis存一个验证码，五分钟过期，并返回一个验证码

    /**
     *
     * @param email
     * @return code
     */
    public String storeCode(String email) {
        String code = generateVerificationCode();
        final String key = "verificationCode:" + email;
        HashMap<String, Object> value = new HashMap<>();
        value.put("code", code);
        value.put("expiry", LocalDateTime.now().plusMinutes(5)); // 5分钟后过期
        redisTemplate.opsForValue().set(key, value, 5, TimeUnit.MINUTES); // 存储验证码，并设置5分钟后过期
        return code;
    }

    public boolean validateCode(String email, String inputCode) {
        final String key = "verificationCode:" + email;
        Map<String, Object> value = (Map<String, Object>) redisTemplate.opsForValue().get(key);

        if (value == null) {
            return false;
        }

        String storedCode = (String) value.get("code");
        LocalDateTime expiry = (LocalDateTime) value.get("expiry");
        if (storedCode.equals(inputCode) && LocalDateTime.now().isBefore(expiry)) {
            redisTemplate.delete(key); // 验证成功后删除验证码
            return true;
        }
        return false;
    }

    public String generateVerificationCode() {
        return RandomStringUtils.randomAlphanumeric(6).toUpperCase();
    }


}
