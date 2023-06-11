package com.ll.tenmindaily.boundedContext.emailVerification.service;

import com.ll.tenmindaily.base.rsData.RsData;
import com.ll.tenmindaily.boundedContext.email.service.EmailService;
import com.ll.tenmindaily.boundedContext.emailVerification.Entity.EmailVerification;
import com.ll.tenmindaily.boundedContext.emailVerification.repository.EmailVerificationRepository;
import com.ll.tenmindaily.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final EmailService emailService;
    private final EmailVerificationRepository emailVerificationRepository;

    @Async
    public void send(Member member) {
        String email = member.getEmail();
        //TODO: 하드 코딩 제거하기
        String title = "[하루10분 이메일인증] 안녕하세요 %s님. 링크를 클릭하여 회원가입을 완료해주세요.".formatted(member.getUsername());
        String url = genEmailVerificationUrl(member);

        emailService.sendEmail(email, title, url);
    }

    public String genEmailVerificationUrl(Member member) {
        return genEmailVerificationUrl(member.getId());
    }

    public String genEmailVerificationUrl(long memberId) {
        String code = genEmailVerificationCode(memberId);

        //TODO: 하드 코딩 제거하기
        return "http://localhost:8080/emailVerification/verify?memberId=%d&code=%s".formatted(memberId, code);
    }

    public String genEmailVerificationCode(long memberId) {
        String code = UUID.randomUUID().toString();

        EmailVerification emailVerification = EmailVerification.builder()
                .memberId(memberId)
                .code(code)
                .expireDate(LocalDateTime.now().plusSeconds(60 * 60 * 1))
                .build();

        emailVerificationRepository.save(emailVerification);

        return code;
    }

    public RsData verifyVerificationCode(long memberId, String code) {
        String foundedCode = findEmailVerificationCode(memberId);

        if (!foundedCode.equals(code)) {
            return RsData.of("F-1", "만료되었거나 유효하지 않은 코드입니다.");
        }

        return RsData.of("S-1", "인증된 코드 입니다.");
    }

    public String findEmailVerificationCode(long memberId) {
        return emailVerificationRepository.findByMemberId(memberId).getCode();
    }
}
