package com.demo.chat.service;

import com.demo.chat.model.ChatMessage;
import com.demo.chat.repository.ChatMessageRepository;
import com.demo.chat.thread.PersistenceThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(ChatMessage message) {
        return chatMessageRepository.save(message);
    }

    public void saveMessageAsync(ChatMessage chatMessage) {
        PersistenceThreadPoolManager.messageExecutor.submit(() -> {
            try {
                saveMessage(chatMessage);
            } catch (Exception e) {
                // Log error
                e.printStackTrace();
            }
        });
    }
}
