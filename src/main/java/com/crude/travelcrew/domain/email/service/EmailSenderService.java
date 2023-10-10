package com.crude.travelcrew.domain.email.service;

import com.crude.travelcrew.domain.email.model.EmailSendReq;
import com.crude.travelcrew.domain.email.model.EmailVerifyReq;
import org.springframework.transaction.annotation.Transactional;

public interface EmailSenderService {
    // 이메일 전송
    void sendEmail(EmailSendReq emailSendReq);

    // 인증키 검증
    @Transactional
    Boolean verifyEmail(EmailVerifyReq emailVerifyReq);

}
