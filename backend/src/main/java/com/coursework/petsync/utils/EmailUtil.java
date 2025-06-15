package com.coursework.petsync.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class EmailUtil {
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${email.code.expiration:300000}") // 默认5分钟过期
    private long codeExpiration;

    @Value("${email.rate.limit:60000}") // 默认1分钟频率限制
    private long rateLimit;

    // 使用线程安全的Map
    private final Map<String, String> emailCodeStore = new ConcurrentHashMap<>();
    private final Map<String, Long> emailRequestTimes = new ConcurrentHashMap<>();

    private final JavaMailSender mailSender;

    public EmailUtil(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean sendEmailVerificationCode(String email) {
        if (!isValidEmail(email)) {
            log.warn("Invalid email format: {}", email);
            return false;
        }

        // 清理过期的验证码
        cleanExpiredCodes();

        // 频率限制检查
        if (isRequestTooFrequent(email)) {
            log.warn("Email sending too frequent for: {}", email);
            return false;
        }

        String code = generateVerificationCode();

        try {
            sendEmail(email, code);
            log.info("Verification code sent to: {}", email);

            emailCodeStore.put(email, code);
            emailRequestTimes.put(email, System.currentTimeMillis());
            return true;
        } catch (Exception e) {
            log.error("Failed to send email to: {}", email, e);
            return false;
        }
    }

    private void sendEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("您的验证码");
        message.setText("您的验证码是：" + code + "\n验证码有效期为5分钟。");
        mailSender.send(message);
    }

    public boolean verifyEmailCode(String email, String code) {
        cleanExpiredCodes();
        String storedCode = emailCodeStore.get(email);
        boolean isValid = storedCode != null && storedCode.equals(code);
        if (isValid) {
            emailCodeStore.remove(email);
            emailRequestTimes.remove(email);
        }
        return isValid;
    }

    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    private boolean isRequestTooFrequent(String email) {
        Long lastRequestTime = emailRequestTimes.get(email);
        return lastRequestTime != null &&
                (System.currentTimeMillis() - lastRequestTime) < rateLimit;
    }

    private void cleanExpiredCodes() {
        long currentTime = System.currentTimeMillis();
        emailRequestTimes.entrySet().removeIf(entry ->
                (currentTime - entry.getValue()) > codeExpiration
        );
        emailCodeStore.keySet().removeIf(key ->
                !emailRequestTimes.containsKey(key)
        );
    }

    // 测试用的getter方法
    String getStoredCode(String email) {
        return emailCodeStore.get(email);
    }

    // 测试用的setter方法
    void setCodeExpiration(long codeExpiration) {
        this.codeExpiration = codeExpiration;
    }

    void setRateLimit(long rateLimit) {
        this.rateLimit = rateLimit;
    }
}