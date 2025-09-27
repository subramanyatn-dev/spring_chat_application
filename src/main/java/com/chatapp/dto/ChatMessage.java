package com.chatapp.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private MessageType type;
    private String content;
    private String senderId;
    private String receiverId;
    private String groupId;
    private LocalDateTime timestamp = LocalDateTime.now();
    
    public enum MessageType {
        CHAT,       // Regular chat message
        JOIN,       // User joined
        LEAVE,      // User left
        TYPING,     // User is typing
        DELIVERED,  // Message delivered
        READ        // Message read
    }
}