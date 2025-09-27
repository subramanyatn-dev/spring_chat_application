package com.chatapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import com.chatapp.dto.ChatMessage;
import com.chatapp.service.ChatService;

@Controller
public class WebSocketController {
    
    private final ChatService chatService;
    
    @Autowired
    public WebSocketController(ChatService chatService) {
        this.chatService = chatService;
    }
    
    @MessageMapping("/chat.private")
    public void handlePrivateMessage(@Payload ChatMessage message) {
        System.out.println("Received message: " + message);
        chatService.sendPrivateMessage(message);
    }
    
    @MessageMapping("/chat.group")
    public void handleGroupMessage(@Payload ChatMessage message) {
        System.out.println("Received group message: " + message);
        chatService.sendGroupMessage(message);
    }
}