package com.socialmedia.backend.controller;

import com.socialmedia.backend.entities.Message;
import com.socialmedia.backend.mapper.MessageMapper;
import com.socialmedia.backend.models.MessageDto;
import com.socialmedia.backend.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
            if(StringUtils.isBlank(messageDto.getMessageType())) {
                messageDto.setMessageType("TEXT");
            }
            System.out.println("📨 Server nhận tin nhắn: " + messageDto);
            Message savedMessage = messageService.saveMessage(messageDto);
            MessageDto objResponse = MessageMapper.toMessageDto(savedMessage);
            messagingTemplate.convertAndSend(
                    "/queue/chat." + messageDto.getReceiverId(),
                    objResponse
            );
            /*messagingTemplate.convertAndSend(
                    "/queue/chat.success." + messageDto.getSenderId(),
                    objResponse
            );*/
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi xử lý tin nhắn: " + e.getMessage());
        }
    }

    // POST - mark daxem = 1 (messsagye)
    //

}
