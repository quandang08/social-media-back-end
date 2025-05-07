package com.socialmedia.backend.mapper;

import com.socialmedia.backend.entities.Message;
import com.socialmedia.backend.models.dto.MessageDto;

public class MessageMapper {
    public static MessageDto toMessageDto(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .content(message.getContent())
                .messageType(message.getMessageType().toString())
                .attachmentUrl(message.getAttachmentUrl())
                .isRead(message.isRead())
                .createdAt(message.getCreatedAt() != null ? message.getCreatedAt().toString() : null)
                .updatedAt(message.getUpdatedAt() != null ? message.getUpdatedAt().toString() : null)
                .build();

    }
}
