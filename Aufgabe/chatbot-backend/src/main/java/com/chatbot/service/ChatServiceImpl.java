package com.chatbot.service;

import com.chatbot.dto.ChatMessage;
import com.chatbot.dto.ChatRequest;
import com.chatbot.dto.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    private static final String ROLE_USER = "user";
    private static final String ROLE_ASSISTANT = "assistant";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final WebClient webClient;
    private final String apiKey;
    private final String model;

    public ChatServiceImpl(
            @Value("${mistral.api-url}") String apiUrl,
            @Value("${mistral.api-key}") String apiKey,
            @Value("${mistral.model}") String model) {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .build();
        this.apiKey = apiKey;
        this.model = model;
    }

    @Override
    public ChatResponse chat(ChatRequest request) {
        log.info("Chat-Anfrage erhalten: {} Zeichen", request.getMessage().length());

        List<Map<String, String>> messages = buildMessages(request);

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", messages
        );

        log.debug("Sende Anfrage an Mistral API mit {} Nachrichten", messages.size());

        Map<String, Object> responseBody = webClient.post()
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        String content = extractContent(responseBody);

        log.info("Antwort von Mistral API erhalten: {} Zeichen", content.length());

        return new ChatResponse(content, ROLE_ASSISTANT);
    }

    private List<Map<String, String>> buildMessages(ChatRequest request) {
        List<Map<String, String>> messages = new ArrayList<>();

        if (request.getHistory() != null) {
            for (ChatMessage msg : request.getHistory()) {
                messages.add(Map.of(
                        "role", msg.getRole(),
                        "content", msg.getContent()
                ));
            }
        }

        messages.add(Map.of(
                "role", ROLE_USER,
                "content", request.getMessage()
        ));

        return messages;
    }

    @SuppressWarnings("unchecked")
    private String extractContent(Map<String, Object> responseBody) {
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        Map<String, Object> firstChoice = choices.get(0);
        Map<String, String> message = (Map<String, String>) firstChoice.get("message");
        return message.get("content");
    }
}
