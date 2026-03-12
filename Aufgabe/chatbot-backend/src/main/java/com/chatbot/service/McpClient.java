package com.chatbot.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class McpClient {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int TIMEOUT_SECONDS = 10;

    private final WebClient webClient;
    private final String apiKey;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<Map<String, Object>> cachedTools;

    public McpClient(
            @Value("${mcp.server-url}") String serverUrl,
            @Value("${mcp.api-key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl(serverUrl)
                .build();
        this.apiKey = apiKey;
    }

    @PostConstruct
    public void init() {
        loadTools();
    }

    /**
     * Laedt Tool-Definitionen vom MCP-Server und konvertiert sie ins Mistral Function Calling Format.
     */
    public void loadTools() {
        log.info("Lade Tool-Definitionen vom MCP-Server...");

        try {
            String response = webClient.get()
                    .uri("/api/tools")
                    .header(AUTHORIZATION_HEADER, BEARER_PREFIX + apiKey)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                    .block();

            List<Map<String, Object>> mcpTools = objectMapper.readValue(
                    response, new TypeReference<>() {});

            this.cachedTools = convertToMistralFormat(mcpTools);
            log.info("{} Tools vom MCP-Server geladen: {}", cachedTools.size(),
                    cachedTools.stream()
                            .map(t -> ((Map<?, ?>) t.get("function")).get("name").toString())
                            .toList());
        } catch (Exception e) {
            log.error("Fehler beim Laden der MCP-Tools: {}", e.getMessage());
            this.cachedTools = List.of();
        }
    }

    public List<Map<String, Object>> getToolDefinitions() {
        return cachedTools;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> convertToMistralFormat(List<Map<String, Object>> mcpTools) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (Map<String, Object> mcpTool : mcpTools) {
            String name = (String) mcpTool.get("name");
            String description = (String) mcpTool.get("description");
            Map<String, Object> mcpParams = (Map<String, Object>) mcpTool.getOrDefault("parameters", Map.of());

            // MCP-Format → OpenAI/Mistral JSON Schema Format
            Map<String, Object> properties = new HashMap<>();
            List<String> required = new ArrayList<>();

            for (Map.Entry<String, Object> entry : mcpParams.entrySet()) {
                String paramName = entry.getKey();
                Map<String, Object> paramDef = (Map<String, Object>) entry.getValue();

                Map<String, Object> property = new HashMap<>();
                property.put("type", mapType((String) paramDef.get("type")));
                property.put("description", paramDef.get("description"));

                if ("array".equals(paramDef.get("type"))) {
                    property.put("items", Map.of("type", "string"));
                }

                properties.put(paramName, property);

                if (Boolean.TRUE.equals(paramDef.get("required"))) {
                    required.add(paramName);
                }
            }

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("type", "object");
            parameters.put("properties", properties);
            if (!required.isEmpty()) {
                parameters.put("required", required);
            }

            Map<String, Object> function = new HashMap<>();
            function.put("name", name);
            function.put("description", description);
            function.put("parameters", parameters);

            Map<String, Object> tool = new HashMap<>();
            tool.put("type", "function");
            tool.put("function", function);

            result.add(tool);
        }

        return result;
    }

    private String mapType(String mcpType) {
        if (mcpType == null) {
            return "string";
        }
        return switch (mcpType) {
            case "number" -> "integer";
            default -> mcpType;
        };
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
