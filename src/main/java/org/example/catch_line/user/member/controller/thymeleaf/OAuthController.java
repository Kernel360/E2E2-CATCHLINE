package org.example.catch_line.user.member.controller.thymeleaf;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.catch_line.common.constant.Role;
import org.example.catch_line.common.session.SessionConst;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.validation.MemberValidator;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class OAuthController {
    private final MemberValidator memberValidator;

    @GetMapping("/login/kakao")
    public String kakaoLogin() {
        return "redirect:/oauth2/authorization/kakao";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(@AuthenticationPrincipal OAuth2User oAuth2User, HttpSession session) {
        // 카카오 로그인 성공 후 세션 설정
        session.setAttribute(SessionConst.ROLE, Role.USER);

        Long kakaoMemberId = (Long) oAuth2User.getAttributes().get("id");
        MemberEntity member = memberValidator.checkIfKakaoMemberPresent(kakaoMemberId);
        session.setAttribute(SessionConst.MEMBER_ID, member.getMemberId());

        return "redirect:/"; // 로그인 후 리다이렉션할 페이지
    }

}
