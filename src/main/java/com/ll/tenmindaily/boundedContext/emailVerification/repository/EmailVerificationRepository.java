package com.ll.tenmindaily.boundedContext.emailVerification.repository;

import com.ll.tenmindaily.boundedContext.emailVerification.Entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    public Optional<EmailVerification> findByMemberId(long memberId);
}
