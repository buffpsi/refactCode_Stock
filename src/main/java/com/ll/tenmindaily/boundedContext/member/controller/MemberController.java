package com.ll.tenmindaily.boundedContext.member.controller;

import com.ll.tenmindaily.base.rsData.RsData;
import com.ll.tenmindaily.boundedContext.member.entity.Member;
import com.ll.tenmindaily.boundedContext.member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usr/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @AllArgsConstructor
    @Getter
    @ToString
    public static class JoinForm {
        @NotBlank
        private final String id;
        @NotBlank
        private final String username;
        @NotBlank
        private final String password;
        @NotBlank
        private final String email;
        @NotBlank
        private final String nickname;
        @NotBlank
        private final String interest1;
        @NotBlank
        private final String interest2;
        private final String profileImage;
    }
    
    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin() {
        return "usr/member/login";
    }
  
    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin() {
        return "usr/member/join";
    }

    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm) {
        RsData<Member> joinRs = memberService.join(joinForm);
        if (joinRs.isFail()) {
            return "redirect:/usr/member/join";
        }
        return "redirect:/usr/member/login";
    }
}

