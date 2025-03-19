package com.socialmedia.backend.service;

import com.socialmedia.backend.entities.Message;

import java.util.List;

public interface MessageService {
    Message sendMessage(Long senderId, Long receiverId, String content, String messageType);

    List<Message> getChatHistory(Long userA, Long userB);

    Message editMessage(Long messageId, String newContent);

    String deleteMessage(Long messageId);

    void markAsRead(Long messageId);
}
