package com.ll.tenmindaily.boundedContext.member.controller;

import com.ll.tenmindaily.base.rq.Rq;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/usr/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    private final Rq rq;

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage")
    public String showMyPage(Model model) {
        Optional<Member> member = memberService.findByUserId(rq.getMember().getUserId());

        if (member.isEmpty()) {
            return "redirect:/usr/member/login";
        }

        Member actor = member.get();

        model.addAttribute("username", actor.getUsername());
        model.addAttribute("nickname", actor.getNickname().isEmpty() ? "닉네임을 생성해주세요." : actor.getNickname());
        model.addAttribute("email", actor.getEmail());
        model.addAttribute("interest1", actor.getInterest1().isEmpty() ? "관심사를 지정해주세요." : actor.getInterest1());
        model.addAttribute("interest2", actor.getInterest2().isEmpty() ? "관심사를 지정해주세요." : actor.getInterest2());
        model.addAttribute("emailVerified", actor.getEmailVerified());

        return "/usr/member/myPage";
    }
}
