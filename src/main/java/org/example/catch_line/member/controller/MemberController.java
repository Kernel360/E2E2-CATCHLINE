package org.example.catch_line.member.controller;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.member.model.dto.MemberResponse;
import org.example.catch_line.member.model.dto.MemberUpdateRequest;
import org.example.catch_line.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.example.catch_line.common.constant.SessionConst.MEMBER_ID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;


    @GetMapping("")
    public ResponseEntity<MemberResponse> findMember(
            HttpSession httpSession
    ) {
        MemberResponse memberResponse = memberService.findMember((Long) httpSession.getAttribute(MEMBER_ID));
        return ResponseEntity.ok().body(memberResponse);
    }

    @PutMapping("")
    public ResponseEntity<MemberResponse> updateMember(
            @Valid
            @RequestBody MemberUpdateRequest memberUpdateRequest,
            HttpSession httpSession
    ) {

        MemberResponse memberResponse = memberService.updateMember(memberUpdateRequest, (Long) httpSession.getAttribute(MEMBER_ID));
        return ResponseEntity.ok().body(memberResponse);

    }

    @PatchMapping("")
    public ResponseEntity<MemberResponse> deleteMember(
            HttpSession httpSession
    ) {
        MemberResponse memberResponse = memberService.deleteMember((Long) httpSession.getAttribute(MEMBER_ID));
        return ResponseEntity.ok().body(memberResponse);
    }


}
