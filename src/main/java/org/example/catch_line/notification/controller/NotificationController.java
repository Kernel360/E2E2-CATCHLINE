package org.example.catch_line.notification.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NotificationController {

    @GetMapping(value = "/notifications")
    public String notification() {
        return "notification/notification";
    }
}
