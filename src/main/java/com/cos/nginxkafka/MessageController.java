package com.cos.nginxkafka;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MessageController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/disconnected")
    public String disconnected() {
        return "disconnected";
    }

    @GetMapping("/search")
    public String searchPage() {
        return "search";
    }
}
