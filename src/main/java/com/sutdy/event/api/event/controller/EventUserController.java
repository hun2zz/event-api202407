package com.sutdy.event.api.event.controller;


import com.sutdy.event.api.event.service.EventUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class EventUserController {
    private final EventUserService eventUserService;


    //이메일 중복확인 API
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(String email) {
        boolean b = eventUserService.checkEmailDuplicate(email);
        return ResponseEntity.ok().body(b);
    }

    //인증 코드 검증 API
    @GetMapping("/code")
    public ResponseEntity<?> verifyCode(String email, String code) {
        log.info("{}'s verify code is [ {} ]", email, code);
        boolean isMatch = eventUserService.isMatchCode(email, code);
        return ResponseEntity.ok().body(isMatch);
    }
}
