package com.crude.travelcrew.domain.email.service;

import com.crude.travelcrew.domain.email.model.EmailSendReq;
import com.crude.travelcrew.domain.email.model.EmailVerifyReq;
import com.crude.travelcrew.global.redis.RedisKey;
import com.crude.travelcrew.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableAsync
public class EmailSenderServiceImpl implements EmailSenderService {

    public static final String subject = "[동행크루] 이메일 인증 코드 입니다.";
    public static final String text = "동행크루 회원 가입 인증코드 : ";
    public static final String authCode = UUID.randomUUID().toString();
    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender javaMailSender;
    private final RedisService redisService;

    @Override
    @Async
    public void sendEmail(EmailSendReq emailSendReq) {
        try {
            SimpleMailMessage smm = new SimpleMailMessage();
            smm.setFrom(from);
            smm.setTo(emailSendReq.getEmail());
            smm.setSubject(subject);
            smm.setText(text + authCode);
            this.javaMailSender.send(smm);
            redisService.setDataWithExpiration(RedisKey.EAUTH.getKey() + emailSendReq.getEmail(), authCode, 60 * 5L);
            log.info("email send success, authCode : " + redisService.getData(RedisKey.EAUTH.getKey() + emailSendReq.getEmail()));
        } catch (Exception e) {
            log.error("can not send email. [" + e.getMessage() + "]");
        }
    }

    @Transactional
    @Override
    public Boolean verifyEmail(EmailVerifyReq emailVerifyReq) {
        String storedCode = redisService.getData(RedisKey.EAUTH.getKey() + emailVerifyReq.getEmail());
            log.info("redis stored key : " + storedCode);
            log.info("input authCode : " + authCode);
        if (storedCode != null && storedCode.equals(authCode)) {
            redisService.deleteData(RedisKey.EAUTH.getKey() + emailVerifyReq.getEmail());
            return true;
        } else return false;
    }
}