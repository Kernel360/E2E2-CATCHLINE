package org.example.catch_line.user.member.controller.thymeleaf;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.model.provider.validation.MemberValidator;
import org.example.catch_line.user.token.JwtTokenUtil;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class OAuthController {
    private final MemberValidator memberValidator;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping("/login/oauth")
    public String kakaoLogin() {
        return "redirect:/oauth2/authorization/kakao";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(@AuthenticationPrincipal OAuth2User oAuth2User, HttpSession session, HttpServletResponse response) {

        if(oAuth2User == null) {
            throw new CatchLineException("Oauth2 user is null");
        }

        // 카카오 로그인 성공 후 세션 설정
        Long kakaoMemberId = (Long) oAuth2User.getAttributes().get("id");
        MemberEntity member = memberValidator.checkIfKakaoMemberPresent(kakaoMemberId);

        // JWT 토큰 생성
        String jwtToken = jwtTokenUtil.generateToken(member.getNickname());

        // 응답 헤더에 JWT 토큰 추가
        response.setHeader("Authorization", "Bearer " + jwtToken);

        return "redirect:/"; // 로그인 후 리다이렉션할 페이지
    }

}
