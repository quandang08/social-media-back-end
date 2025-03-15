package com.socialmedia.backend.controller;

import com.socialmedia.backend.dto.NotificationDto;
import com.socialmedia.backend.exception.UserException;
import com.socialmedia.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/")
    public ResponseEntity<List<NotificationDto>> getNotifications(
            @RequestHeader("Authorization") String jwt
    ) throws UserException {
        Long userId = getUserIdFromJwt(jwt);
        List<NotificationDto> notifications = notificationService.getNotificationsForUser(userId);

        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationDto> markNotificationAsRead(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt
    ) throws UserException {
        Long userId = getUserIdFromJwt(jwt);
        NotificationDto updated = notificationService.markAsRead(id, userId);

        return ResponseEntity.ok(updated);
    }

    private Long getUserIdFromJwt(String jwt) {
        // Ở dự án thật có thể parse token bằng JWT lib
        // Hoặc đã có 1 filter set "reqUserId" trong request
        return 4L;
    }
}
