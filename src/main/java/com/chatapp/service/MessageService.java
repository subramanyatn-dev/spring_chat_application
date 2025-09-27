package com.chatapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chatapp.entity.Group;
import com.chatapp.entity.Message;
import com.chatapp.entity.User;
import com.chatapp.repository.GroupRepository;
import com.chatapp.repository.MessageRepository;
import com.chatapp.repository.UserRepository;

@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, 
                        UserRepository userRepository,
                        GroupRepository groupRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    /**
     * Send a direct message to another user
     */
    public Message sendDirectMessage(String senderId, String receiverId, String content) {
        User sender = userRepository.findById(Integer.parseInt(senderId))
            .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(Integer.parseInt(receiverId))
            .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setIsRead(false);

        return messageRepository.save(message);
    }

    /**
     * Send a message to a group
     */
    public Message sendGroupMessage(String senderId, String groupId, String content) {
        User sender = userRepository.findById(Integer.parseInt(senderId))
            .orElseThrow(() -> new RuntimeException("Sender not found"));
        Group group = groupRepository.findById(Integer.parseInt(groupId))
            .orElseThrow(() -> new RuntimeException("Group not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setGroup(group);
        message.setContent(content);
        message.setIsRead(false);

        return messageRepository.save(message);
    }

    /**
     * Get all messages for a user (both sent and received) with pagination
     */
    public Page<Message> getMessagesByUser(Integer userId, Pageable pageable) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        // Get direct messages where user is sender or receiver
        return messageRepository.findLatestMessagesForUser(user, pageable);
    }

    /**
     * Get direct messages between two users with pagination
     */
    public Page<Message> getDirectMessagesBetweenUsers(Integer user1Id, Integer user2Id, Pageable pageable) {
        User user1 = userRepository.findById(user1Id)
            .orElseThrow(() -> new RuntimeException("User 1 not found"));
        User user2 = userRepository.findById(user2Id)
            .orElseThrow(() -> new RuntimeException("User 2 not found"));

        return messageRepository.findBySenderAndReceiver(user1, user2, pageable);
    }

    /**
     * Mark a message as read
     */
    public Message markMessageAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setIsRead(true);
        return messageRepository.save(message);
    }
    
    /**
     * Get chat history between two users by username
     */
    public List<Message> getChatHistory(String user1, String user2) {
        User sender = userRepository.findByUsername(user1)
            .orElseThrow(() -> new RuntimeException("User not found: " + user1));
        User receiver = userRepository.findByUsername(user2)
            .orElseThrow(() -> new RuntimeException("User not found: " + user2));
            
        // Get messages in both directions and sort by timestamp
        return messageRepository.findMessagesBewtweenUsers(sender.getUserId(), receiver.getUserId());
    }
}