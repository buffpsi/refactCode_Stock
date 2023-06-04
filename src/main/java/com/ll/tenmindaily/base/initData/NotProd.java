package com.ll.tenmindaily.base.initData;

import com.ll.tenmindaily.boundedContext.member.controller.MemberController;
import com.ll.tenmindaily.boundedContext.member.entity.Member;
import com.ll.tenmindaily.boundedContext.member.repository.MemberRepository;
import com.ll.tenmindaily.boundedContext.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            MemberRepository memberRepository
    ) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                Member member = memberService.join(new MemberController.JoinForm(
                        "user1", "user1", "1234", "user1@email.com", "user1", "stocks",
                        "cryptoCurrency", null
                )).getData();
            }
        };
    }
}
