package com.ll.tenmindaily.boundedContext.email.service;

import com.ll.tenmindaily.base.rsData.RsData;
import com.ll.tenmindaily.boundedContext.email.entity.SendEmailLog;
import com.ll.tenmindaily.boundedContext.email.repository.SendEmailLogRepository;
import com.ll.tenmindaily.boundedContext.emailSender.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {
    private final SendEmailLogRepository emailLogRepository;
    private final EmailSenderService emailSenderService;

    @Transactional
    public RsData sendEmail(String email, String subject, String body) {
        SendEmailLog sendEmailLog = SendEmailLog
                .builder()
                .email(email)
                .subject(subject)
                .body(body)
                .build();

        emailLogRepository.save(sendEmailLog);

        RsData trySendRs = trySend(email, subject, body);

        setCompleted(sendEmailLog, trySendRs.getResultCode(), trySendRs.getMsg());

        return RsData.of("S-1", "메일이 발송되었습니다.", sendEmailLog.getId());
    }

    private RsData trySend(String email, String title, String body) {
        // 쓸 데 없는 메일 발송을 막기 위해 테스트용 이메일 주소를 삽입해놨습니다.
        // 실제 발송 테스트를 원하시면 아래 if 문을 삭제하거나 주석 처리하여 테스트를 진행해주세요.
        if (!email.equals("test@test.com")) {
            return RsData.of("S-0", "메일이 발송되었습니다.");
        }
        try {
            emailSenderService.send(email, "no-reply@no-reply.com", title, body);

            return RsData.of("S-1", "메일이 발송되었습니다.");
        } catch (MailException e) {
            return RsData.of("F-1", e.getMessage());
        }
    }

    @Transactional
    public void setCompleted(SendEmailLog sendEmailLog, String resultCode, String message) {
        if (resultCode.startsWith("S-")) {
            sendEmailLog.setSendEndDate(LocalDateTime.now());
        } else {
            sendEmailLog.setFailDate(LocalDateTime.now());
        }

        sendEmailLog.setResultCode(resultCode);
        sendEmailLog.setMessage(message);

        emailLogRepository.save(sendEmailLog);
    }
}
