package com.socialmedia.backend.controller;

import com.socialmedia.backend.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class NotificationWebSocketController {
    private final SimpMessagingTemplate messagingTemplate;

    // Phương thức này được gọi khi có notification mới cần gửi tới client
    public void sendNotification(NotificationDto notificationDto) {
        // Gửi notification tới topic /topic/notifications
        messagingTemplate.convertAndSend("/topic/notifications", notificationDto);
    }
}
