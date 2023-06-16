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
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final EmailService emailService;
    private final EmailVerificationRepository emailVerificationRepository;

    @Async
    public void send(Member member, String email) {
        if (email == null) {
            email = member.getEmail();
        }
        //TODO: 하드 코딩 제거하기
        String title = "[하루10분 이메일인증] 안녕하세요 %s님. 링크를 클릭하여 이메일 인증을 완료해주세요.".formatted(member.getUsername());
        String url = "<a href=\""+ genEmailVerificationUrl(member) + "\">이메일 인증 링크</a>";

        emailService.sendEmail(email, title, url);
    }

    public String genEmailVerificationUrl(Member member) {
        return genEmailVerificationUrl(member.getId());
    }

    /*
        이메일 인증 URI를 생성하는 메소드.
        member 테이블의 id 컬럼 데이터를 이용하여, 해당 member가 이전에 발급 받은 인증 코드가 있는지 판단하고,
        기존 인증 코드가 존재하지 않는다면, 새로운 인증 코드를 발급하여 인증 메일을 보내게 됩니다.
        기존 인증 코드가 존재한다면, 유효 기간을 검증하고,
        유효 기간이 지난 코드의 경우 파기(DB에서 삭제) 후, 인증 코드를 재발급 하여 인증 메일을 발송합니다.
        유효 기간이 지나지 않은 경우, 인증 코드를 새로 발급하지 않고, 인증 메일을 보낼 때 사용합니다.
    */
    public String genEmailVerificationUrl(long memberId) {
        Optional<EmailVerification> emailVerification = this.findEmailVerification(memberId);
        String code = emailVerification.map(EmailVerification::getCode).orElseGet(() -> genEmailVerificationCode(memberId));

        if (emailVerification.isPresent() && isVerificationExpired(emailVerification.get())) {
            emailVerificationRepository.delete(emailVerification.get());
            code = genEmailVerificationCode(memberId);
        }

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
        Optional<EmailVerification> emailVerification = findEmailVerification(memberId);
        return emailVerification.map(EmailVerification::getCode).orElse(null);
    }

    public Optional<EmailVerification> findEmailVerification(long memberId) {
        return emailVerificationRepository.findByMemberId(memberId);
    }

    boolean isVerificationExpired(EmailVerification emailVerification) {
        LocalDateTime expireDate = emailVerification.getExpireDate();
        return expireDate.isBefore(LocalDateTime.now());
    }
}
