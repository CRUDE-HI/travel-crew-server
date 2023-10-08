package com.crude.travelcrew.domain.email.service;

import com.crude.travelcrew.domain.email.model.EmailMessage;

public interface EmailSenderService {
    // 이메일 전송
    boolean sendEmail(EmailMessage emailMessage, String authCode);

}
