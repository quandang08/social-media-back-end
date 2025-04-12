package com.socialmedia.backend.controller;

import com.socialmedia.backend.entities.Message;
import com.socialmedia.backend.mapper.MessageMapper;
import com.socialmedia.backend.models.MessageDto;
import com.socialmedia.backend.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/send")
    public MessageDto sendMessage(@RequestBody MessageDto messageDto) {
        Message message = messageService.saveMessage(messageDto);
        messagingTemplate.convertAndSendToUser(
                messageDto.getReceiverId().toString(),  // User ID
                "/queue/chat",  // Queue path
                messageDto  // Response message
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
    @DeleteMapping("/{messageId}")
    public void deleteMessage(@PathVariable Long messageId) {
        messageService.deleteMessage(messageId);
    }

    // Endpoint đánh dấu tin nhắn đã đọc
    @PutMapping("/{messageId}/read")
    public void markAsRead(@PathVariable Long messageId) {
        messageService.markAsRead(messageId);
    }
}
