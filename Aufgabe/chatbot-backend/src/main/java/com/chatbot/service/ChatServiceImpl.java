package com.chatbot.service;

import com.chatbot.dto.ChatMessage;
import com.chatbot.dto.ChatRequest;
import com.chatbot.dto.ChatResponse;
import com.chatbot.dto.TraceEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    private static final String ROLE_USER = "user";
    private static final String ROLE_ASSISTANT = "assistant";
    private static final String ROLE_TOOL = "tool";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int MAX_TOOL_ITERATIONS = 5;
    private static final int MAX_DETAIL_LENGTH = 500;

    private final WebClient webClient;
    private final String apiKey;
    private final String model;
    private final McpClient mcpClient;
    private final ObjectMapper objectMapper;

    public ChatServiceImpl(
            @Value("${mistral.api-url}") String apiUrl,
            @Value("${mistral.api-key}") String apiKey,
            @Value("${mistral.model}") String model,
            McpClient mcpClient) {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .build();
        this.apiKey = apiKey;
        this.model = model;
        this.mcpClient = mcpClient;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    @SuppressWarnings("unchecked")
    public ChatResponse chat(ChatRequest request) {
        log.info("Chat-Anfrage erhalten: {} Zeichen", request.getMessage().length());

        List<TraceEntry> trace = new ArrayList<>();

        // Trace: USER_INPUT
        addTrace(trace, "USER_INPUT", "Benutzer-Nachricht empfangen",
                truncate(request.getMessage()), null);

        List<Map<String, Object>> messages = buildMessages(request);

        // Erster Request mit Tools
        Map<String, Object> requestBody = buildRequestBody(messages, true);

        addTrace(trace, "LLM_REQUEST", "Anfrage an Mistral API",
                "Model: " + model + ", Messages: " + messages.size() + ", Tools: aktiviert", null);

        Map<String, Object> responseBody = callMistralApi(requestBody);

        int iteration = 0;
        while (iteration < MAX_TOOL_ITERATIONS) {
            Map<String, Object> firstChoice = getFirstChoice(responseBody);
            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
            String finishReason = (String) firstChoice.get("finish_reason");

            List<Map<String, Object>> toolCalls = (List<Map<String, Object>>) message.get("tool_calls");
            boolean hasToolCalls = "tool_use".equals(finishReason) || toolCalls != null;

            if (!hasToolCalls) {
                break;
            }

            iteration++;
            log.info("Tool-Call-Iteration {}/{}", iteration, MAX_TOOL_ITERATIONS);

            // Trace: LLM_RESPONSE mit Tool-Call-Info
            String toolNames = extractToolNames(toolCalls);
            addTrace(trace, "LLM_RESPONSE", "Tool-Call: " + toolNames,
                    truncate(toJson(toolCalls)), null);

            // Assistant-Message (mit tool_calls) zur messages-Liste hinzufuegen
            Map<String, Object> assistantMessage = new HashMap<>();
            assistantMessage.put("role", ROLE_ASSISTANT);
            assistantMessage.put("content", message.get("content"));
            assistantMessage.put("tool_calls", toolCalls);
            messages.add(assistantMessage);

            // Jeden Tool-Call ausfuehren
            for (Map<String, Object> toolCall : toolCalls) {
                String toolCallId = (String) toolCall.get("id");
                Map<String, Object> function = (Map<String, Object>) toolCall.get("function");
                String toolName = (String) function.get("name");
                String argumentsJson = (String) function.get("arguments");

                Map<String, Object> arguments = parseArguments(argumentsJson);

                addTrace(trace, "MCP_TOOL_CALL", "Aufruf: " + toolName,
                        truncate(argumentsJson), null);

                long start = System.currentTimeMillis();
                String result = mcpClient.executeTool(toolName, arguments);
                long durationMs = System.currentTimeMillis() - start;

                addTrace(trace, "MCP_TOOL_RESULT", "Ergebnis: " + toolName,
                        truncate(result), durationMs);

                // Tool-Result-Message hinzufuegen
                Map<String, Object> toolResultMessage = new HashMap<>();
                toolResultMessage.put("role", ROLE_TOOL);
                toolResultMessage.put("name", toolName);
                toolResultMessage.put("content", result);
                toolResultMessage.put("tool_call_id", toolCallId);
                messages.add(toolResultMessage);
            }

            // Neuer Request an Mistral mit erweiterter messages-Liste
            requestBody = buildRequestBody(messages, true);
            addTrace(trace, "LLM_REQUEST", "Folge-Anfrage an Mistral API",
                    "Messages: " + messages.size(), null);

            responseBody = callMistralApi(requestBody);
        }

        // Finale Antwort extrahieren
        String content = extractContent(responseBody);

        addTrace(trace, "LLM_RESPONSE", "Finale Antwort erhalten",
                truncate(content), null);

        log.info("Antwort von Mistral API erhalten: {} Zeichen", content.length());

        return new ChatResponse(content, ROLE_ASSISTANT, trace);
    }

    private List<Map<String, Object>> buildMessages(ChatRequest request) {
        List<Map<String, Object>> messages = new ArrayList<>();

        if (request.getHistory() != null) {
            for (ChatMessage msg : request.getHistory()) {
                Map<String, Object> m = new HashMap<>();
                m.put("role", msg.getRole());
                m.put("content", msg.getContent());
                messages.add(m);
            }
        }

        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put("role", ROLE_USER);
        userMessage.put("content", request.getMessage());
        messages.add(userMessage);

        return messages;
    }

    private Map<String, Object> buildRequestBody(List<Map<String, Object>> messages, boolean includeTools) {
        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", messages);
        if (includeTools) {
            body.put("tools", mcpClient.getToolDefinitions());
            body.put("tool_choice", "auto");
        }
        return body;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> callMistralApi(Map<String, Object> requestBody) {
        log.debug("Sende Anfrage an Mistral API mit {} Nachrichten",
                ((List<?>) requestBody.get("messages")).size());

        return webClient.post()
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getFirstChoice(Map<String, Object> responseBody) {
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        return choices.get(0);
    }

    @SuppressWarnings("unchecked")
    private String extractContent(Map<String, Object> responseBody) {
        Map<String, Object> firstChoice = getFirstChoice(responseBody);
        Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
        Object content = message.get("content");
        return content != null ? content.toString() : "";
    }

    @SuppressWarnings("unchecked")
    private String extractToolNames(List<Map<String, Object>> toolCalls) {
        if (toolCalls == null) {
            return "unknown";
        }
        List<String> names = new ArrayList<>();
        for (Map<String, Object> tc : toolCalls) {
            Map<String, Object> function = (Map<String, Object>) tc.get("function");
            names.add((String) function.get("name"));
        }
        return String.join(", ", names);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseArguments(String argumentsJson) {
        if (argumentsJson == null || argumentsJson.isBlank()) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(argumentsJson, Map.class);
        } catch (JsonProcessingException e) {
            log.warn("Fehler beim Parsen der Tool-Argumente: {}", argumentsJson);
            return new HashMap<>();
        }
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return String.valueOf(obj);
        }
    }

    private void addTrace(List<TraceEntry> trace, String source, String action,
                          String detail, Long durationMs) {
        trace.add(new TraceEntry(
                Instant.now().toString(),
                source,
                action,
                detail,
                durationMs
        ));
    }

    private String truncate(String value) {
        if (value == null) {
            return null;
        }
        return value.length() > MAX_DETAIL_LENGTH
                ? value.substring(0, MAX_DETAIL_LENGTH) + "..."
                : value;
    }
}
