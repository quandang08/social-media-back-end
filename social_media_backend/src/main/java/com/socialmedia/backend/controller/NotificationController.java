package com.socialmedia.backend.controller;

import com.socialmedia.backend.models.NotificationDto;
import com.socialmedia.backend.exception.UserException;
import com.socialmedia.backend.entities.Notification;
import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.service.NotificationService;
import com.socialmedia.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<NotificationDto> createNotification(
            @RequestBody NotificationDto notificationDto,
            @RequestHeader("Authorization") String jwt
    ) throws UserException {

        User actor = userService.findUserProfileByJwt(jwt);
        // Tạo notification entity từ DTO
        Notification notification = new Notification();
        notification.setUserId(notificationDto.getUserId());
        notification.setActorId(actor.getId());
        notification.setType(notificationDto.getType());
        notification.setContent(notificationDto.getContent());
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        // Lưu vào DB
        NotificationDto createdNotification = notificationService.createAndSaveNotification(notification);
        return ResponseEntity.ok(createdNotification);
    }

    @GetMapping("/")
    public ResponseEntity<List<NotificationDto>> getNotifications(
            @RequestHeader("Authorization") String jwt
    ) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        Long userId = user.getId();  // Lấy ID từ user
        List<NotificationDto> notifications = notificationService.getNotificationsForUser(userId);

        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationDto> markNotificationAsRead(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt
    ) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        Long userId = user.getId();
        NotificationDto updated = notificationService.markAsRead(id, userId);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteNotification(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt
    ) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        Long userId = user.getId();
        notificationService.deleteNotification(id, userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Notification deleted successfully");

        return ResponseEntity.ok(response);
    }
}
