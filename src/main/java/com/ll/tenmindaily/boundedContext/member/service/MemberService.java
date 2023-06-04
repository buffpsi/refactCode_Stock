package com.ll.tenmindaily.boundedContext.member.service;

import com.ll.tenmindaily.base.rsData.RsData;
import com.ll.tenmindaily.boundedContext.member.controller.MemberController;
import com.ll.tenmindaily.boundedContext.member.entity.Member;
import com.ll.tenmindaily.boundedContext.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RsData<Member> join(MemberController.JoinForm joinForm) {
        String username = joinForm.getUsername();
        String id = joinForm.getId();
        String password = joinForm.getPassword();
        String email = joinForm.getEmail();
        String nickname = joinForm.getNickname();
        String interest1 = joinForm.getInterest1();
        String interest2 = joinForm.getInterest2();

        if (findByUsername(username).isPresent()) {
            return RsData.of("F-1", "해당 아이디(%s)는 이미 사용중입니다.".formatted(username));
        }

        if (StringUtils.hasText(password)) password = passwordEncoder.encode(password);

        Member member = Member
                .builder()
                .username(username)
                .userId(id)
                .password(password)
                .email(email)
                .emailVerified(false)
                .nickname(nickname)
                .interest1(interest1)
                .interest2(interest2)
                .providerType(null)
                .build();

        return RsData.of("S-1", "회원가입이 완료되었습니다.", memberRepository.save(member));
    }

    private Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }
}
