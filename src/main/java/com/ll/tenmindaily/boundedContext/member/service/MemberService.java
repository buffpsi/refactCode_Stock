package com.ll.tenmindaily.boundedContext.member.service;

import com.ll.tenmindaily.base.exception.DataNotFoundException;
import com.ll.tenmindaily.base.rsData.RsData;
import com.ll.tenmindaily.boundedContext.member.controller.MemberController;
import com.ll.tenmindaily.boundedContext.member.entity.Member;
import com.ll.tenmindaily.boundedContext.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;
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

        if (findByUserId(username).isPresent()) {
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

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public Optional<Member> findByUserId(String userId) {
        return memberRepository.findByUserId(userId);
    }

    public Member getUser(String userId) {
        Optional<Member> member = findByUserId(userId);
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }

    @Transactional
    public RsData<Member> whenSocialLogin(OAuth2User oAuth2User, String userId, String providerTypeCode) {
        Optional<Member> opMember = findByUserId(userId);

        if (opMember.isPresent()) return RsData.of("S-2", "로그인 되었습니다.", opMember.get());

        String username = null;
        String email = null;
        String profileImage = null;

        switch (providerTypeCode) {
            case "NAVER" -> {
                username = ((Map<String, String>) oAuth2User.getAttribute("response")).get("name");
                email = ((Map<String, String>) oAuth2User.getAttribute("response")).get("email");
                profileImage = ((Map<String, String>) oAuth2User.getAttribute("response")).get("profile_image");
            }
            case "KAKAO" -> {
                username = ((Map<String, String>) oAuth2User.getAttribute("properties")).get("nickname");
                email = ((Map<String, String>) oAuth2User.getAttribute("kakao_account")).get("email");

                if (!((Map<String, Map<String, Object>>) oAuth2User.getAttribute("kakao_account")).get("profile").get("is_default_image").equals("true")) {
                    profileImage = ((Map<String, String>) oAuth2User.getAttribute("properties")).get("profile_image");
                }
            }
            case "GOOGLE" -> {
                username = oAuth2User.getAttribute("name");
                email = oAuth2User.getAttribute("email");
                profileImage = oAuth2User.getAttribute("picture");
            }
        }

        MemberController.JoinForm joinForm = new MemberController.JoinForm(userId, username, "", email, "", "", "", profileImage);

        return join(joinForm);
    }

    @Transactional
    public RsData<Member> modify(Member actor, MemberController.JoinForm joinForm) {
        actor.setUsername(joinForm.getUsername());
        actor.setNickname(joinForm.getNickname());
        actor.setEmail(joinForm.getEmail());

        return RsData.of("S-1","성공적으로 수정되었습니다.", memberRepository.save(actor));
    }

}
