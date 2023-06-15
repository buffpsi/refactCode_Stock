package com.ll.tenmindaily.boundedContext.emailVerification.controller;

import com.ll.tenmindaily.base.rq.Rq;
import com.ll.tenmindaily.base.rsData.RsData;
import com.ll.tenmindaily.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/emailVerification")
public class EmailVerificationController {
    private final MemberService memberService;
    private final Rq rq;

    @GetMapping("/verify")
    public String verify(long memberId, String code, RedirectAttributes redirectAttributes) {
        RsData verifyEmailRsData = memberService.verifyEmail(memberId, code);


        if (verifyEmailRsData.isFail()) {
            String errorMessage = verifyEmailRsData.getMsg();
            redirectAttributes.addFlashAttribute("message", errorMessage);
            return "redirect:/";
        }

        String successMsg = verifyEmailRsData.getMsg();
        redirectAttributes.addFlashAttribute("message", successMsg);

        if (rq.isLogout()) {
            return "redirect:/usr/member/login";
        }

        return "redirect:/";
    }
}
