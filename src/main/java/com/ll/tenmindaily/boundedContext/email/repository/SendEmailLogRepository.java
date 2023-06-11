package com.ll.tenmindaily.boundedContext.email.repository;

import com.ll.tenmindaily.boundedContext.email.entity.SendEmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendEmailLogRepository extends JpaRepository<SendEmailLog, Long> {
}
