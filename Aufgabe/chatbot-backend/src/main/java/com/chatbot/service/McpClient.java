package com.chatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Map;

@Slf4j
@Service
public class McpClient {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int TIMEOUT_SECONDS = 10;

    private final WebClient webClient;
    private final String apiKey;

    public McpClient(
            @Value("${mcp.server-url}") String serverUrl,
            @Value("${mcp.api-key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl(serverUrl)
                .build();
        this.apiKey = apiKey;
    }

    public String executeTool(String toolName, Map<String, Object> arguments) {
        log.info("MCP Tool-Aufruf: {} mit Argumenten: {}", toolName, arguments);

        try {
            String result = webClient.post()
                    .uri("/api/tools/{toolName}", toolName)
                    .header(AUTHORIZATION_HEADER, BEARER_PREFIX + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of("arguments", arguments))
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                    .block();

            log.debug("MCP Tool-Ergebnis fuer {}: {}", toolName, result);
            return result;
        } catch (Exception e) {
            log.error("MCP Tool-Aufruf fehlgeschlagen fuer {}: {}", toolName, e.getMessage());
            throw new RuntimeException("MCP Tool-Aufruf fehlgeschlagen: " + toolName, e);
        }
    }
}
