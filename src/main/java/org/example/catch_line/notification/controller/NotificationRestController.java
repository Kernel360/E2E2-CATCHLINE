package org.example.catch_line.notification.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.notification.service.NotificationService;
import org.example.catch_line.user.auth.details.MemberUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NotificationRestController {

    private final NotificationService notificationService;

    @GetMapping(value = "/notifications/sse", produces = "text/event-stream")
    public SseEmitter subscribe(@AuthenticationPrincipal MemberUserDetails userDetails,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {

        log.info("SSE 연결");
        Long memberId = userDetails.getMember().getMemberId();
        return notificationService.connect(memberId, lastEventId);
    }
}
