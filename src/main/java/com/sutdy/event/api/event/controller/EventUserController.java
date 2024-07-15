package com.sutdy.event.api.event.controller;


import com.sutdy.event.api.event.dto.request.EventUserSaveDto;
import com.sutdy.event.api.event.dto.request.LoginRequestDto;
import com.sutdy.event.api.event.service.EventUserService;
import com.sutdy.event.api.exception.LoginFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
//@CrossOrigin(origins = "http://localhost:3000") // 클라이언트를 확인
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

    //회원가입 마무리 처리
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody EventUserSaveDto dto) {

        log.info("save user info - {}", dto);
        try {
            eventUserService.confirmSignUp(dto);
        } catch (LoginFailException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("saved success");
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody LoginRequestDto dto) {
        log.info("save user info - {}", dto);
        try {
            eventUserService.authenticate(dto);
            return ResponseEntity.ok().body("login success");
        } catch (Exception e) {
            //서비스에서 예외 발생 ( 로그인 실패 )
            String errorMessage = e.getMessage();
            return ResponseEntity.status(422).body(errorMessage);

        }
    }
}
