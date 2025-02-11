package com.cos.nginxkafka;

import com.cos.nginxkafka.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private ChatService chatService;

    @GetMapping("/")
    public String index() {
        
        return "index";
    }

}
