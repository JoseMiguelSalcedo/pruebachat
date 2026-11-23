package com.example.chat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ModelService modelService;
    private final String ollamaBaseUrl;
    private final String configuredModel;
    private final RestClient restClient;

    public ChatController(
            ModelService modelService,
            @Value("${spring.ai.ollama.base-url:http://localhost:11434}") String ollamaBaseUrl,
            @Value("${spring.ai.ollama.chat.model:llama3}") String configuredModel) {
        this.modelService = modelService;
        this.ollamaBaseUrl = ollamaBaseUrl;
        this.configuredModel = configuredModel;
        this.restClient = RestClient.create();
    }

    public record ChatRequest(String question, String message, String sessionId) {
        public String resolvedQuestion() {
            if (question != null && !question.isBlank()) return question;
            return message == null ? "" : message;
        }
    }

    public record ChatResponse(String answer, String sessionId, long elapsedMs) {}

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        String question = request.resolvedQuestion().trim();
        if (question.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ChatResponse("La pregunta no puede estar vacia.", "", 0));
        }
        String sessionId = (request.sessionId() == null || request.sessionId().isBlank())
                ? UUID.randomUUID().toString()
                : request.sessionId();

        long start = System.currentTimeMillis();
        String answer = modelService.generateResponse(question, sessionId);
        long elapsedMs = System.currentTimeMillis() - start;

        return ResponseEntity.ok(new ChatResponse(answer, sessionId, elapsedMs));
    }

    @GetMapping("/status")
    public ResponseEntity> status() {
        boolean active = false;
        try {
            restClient.get()
                    .uri(ollamaBaseUrl + "/api/tags")
                    .retrieve()
                    .toBodilessEntity();
            active = true;
        } catch (Exception ignored) {
            active = false;
        }
        return ResponseEntity.ok(Map.of(
                "active", active,
                "status", active ? "ollama activo" : "ollama inactivo",
                "model", configuredModel,
                "service", modelService.getClass().getSimpleName()
        ));
    }
}