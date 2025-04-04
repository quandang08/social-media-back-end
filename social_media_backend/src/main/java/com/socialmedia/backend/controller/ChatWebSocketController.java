package com.socialmedia.backend.controller;

import com.socialmedia.backend.entities.Message;
import com.socialmedia.backend.mapper.MessageMapper;
import com.socialmedia.backend.models.MessageDto;
import com.socialmedia.backend.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    @MessageMapping("/chat.sendMessage")
    public void handleMessage(@Payload MessageDto messageDto) {
        try {
            // 1. Lưu vào database
            Message savedMessage = messageService.sendMessage(
                    messageDto.getSenderId(),
                    messageDto.getReceiverId(),
                    messageDto.getContent(),
                    messageDto.getMessageType()
            );

            // 2. Chuẩn bị DTO để gửi đi
            MessageDto responseDto = MessageMapper.toMessageDto(savedMessage);

            // 3. Gửi cho người nhận (QUAN TRỌNG: dùng convertAndSendToUser)
            messagingTemplate.convertAndSendToUser(
                    messageDto.getReceiverId().toString(),
                    "/queue/chat",
                    responseDto
            );

            // 4. Gửi cho người gửi (để sync đa thiết bị)
            messagingTemplate.convertAndSendToUser(
                    messageDto.getSenderId().toString(),
                    "/queue/chat",
                    responseDto
            );

        } catch (Exception e) {
            // 5. Xử lý lỗi và gửi thông báo
            messagingTemplate.convertAndSendToUser(
                    messageDto.getSenderId().toString(),
                    "/queue/errors",
                    "Gửi tin nhắn thất bại: " + e.getMessage()
            );
        }
    }
}
