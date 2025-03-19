package com.socialmedia.backend.service;

import com.socialmedia.backend.entities.Message;
import com.socialmedia.backend.entities.MessageType;
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
    public Message sendMessage(Long senderId, Long receiverId, String content, String type) {
        // Tạo đối tượng Message
        MessageType enumType = MessageType.valueOf(type.toUpperCase());
        Message msg = Message.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .content(content)
                .messageType(enumType)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        return messageRepository.save(msg);
    }

    @Override
    public List<Message> getChatHistory(Long userA, Long userB) {
        // Lấy tin nhắn từ A->B và B->A
        List<Message> ab = messageRepository.findBySenderIdAndReceiverIdOrderByCreatedAt(userA, userB);
        List<Message> ba = messageRepository.findBySenderIdAndReceiverIdOrderByCreatedAt(userB, userA);

        // Gộp 2 danh sách lại
        List<Message> all = new ArrayList<>();
        all.addAll(ab);
        all.addAll(ba);

        // Sắp xếp all theo createdAt
        all.sort((m1, m2) -> m1.getCreatedAt().compareTo(m2.getCreatedAt()));

        return all;
    }

    @Override
    public Message editMessage(Long messageId, String newContent) {
        // Tìm message
        Message msg = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        // Cập nhật nội dung
        msg.setContent(newContent);

        return messageRepository.save(msg);
    }

    @Override
    public String deleteMessage(Long messageId) {
        Message msg = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        messageRepository.delete(msg);
        return "Message deleted successfully";
    }

    @Override
    public void markAsRead(Long messageId) {
        Message msg = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        msg.setRead(true);
        messageRepository.save(msg);
    }
}
