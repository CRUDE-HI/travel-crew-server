package com.crude.travelcrew.domain.email.controller;

import com.crude.travelcrew.domain.email.model.EmailAuthReq;
import com.crude.travelcrew.domain.email.model.EmailMessage;
import com.crude.travelcrew.domain.email.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailSenderService emailSenderService;

    // 이메일 인증번호 발송
    @PostMapping("/email/send")
    public ResponseEntity<?> sendEmailAuth(@RequestBody EmailMessage emailMessage) {
        String authCode = UUID.randomUUID().toString();
        emailSenderService.sendEmail(emailMessage, authCode);
        return ResponseEntity.ok("이메일 전송됨");
    }
}
