package com.ll.tenmindaily.base.rq;

import com.ll.tenmindaily.base.rsData.RsData;
import com.ll.tenmindaily.boundedContext.member.entity.Member;
import com.ll.tenmindaily.boundedContext.member.service.MemberService;
import com.ll.tenmindaily.standard.util.Ut;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Date;

@Component
@RequestScope
public class Rq {

    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final MemberService memberService;
    private final User user;
    private Member member = null;

    public Rq(HttpServletRequest req, HttpServletResponse resp, MemberService memberService) {
        this.req = req;
        this.resp = resp;
        this.memberService = memberService;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof User) {
            this.user = (User) authentication.getPrincipal();
        } else {
            this.user = null;
        }
    }

    public boolean isLogin() {
        return user != null;
    }

    public boolean isLogout() {
        return !isLogin();
    }

    public Member getMember() {
        if (isLogout()) return null;

        // 데이터가 없는지 체크
        if (member == null) {
            member = memberService.findByUserId(user.getUsername()).orElseThrow();
        }

        return member;
    }

    // 뒤로가기 + 메세지
    public String historyBack(String msg) {
        String referer = req.getHeader("referer");
        String key = "historyBackErrorMsg___" + referer;
        req.setAttribute("localStorageKeyAboutHistoryBackErrorMsg", key);
        req.setAttribute("historyBackErrorMsg", msg);
        // 200 이 아니라 400 으로 응답코드가 지정되도록
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "common/js";
    }

    // 뒤로가기 + 메세지
    public String historyBack(RsData rsData) {
        return historyBack(rsData.getMsg());
    }

    // 302 + 메세지
    public String redirectWithMsg(String url, RsData rsData) {
        return redirectWithMsg(url, rsData.getMsg());
    }

    // 302 + 메세지
    public String redirectWithMsg(String url, String msg) {
        return "redirect:" + urlWithMsg(url, msg);
    }


    private String urlWithMsg(String url, String msg) {
        // 기존 URL에 혹시 msg 파라미터가 있다면 그것을 지우고 새로 넣는다.
        return Ut.url.modifyQueryParam(url, "msg", msgWithTtl(msg));
    }

    private String msgWithTtl(String msg) {
        return Ut.url.encode(msg) + ";ttl=" + new Date().getTime();
    }


}