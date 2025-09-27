package com.chatapp.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.chatapp.dto.ChatMessage;
import com.chatapp.dto.MessageDto;
import com.chatapp.entity.Message;
import com.chatapp.entity.User;
import com.chatapp.repository.MessageRepository;
import com.chatapp.repository.UserRepository;
import com.chatapp.utils.DtoConverter;

@Service
public class ChatService {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final GroupService groupService;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    
    @Autowired
    public ChatService(SimpMessagingTemplate messagingTemplate,
                      MessageService messageService,
                      GroupService groupService,
                      UserRepository userRepository,
                      MessageRepository messageRepository) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
        this.groupService = groupService;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }
    
    public void sendPrivateMessage(ChatMessage chatMessage) {
        System.out.println("ChatService: Processing private message from " + chatMessage.getSenderId() + " to " + chatMessage.getReceiverId());
        System.out.println("Message content: " + chatMessage.getContent());
        System.out.println("Message type: " + chatMessage.getType());
        
        try {
            // 1. Save to database first (only for CHAT messages, not TYPING)
            if ("CHAT".equals(chatMessage.getType().toString())) {
                Message savedMessage = saveMessageToDatabase(chatMessage);
                System.out.println("💾 Message saved to database with ID: " + savedMessage.getMessageId());
            }
            
            // 2. Then send via WebSocket
            String receiverTopic = "/topic/user/" + chatMessage.getReceiverId();
            String senderTopic = "/topic/user/" + chatMessage.getSenderId();
            
            // Send to receiver
            System.out.println("Sending to receiver topic: " + receiverTopic);
            messagingTemplate.convertAndSend(receiverTopic, chatMessage);
            System.out.println("✅ Message sent to receiver");
            
            // Send confirmation to sender (so they see their message)
            System.out.println("Sending confirmation to sender topic: " + senderTopic);
            messagingTemplate.convertAndSend(senderTopic, chatMessage);
            System.out.println("✅ Confirmation sent to sender");
            
        } catch (Exception e) {
            System.err.println("❌ Error sending message: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private Message saveMessageToDatabase(ChatMessage chatMessage) {
        // Find or create users
        User sender = findOrCreateUser(chatMessage.getSenderId());
        User receiver = findOrCreateUser(chatMessage.getReceiverId());
        
        // Create message entity
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(chatMessage.getContent());
        message.setCreatedAt(LocalDateTime.now());
        message.setIsRead(false);
        
        return messageRepository.save(message);
    }
    
    private User findOrCreateUser(String username) {
        return userRepository.findByUsername(username)
            .orElseGet(() -> {
                User newUser = new User(); // Constructor will set default profile photo
                newUser.setUsername(username);
                newUser.setDisplayName(username);
                newUser.setPasswordHash("temp_hash_" + System.currentTimeMillis()); // Temporary password hash for auto-created users
                newUser.setCreatedAt(LocalDateTime.now());
                // profilePhoto is already set to default by constructor
                System.out.println("👤 Created new user: " + username);
                return userRepository.save(newUser);
            });
    }
    
    public void sendGroupMessage(ChatMessage message) {
        // Save to database
        Message savedMessage = messageService.sendGroupMessage(
            message.getSenderId(),
            message.getGroupId(),
            message.getContent()
        );
        
        MessageDto messageDto = DtoConverter.convertToDto(savedMessage);
        
        // Broadcast to group
        messagingTemplate.convertAndSend(
            "/topic/group/" + message.getGroupId(),
            messageDto
        );
    }
    
    public void notifyUserJoined(String userId, String groupId) {
        ChatMessage joinMessage = new ChatMessage();
        joinMessage.setType(ChatMessage.MessageType.JOIN);
        joinMessage.setSenderId(userId);
        joinMessage.setGroupId(groupId);
        
        messagingTemplate.convertAndSend(
            "/topic/group/" + groupId,
            joinMessage
        );
    }
    
    public void notifyUserLeft(String userId, String groupId) {
        ChatMessage leaveMessage = new ChatMessage();
        leaveMessage.setType(ChatMessage.MessageType.LEAVE);
        leaveMessage.setSenderId(userId);
        leaveMessage.setGroupId(groupId);
        
        messagingTemplate.convertAndSend(
            "/topic/group/" + groupId,
            leaveMessage
        );
    }
    
    public void notifyTyping(String senderId, String receiverId) {
        ChatMessage typingMessage = new ChatMessage();
        typingMessage.setType(ChatMessage.MessageType.TYPING);
        typingMessage.setSenderId(senderId);
        typingMessage.setReceiverId(receiverId);
        
        messagingTemplate.convertAndSendToUser(
            receiverId,
            "/queue/typing",
            typingMessage
        );
    }
}