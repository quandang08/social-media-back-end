package com.socialmedia.backend.service;

import com.socialmedia.backend.entities.Message;
import com.socialmedia.backend.models.MessageDto;

import java.util.List;

public interface MessageService {
    Message saveMessage(MessageDto messageDto);

    List<Message> getChatHistory(Long userA, Long userB);

    Message editMessage(Long messageId, String newContent);

    void deleteMessage(Long messageId);

    void markAsRead(Long messageId);
}
