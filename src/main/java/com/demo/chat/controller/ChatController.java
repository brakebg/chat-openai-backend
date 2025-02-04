package com.demo.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.demo.chat.service.OpenAIClient;

import java.io.IOException;
import java.util.Map;

@RestController
public class ChatController {

    @Autowired
    private OpenAIClient openAIClient;

    @PostMapping("/api/chat")
    public String chat(@RequestBody Map<String, String> request) throws IOException {
        String userMessage = request.get("message");
        return openAIClient.getChatResponse(userMessage);
    }
}
