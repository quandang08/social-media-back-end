package com.socialmedia.backend.controller;

import com.socialmedia.backend.entities.Message;
import com.socialmedia.backend.mapper.MessageMapper;
import com.socialmedia.backend.models.MessageDto;
import com.socialmedia.backend.repository.MessageRepository;
import com.socialmedia.backend.security.CustomUserDetails;
import com.socialmedia.backend.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;

    @PostMapping("/send")
    public MessageDto sendMessage(@RequestBody MessageDto messageDto) {
        Message message = messageService.saveMessage(messageDto);
        messagingTemplate.convertAndSendToUser(
                messageDto.getReceiverId().toString(),
                "/queue/chat",
                messageDto
        );
        return MessageMapper.toMessageDto(message);
    }

    @GetMapping("/history")
    public List<MessageDto> getChatHistory(
            @RequestParam Long userA,
            @RequestParam Long userB
    ) {
        List<Message> messages = messageService.getChatHistory(userA, userB);
        return messages.stream()
                .map(MessageMapper::toMessageDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/{messageId}")
    public MessageDto editMessage(
            @PathVariable Long messageId,
            @RequestBody MessageDto messageDto
    ) {
        Message message = messageService.editMessage(messageId, messageDto.getContent());
        return MessageMapper.toMessageDto(message);
    }

    // Endpoint xóa tin nhắn
//    @DeleteMapping("/{messageId}")
//    public void deleteMessage(@PathVariable Long messageId) {
//        // Lấy message trước khi xóa để lấy thông tin sender/receiver
//        Message message = messageRepository.findById(messageId)
//                .orElseThrow(() -> new RuntimeException("Message not found"));
//
//        // Thực hiện xóa
//        messageService.deleteMessage(messageId);
//
//        // Gửi thông báo realtime
//        messagingTemplate.convertAndSend(
//                "/topic/chat." + getConversationId(message.getSenderId(), message.getReceiverId()),
//                Map.of(
//                        "type", "MESSAGE_DELETED",
//                        "messageId", messageId
//                )
//        );
//    }

    private String getConversationId(Long userA, Long userB) {
        return userA < userB ? userA + "_" + userB : userB + "_" + userA;
    }

    // Endpoint đánh dấu tin nhắn đã đọc
    @PutMapping("/{messageId}/read")
    public void markAsRead(@PathVariable Long messageId) {
        messageService.markAsRead(messageId);
    }
}
