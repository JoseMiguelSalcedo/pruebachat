package com.example.chat.impl;

import com.example.chat.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class SpringAiModelService implements ModelService {

    private static final Logger log = LoggerFactory.getLogger(SpringAiModelService.class);
    private final ChatClient chatClient;

    public SpringAiModelService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public String generateResponse(String question, String sessionId) {
        log.info("Chat simple (SpringAiModelService). sessionId={}", sessionId);
        String response = chatClient.prompt()
                .user(question)
                .call()
                .content();
        return response == null ? "" : response;
    }
}