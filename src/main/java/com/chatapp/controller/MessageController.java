package com.chatapp.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.dto.MessageDto;
import com.chatapp.dto.SendGroupMessageRequest;
import com.chatapp.dto.SendMessageRequest;
import com.chatapp.entity.Message;
import com.chatapp.service.MessageService;
import com.chatapp.utils.DtoConverter;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*") // For development, configure appropriately for production
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/direct")
    public ResponseEntity<?> sendDirectMessage(@RequestBody SendMessageRequest request) {
        try {
            // Validate content
            if (request.getContent() == null || request.getContent().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Message content cannot be empty");
            }
            
            Message message = messageService.sendDirectMessage(
                String.valueOf(request.getSenderId()),
                String.valueOf(request.getReceiverId()),
                request.getContent().trim()
            );
            return ResponseEntity.ok(DtoConverter.convertToDto(message));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/group")
    public ResponseEntity<?> sendGroupMessage(@RequestBody SendGroupMessageRequest request) {
        try {
            // Validate content
            if (request.getContent() == null || request.getContent().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Message content cannot be empty");
            }
            
            Message message = messageService.sendGroupMessage(
                String.valueOf(request.getSenderId()),
                String.valueOf(request.getGroupId()),
                request.getContent().trim()
            );
            return ResponseEntity.ok(DtoConverter.convertToDto(message));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<MessageDto>> getUserMessages(
            @PathVariable Integer userId,
            Pageable pageable) {
        try {
            Page<Message> messages = messageService.getMessagesByUser(userId, pageable);
            Page<MessageDto> messageDtos = messages.map(DtoConverter::convertToDto);
            return ResponseEntity.ok(messageDtos);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/direct/{userId1}/{userId2}")
    public ResponseEntity<Page<MessageDto>> getDirectMessages(
            @PathVariable Integer userId1,
            @PathVariable Integer userId2,
            Pageable pageable) {
        try {
            Page<Message> messages = messageService.getDirectMessagesBetweenUsers(userId1, userId2, pageable);
            Page<MessageDto> messageDtos = messages.map(DtoConverter::convertToDto);
            return ResponseEntity.ok(messageDtos);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<MessageDto>> getChatHistory(
            @RequestParam String user1,
            @RequestParam String user2) {
        try {
            List<Message> rawMessages = messageService.getChatHistory(user1, user2);
            List<MessageDto> messages = rawMessages.stream()
                    .map(DtoConverter::convertToDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<MessageDto>> getGroupMessageHistory(@PathVariable Integer groupId) {
        try {
            // The service does not currently expose a group-specific retrieval method.
            // Return an empty list for now; implement a service method like
            // List<Message> getGroupMessages(Integer groupId) if you need actual data.
            List<MessageDto> messages = Collections.emptyList();
            return ResponseEntity.ok(messages);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    }

