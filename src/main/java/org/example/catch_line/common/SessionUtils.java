package org.example.catch_line.common;

import org.example.catch_line.common.constant.SessionConst;

import jakarta.servlet.http.HttpSession;

public class SessionUtils {
	public static Long getMemberId(HttpSession session) {
		Long memberId = (Long) session.getAttribute(SessionConst.MEMBER_ID);
		if (memberId == null) {
			throw new RuntimeException("로그인이 필요합니다");
		}
		return memberId;
	}
}
