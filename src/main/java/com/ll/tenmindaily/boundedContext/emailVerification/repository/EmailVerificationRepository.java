package com.ll.tenmindaily.boundedContext.emailVerification.repository;

import com.ll.tenmindaily.boundedContext.emailVerification.Entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    public EmailVerification findByMemberId(long memberId);
}
