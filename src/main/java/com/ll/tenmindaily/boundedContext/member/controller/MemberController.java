package com.ll.tenmindaily.boundedContext.member.controller;

import com.ll.tenmindaily.base.rq.Rq;
import com.ll.tenmindaily.base.rsData.RsData;
import com.ll.tenmindaily.boundedContext.emailVerification.Entity.EmailVerification;
import com.ll.tenmindaily.boundedContext.emailVerification.service.EmailVerificationService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/usr/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    private final EmailVerificationService emailVerificationService;

    private final Rq rq;

    @AllArgsConstructor
    @Getter
    @ToString
    public static class JoinForm {
        @NotBlank
        private final String userId;
        @NotBlank
        private final String username;
        @NotBlank
        private final String password;
        @NotBlank
        private final String email;
        @NotBlank
        private final String nickname;
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin(@RequestParam(required = false) String userId, Model model) {
        model.addAttribute("userId", userId);
        return "usr/member/login";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin() {
        return "usr/member/join";
    }

    @PreAuthorize("isAnonymous()")
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
        Member actor = rq.getMember();

        model.addAttribute("username", actor.getUsername());
        model.addAttribute("nickname", actor.getNickname().isEmpty() ? "닉네임을 생성해주세요." : actor.getNickname());
        model.addAttribute("email", actor.getEmail());
        model.addAttribute("createdAt", actor.getCreatedAt());
        model.addAttribute("emailVerified", actor.getEmailVerified());

        return "usr/member/myPage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/editMyPage")
    public String showEditMyPage(Model model) {
        Member actor = memberService.getUser(rq.getMember().getUserId());

        model.addAttribute("username", actor.getUsername());
        model.addAttribute("nickname", actor.getNickname());
        model.addAttribute("email", actor.getEmail());
        model.addAttribute("emailVerified", actor.getEmailVerified());

        return "usr/member/editMyPage";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/editMyPage")
    public String editMyPage(JoinForm joinForm) {
        Member actor = rq.getMember();

        RsData<Member> modifyRsData = memberService.modify(actor, joinForm);

        return "redirect:/usr/member/myPage";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/findPassword")
    public String showFindPassword() {
        return "/usr/member/findPassword";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/findUserId")
    public String showFindUserId() {
        return "usr/member/findUserId";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/findUserId")
    public String findUserId(String email, RedirectAttributes redirectAttributes) {
        Member actor = memberService.findByEmail(email).orElse(null);

        if (actor == null) {
            return "redirect:/usr/member/findUserId";
        }

        String foundedUserId = actor.getUserId();
        String successMsg = "해당 이메일로 가입한 계정의 아이디는 '%s' 입니다.".formatted(foundedUserId);
        //TODO: 사용자에게 성공 메시지 보여주기

        return "redirect:/usr/member/login?userId=%s".formatted(foundedUserId);
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/findPassword")
    public String findPassword(String userId, String email, RedirectAttributes redirectAttributes) {
        Member member = memberService.findByUserIdAndEmail(userId, email).orElse(null);

        if (member == null) {
            String userNotFoundMsg = "일치하는 회원이 존재하지 않습니다.";
            redirectAttributes.addFlashAttribute("message", userNotFoundMsg);
            return "redirect:/usr/member/findPassword";
        }

        RsData sendTempLoginPwToEmailResultData = memberService.sendTempPasswordToEmail(member);

        if (sendTempLoginPwToEmailResultData.isFail()) {
            String errorMsg = sendTempLoginPwToEmailResultData.getMsg();
            redirectAttributes.addFlashAttribute("message", errorMsg);
            return "redirect:/usr/member/findPassword";
        }

        String successMsg = sendTempLoginPwToEmailResultData.getMsg();
        redirectAttributes.addFlashAttribute("message", successMsg);
        return "redirect:/usr/member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modifyPassword")
    public String showModifyPassword() {
        return "usr/member/modifyPassword";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modifyPassword")
    public String modifyPassword(String oldPassword, String password, RedirectAttributes redirectAttributes) {
        Member actor = rq.getMember();
        RsData modifyRsData = memberService.modifyPassword(actor, password, oldPassword);
        redirectAttributes.addFlashAttribute("message", modifyRsData.getMsg());
        if (modifyRsData.isFail()) {
            return "redirect:/usr/member/modifyPassword";
        }
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/withdrawal")
    public String withdrawal() {
        Member actor = rq.getMember();
        memberService.deleteMember(actor);
        return "redirect:/usr/member/login?logout";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/emailVerification")
    public String showEmailVerification() {
        return "/usr/member/emailVerification";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/emailVerification")
    public String emailVerification(String email) {
        Member actor = rq.getMember();
        String actorEmail = actor.getEmail();

        if (!actorEmail.equals(email)) {
            RsData.of("F-1", "기존 회원정보와 다른 이메일 주소입니다. 회원정보를 변경하거나, 기존 이메일 주소로 이메일 인증을 진행해주세요.");
            return "redirect:/usr/member/myPage";
        }

        RsData verificationRsData = memberService.sendVerificationMail(actor, email);
        return "redirect:/usr/member/myPage";
    }
}
