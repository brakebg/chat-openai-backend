package com.demo.chat.handler;

import com.demo.chat.model.ChatMessage;
import com.demo.chat.service.ChatMessageService;
import com.demo.chat.service.OpenAIClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);

    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final OpenAIClient openAIClient;
    private final ChatMessageService chatMessageService;

    @Autowired
    public ChatWebSocketHandler(OpenAIClient openAIClient, ChatMessageService chatMessageService) {
        this.openAIClient = openAIClient;
        this.chatMessageService = chatMessageService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        logger.info("New session established. Total open sessions: " + sessions.size());
    }

    private void saveChatMessageAsync(ChatMessage chatMessage) {
        chatMessageService.saveMessageAsync(chatMessage);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String payload = message.getPayload();
            String[] parts = payload.split(";", 2);
            String userMessage = parts[0];

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setMessage(userMessage);
            chatMessage.setTimestamp(new Date().toString());
            saveChatMessageAsync(chatMessage);

            String aiResponse = openAIClient.getChatResponse(userMessage);
            session.sendMessage(new TextMessage(aiResponse));

            chatMessage = new ChatMessage();
            chatMessage.setMessage(aiResponse);
            chatMessage.setTimestamp(new Date().toString());

            saveChatMessageAsync(chatMessage);

        } catch (IOException e) {
            logger.error("Error processing message: ", e);
            session.sendMessage(new TextMessage("Error processing your message from openai-api."));
        } catch (Exception e) {
            logger.error("Error processing message: ", e);
            session.sendMessage(new TextMessage("Error processing your message. Please try again later."));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }
}
