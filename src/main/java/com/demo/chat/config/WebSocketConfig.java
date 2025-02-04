package com.demo.chat.config;

import com.demo.chat.service.ChatMessageService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.demo.chat.service.OpenAIClient;
import com.demo.chat.handler.ChatWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {


    private final OpenAIClient openAIClient;
    private final ChatMessageService chatMessageService;

    public WebSocketConfig(OpenAIClient openAIClient, ChatMessageService chatMessageService) {
        this.openAIClient = openAIClient;
        this.chatMessageService = chatMessageService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatWebSocketHandler(openAIClient, chatMessageService), "/chat");
    }
}
