package com.sutdy.event.api.event.service;


import com.sutdy.event.api.event.entity.EmailVerification;
import com.sutdy.event.api.event.entity.EventUser;
import com.sutdy.event.api.event.repository.EmailVerificationRepository;
import com.sutdy.event.api.event.repository.EventUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class EventUserService {
    @Value("${study.mail.host}")
    private String mailHost;
    private final EventUserRepository eventUserRepository;
    private final EmailVerificationRepository emailVerificationRepository;

    private final JavaMailSender mailSender;

    public boolean checkEmailDuplicate(String email) {
        EventUser build = EventUser.builder()
                .email("abc@def.com" + (int) (Math.random() * 10))
                .build();
        eventUserRepository.save(build);
        boolean b = eventUserRepository.existsByEmail(email);

        //일련의 후속 처리 ( 데이터베이스 처리, 이메일 보내는 처리 )
        if (!b)processSignUp(email);
        return b;
    }

    private void processSignUp(String email) {
        //1. 임시 회원가입
        EventUser newEventUser = EventUser.builder()
                .email(email)
                .build();

        EventUser savedUser = eventUserRepository.save(newEventUser);

        //2. 이메일 인증코드 발송
        String code = sendVerificationEmail(email);

        //3. 인증 코드 정보를 데이터베이스에 저장
        EmailVerification verification = EmailVerification.builder()
                .verificationCode(code) //인증코드
                .expiryDate(LocalDateTime.now().plusMinutes(5)) //만료 시간 (5분뒤)
                .eventUser(savedUser) // FK
                .build();

        emailVerificationRepository.save(verification);
    }

    // 이메일 인증 코드 보내기
    public String sendVerificationEmail(String email) {

        // 검증 코드 생성하기
        String code = generateVerificationCode();

        // 이메일을 전송할 객체 생성
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            // 누구에게 이메일을 보낼 것인지
            messageHelper.setTo(email);
            // 이메일 제목 설정
            messageHelper.setSubject("[인증메일] 중앙정보스터디 가입 인증 메일입니다.");
            // 이메일 내용 설정
            messageHelper.setText(
                    "인증 코드: <b style=\"font-weight: 700; letter-spacing: 5px; font-size: 30px;\">" + code + "</b>"
                    , true
            );

            // 전송자의 이메일 주소
            messageHelper.setFrom(mailHost);

            // 이메일 보내기
            mailSender.send(mimeMessage);

            log.info("{} 님에게 이메일 전송!", email);

            return code;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 검증 코드 생성 로직 1000~9999 사이의 4자리 숫자
    private String generateVerificationCode() {
        return String.valueOf((int) (Math.random() * 9000 + 1000));
    }


    //인증코드 체크
    public boolean isMatchCode(String email, String code) {
        //이메일을 통해 회원정보를 탐색
        EventUser eventUser = eventUserRepository.findByEmail(email).orElse(null);
        if (eventUser != null) {
        //인증코드가 있는지 탐색
            EmailVerification ev = emailVerificationRepository.findByEventUser(eventUser).orElse(null);
        //인증코드가 있고, 만료시간이 지나지 않았고 코드번호가 일치할 경우
            if (ev!= null
                && ev.getExpiryDate().isAfter(LocalDateTime.now())
                    && code.equals(ev.getVerificationCode())
            ) {
                return true;
            }
        }



        return false;
    }
}