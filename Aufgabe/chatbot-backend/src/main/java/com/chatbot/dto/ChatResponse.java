package com.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    private String response;
    private String role;
    private List<TraceEntry> trace;

    public ChatResponse(String response, String role) {
        this.response = response;
        this.role = role;
    }
}
