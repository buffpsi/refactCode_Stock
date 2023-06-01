package com.ll.tenmindaily.boundedContext.member.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usr/member")
public class MemberController {
    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin() {
        return "usr/member/login";
    }
}

