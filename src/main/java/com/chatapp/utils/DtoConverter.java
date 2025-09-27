package com.chatapp.utils;

import com.chatapp.dto.MessageDto;
import com.chatapp.dto.UserDto;
import com.chatapp.entity.Message;
import com.chatapp.entity.User;

public class DtoConverter {
    
    public static UserDto convertToDto(User user) {
        if (user == null) return null;
        return new UserDto(user.getUserId(), user.getUsername(), user.getDisplayName(), user.getProfilePhoto());
    }
    
    public static MessageDto convertToDto(Message message) {
        if (message == null) return null;
        
        MessageDto dto = new MessageDto();
        dto.setMessageId(message.getMessageId());
        dto.setSender(convertToDto(message.getSender()));
        dto.setReceiver(convertToDto(message.getReceiver()));
        dto.setContent(message.getContent());
        dto.setCreatedAt(message.getCreatedAt());
        dto.setIsRead(message.getIsRead());
        if (message.getGroup() != null) {
            dto.setGroupId(message.getGroup().getGroupId());
        }
        return dto;
    }
}