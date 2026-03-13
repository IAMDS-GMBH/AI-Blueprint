package com.chatbot.service;

import com.chatbot.dto.ChatRequest;
import com.chatbot.dto.ChatResponse;

public interface ChatService {

    ChatResponse chat(ChatRequest request);
}
