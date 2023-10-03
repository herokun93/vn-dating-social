package vn.dating.app.socket.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.dating.app.socket.service.KafkaService;

@Slf4j
@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private KafkaService kafkaService;

    @GetMapping("/public")
    public String getPublic(){
        kafkaService.sendMessage();
        return "public";
    }
}
