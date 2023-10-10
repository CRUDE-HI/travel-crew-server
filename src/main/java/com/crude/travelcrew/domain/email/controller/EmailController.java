package com.crude.travelcrew.domain.email.controller;

import com.crude.travelcrew.domain.email.model.EmailSendReq;
import com.crude.travelcrew.domain.email.model.EmailVerifyReq;
import com.crude.travelcrew.domain.email.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailSenderService emailSenderService;

    // 이메일 인증번호 발송
    @PostMapping("/email/send")
    public ResponseEntity<?> sendEmail(@RequestBody EmailSendReq emailSendReq) {
        this.emailSenderService.sendEmail(emailSendReq);
        return ResponseEntity.ok("이메일 전송 및 Redis에 저장");
    }

    @PostMapping("/email/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody EmailVerifyReq emailVerifyReq) {
        boolean result = emailSenderService.verifyEmail(emailVerifyReq);
        if (result) {
            return ResponseEntity.ok("인증성공");
        } else return ResponseEntity.badRequest().body("인증실패");
    }

}