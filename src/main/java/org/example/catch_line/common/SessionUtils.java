package org.example.catch_line.common;

import org.example.catch_line.common.constant.Role;
import org.example.catch_line.common.constant.SessionConst;

import jakarta.servlet.http.HttpSession;
import org.example.catch_line.exception.session.InvalidSessionException;

public class SessionUtils {
	public static Long getMemberId(HttpSession session) {
		Long memberId = (Long) session.getAttribute(SessionConst.MEMBER_ID);
		if (memberId == null) {
			throw new InvalidSessionException();
		}
		return memberId;
	}

	public static Role getRole(HttpSession session) {
		return (Role) session.getAttribute(SessionConst.ROLE);
	}
}
