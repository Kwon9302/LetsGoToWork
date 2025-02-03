package com.cos.nginxkafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class MessageController {
    private final KafkaProducer kafkaProducer;

    public MessageController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam("message") String message, Model model) {
        kafkaProducer.sendMessage("test-topic", message);
        log.info("메시지 : {} ",message);
        model.addAttribute("sentMessage", message);
        return "index";
    }
}
