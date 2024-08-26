package org.example.catch_line.user.member.controller.thymeleaf;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.common.cookie.CookieUtils;
import org.example.catch_line.config.auth.MemberUserDetails;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.user.member.model.dto.MemberResponse;
import org.example.catch_line.user.member.model.dto.MemberUpdateRequest;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.model.mapper.MemberResponseMapper;
import org.example.catch_line.user.member.model.provider.MemberDataProvider;
import org.example.catch_line.user.member.service.MemberService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final MemberResponseMapper memberResponseMapper;

    @GetMapping
    public String findMember(@AuthenticationPrincipal MemberUserDetails memberUserDetails, Model model) {
        MemberEntity member = memberUserDetails.getMember();
        MemberResponse memberResponse = memberResponseMapper.entityToResponse(member);
        model.addAttribute("member", memberResponse);
        return "member/memberDetail";
    }

    @GetMapping("/update")
    public String showUpdateMemberForm(@AuthenticationPrincipal MemberUserDetails memberUserDetails, Model model) {

        MemberEntity member = memberUserDetails.getMember();
        model.addAttribute("memberUpdateRequest", new MemberUpdateRequest(null, null, null, null));
        MemberResponse memberResponse = memberResponseMapper.entityToResponse(member);
        model.addAttribute("member", memberResponse);
        return "member/memberUpdate";
    }

    @PostMapping("/update")
    public String updateMember(
            @AuthenticationPrincipal MemberUserDetails memberUserDetails,
            @Valid @ModelAttribute MemberUpdateRequest memberUpdateRequest,
            BindingResult bindingResult, Model model) {


        if (bindingResult.hasErrors()) {
            log.info("error : {}", bindingResult);
            MemberResponse memberResponse = memberResponseMapper.entityToResponse(memberUserDetails.getMember());
            model.addAttribute("member", memberResponse);

            model.addAttribute("bindingResult", bindingResult);
            return "member/memberUpdate";
        }

        try {
            memberService.updateMember(memberUpdateRequest, memberUserDetails.getMember().getMemberId());
        } catch (CatchLineException e) {
            log.info("error : {}", e.getMessage());

            MemberResponse memberResponse = memberService.findMember(memberUserDetails.getMember().getMemberId());
            model.addAttribute("member", memberResponse);

            model.addAttribute("exception", e.getMessage());
            return "member/memberUpdate";

        }
        return "redirect:/members";
    }

    @PostMapping("/delete")
    public String deleteMember(@AuthenticationPrincipal MemberUserDetails memberUserDetails) {
        memberService.deleteMember(memberUserDetails.getMember().getMemberId());
        return "redirect:/logout";
    }
}
