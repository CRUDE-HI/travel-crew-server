package com.crude.travelcrew.domain.email.service;

import com.crude.travelcrew.domain.email.model.EmailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderServiceImpl implements EmailSenderService {

    public static final String subject = "[동행크루] 이메일 인증 코드 입니다.";

    @Value("${spring.mail.username}")
    private String from;
    private final JavaMailSender javaMailSender;

    @Override
    public boolean sendEmail(EmailMessage emailMessage, String authCode) {
        try {
            SimpleMailMessage smm = new SimpleMailMessage();
            smm.setFrom(from);
            smm.setTo(emailMessage.getTo());
            smm.setSubject(subject);
            smm.setText(emailMessage.getMessage() + authCode);
            this.javaMailSender.send(smm);
        } catch (Exception e) {
            log.error("can not send email. [" + e.getMessage() + "]");
            return false;
        }
        log.info("email send success");
        return true;
    }
}