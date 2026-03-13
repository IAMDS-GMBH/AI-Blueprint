package com.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraceEntry {

    private String timestamp;
    private String source;
    private String action;
    private String detail;
    private Long durationMs;
}
