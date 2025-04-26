package com.socialmedia.backend.service;

import com.socialmedia.backend.entities.Message;
import com.socialmedia.backend.entities.MessageType;
import com.socialmedia.backend.models.MessageDto;
import com.socialmedia.backend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImplementation implements MessageService{

    private final MessageRepository messageRepository;

    @Override
    public Message saveMessage(MessageDto messageDto) {
        MessageType enumType = MessageType.valueOf(messageDto.getMessageType().toUpperCase());
        Message msg = Message.builder()
                .senderId(messageDto.getSenderId())
                .receiverId(messageDto.getReceiverId())
                .content(messageDto.getContent())
                .messageType(enumType)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
        return messageRepository.save(msg);
    }

    @Override
    public void deleteMessage(Long messageId) {
        Message msg = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        messageRepository.delete(msg);
    }

    @Override
    public List<Message> getChatHistory(Long userA, Long userB) {
        List<Message> ab = messageRepository.findBySenderIdAndReceiverIdOrderByCreatedAt(userA, userB);
        List<Message> ba = messageRepository.findBySenderIdAndReceiverIdOrderByCreatedAt(userB, userA);

        List<Message> all = new ArrayList<>();
        all.addAll(ab);
        all.addAll(ba);

        all.sort((m1, m2) -> m1.getCreatedAt().compareTo(m2.getCreatedAt()));

        return all;
    }

    @Override
    public Message editMessage(Long messageId, String newContent) {
        Message msg = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        msg.setContent(newContent);

        return messageRepository.save(msg);
    }

    @Override
    public void markAsRead(Long messageId) {
        Message msg = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        msg.setRead(true);
        messageRepository.save(msg);
    }
}
