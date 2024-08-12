package org.example.catch_line.member.controller.thymeleaf;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.common.SessionUtils;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.member.model.dto.MemberResponse;
import org.example.catch_line.member.model.dto.MemberUpdateRequest;
import org.example.catch_line.member.service.MemberService;
import org.example.catch_line.member.validation.MemberValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    private final MemberValidator memberValidator;

    @GetMapping
    public String findMember(HttpSession httpSession, Model model) {
        MemberResponse memberResponse = memberService.findMember(SessionUtils.getMemberId(httpSession));
        model.addAttribute("member", memberResponse);
        return "member/memberDetail";  // 타임리프 템플릿 파일 이름
    }

    @GetMapping("/update")
    public String showUpdateMemberForm(
            HttpSession httpSession,
            Model model) {
        model.addAttribute("memberUpdateRequest", new MemberUpdateRequest("", "", "", ""));
        MemberResponse memberResponse = memberService.findMember(SessionUtils.getMemberId(httpSession));
        model.addAttribute("member", memberResponse);
        return "member/memberUpdate";
    }

    @PostMapping("/update")
    public String updateMember(
            @Valid @ModelAttribute MemberUpdateRequest memberUpdateRequest,
            HttpSession httpSession,
            BindingResult bindingResult,
            Model model
    ) {
        if(bindingResult.hasErrors()) {
            log.info("회원수정 에러");
            return "member/memberUpdate";
        }
        try {
            memberService.updateMember(memberUpdateRequest, SessionUtils.getMemberId(httpSession));
        } catch (CatchLineException e) {
            log.error("회원수정 중 예외 발생", e);
            model.addAttribute("exception", e.getMessage());
            return "member/memberUpdate";

        }
        return "redirect:/members";
    }

    @PostMapping("/delete")
    public String deleteMember(HttpSession httpSession) {
        memberService.deleteMember(SessionUtils.getMemberId(httpSession));
        httpSession.invalidate();
        return "redirect:/";
    }
}
