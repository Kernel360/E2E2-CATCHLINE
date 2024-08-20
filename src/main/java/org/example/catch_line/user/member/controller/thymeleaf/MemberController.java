package org.example.catch_line.user.member.controller.thymeleaf;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.common.session.SessionUtils;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.user.member.model.dto.MemberResponse;
import org.example.catch_line.user.member.model.dto.MemberUpdateRequest;
import org.example.catch_line.user.member.service.MemberService;
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

    @GetMapping
    public String findMember(HttpSession httpSession, Model model) {
        MemberResponse memberResponse = memberService.findMember(SessionUtils.getMemberId(httpSession));
        model.addAttribute("member", memberResponse);
        return "member/memberDetail";
    }

    @GetMapping("/update")
    public String showUpdateMemberForm(HttpSession httpSession, Model model) {
        model.addAttribute("memberUpdateRequest", new MemberUpdateRequest(null, null, null,  null));
        MemberResponse memberResponse = memberService.findMember(SessionUtils.getMemberId(httpSession));
        model.addAttribute("member", memberResponse);
        return "member/memberUpdate";
    }

    @PostMapping("/update")
    public String updateMember(
            @Valid @ModelAttribute MemberUpdateRequest memberUpdateRequest,
            BindingResult bindingResult, HttpSession httpSession, Model model) {

        if(bindingResult.hasErrors()) {
            log.info("error : {}", bindingResult);
            MemberResponse memberResponse = memberService.findMember(SessionUtils.getMemberId(httpSession));
            model.addAttribute("member", memberResponse);

            model.addAttribute("bindingResult", bindingResult);
            return "member/memberUpdate";
        }

        try {
            memberService.updateMember(memberUpdateRequest, SessionUtils.getMemberId(httpSession));
        } catch (CatchLineException e) {
            log.info("error : {}", e.getMessage());

            // 현재 세션에서 memberId를 가져와서 다시 조회
            MemberResponse memberResponse = memberService.findMember(SessionUtils.getMemberId(httpSession));
            model.addAttribute("member", memberResponse);

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
