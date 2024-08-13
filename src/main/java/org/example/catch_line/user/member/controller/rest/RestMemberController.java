package org.example.catch_line.user.member.controller.rest;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.common.SessionUtils;
import org.example.catch_line.user.member.model.dto.MemberResponse;
import org.example.catch_line.user.member.model.dto.MemberUpdateRequest;
import org.example.catch_line.user.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class RestMemberController {

    private final MemberService memberService;


    @GetMapping
    public ResponseEntity<MemberResponse> findMember(
            HttpSession httpSession
    ) {
        MemberResponse memberResponse = memberService.findMember(SessionUtils.getMemberId(httpSession));
        return ResponseEntity.ok().body(memberResponse);
    }

    @PutMapping
    public ResponseEntity<MemberResponse> updateMember(
            @Valid
            @RequestBody MemberUpdateRequest memberUpdateRequest,
            HttpSession httpSession
    ) {

        MemberResponse memberResponse = memberService.updateMember(memberUpdateRequest, SessionUtils.getMemberId(httpSession));
        return ResponseEntity.ok().body(memberResponse);

    }

    @PatchMapping
    public ResponseEntity<MemberResponse> deleteMember(
            HttpSession httpSession
    ) {
        MemberResponse memberResponse = memberService.deleteMember(SessionUtils.getMemberId(httpSession));
        httpSession.invalidate();
        return ResponseEntity.ok().body(memberResponse);
    }


}
