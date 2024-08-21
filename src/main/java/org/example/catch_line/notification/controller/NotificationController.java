package org.example.catch_line.notification.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.catch_line.notification.service.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    @GetMapping(value = "/test")
    public String test() {
        return "reservation/test";
    }
}
