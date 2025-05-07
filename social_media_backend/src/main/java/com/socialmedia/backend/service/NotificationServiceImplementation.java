package com.socialmedia.backend.service;

import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.models.NotificationDto;
import com.socialmedia.backend.exception.UserException;
import com.socialmedia.backend.entities.Notification;
import com.socialmedia.backend.repository.NotificationRepository;
import com.socialmedia.backend.mapper.NotificationMapper;
import com.socialmedia.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.socialmedia.backend.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImplementation implements NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public List<NotificationDto> getNotificationsForUser(Long userId) throws UserException {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return notifications.stream()
                .map(notification -> {
                    // Lấy actor
                    User actor = userRepository.findById(notification.getActorId()).orElse(null);
                    // Gọi mapper có 2 tham số
                    return NotificationMapper.toNotificationDto(notification, actor);
                })
                .collect(Collectors.toList());
    }

    @Override
    public NotificationDto markAsRead(Long notificationId, Long userId) throws UserException {
        Notification noti = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new UserException("Notification not found"));
        User actor = userRepository.findById(noti.getActorId()).orElse(null);

        if (!noti.getUserId().equals(userId)) {
            throw new UserException("Bạn không có quyền cập nhật thông báo này");
        }

        // Đánh dấu thông báo là đã đọc
        noti.setRead(true);
        noti.setUpdatedAt(LocalDateTime.now());
        notificationRepository.save(noti);

        //Trả về DTO của notification đã cập nhật
        return NotificationMapper.toNotificationDto(noti, actor);
    }

    @Override
    public void deleteNotification(Long notificationId, Long userId) throws UserException {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        if (!notification.getUserId().equals(userId)) {
            throw new UserException("You are not authorized to delete this notification");
        }

        notificationRepository.delete(notification);
    }

    @Override
    public void markAllAsRead(Long userId) throws UserException {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        notifications.forEach(noti -> {
            if (!noti.isRead()) {
                noti.setRead(true);
                noti.setUpdatedAt(LocalDateTime.now());
            }
        });
        notificationRepository.saveAll(notifications);
    }

    @Override
    public NotificationDto createAndSaveNotification(Notification notification) {
        Notification savedNotification = notificationRepository.save(notification);
        User actor = userRepository.findById(notification.getActorId())
                .orElse(null);
        NotificationDto dto = NotificationMapper.toNotificationDto(savedNotification, actor);

        // Gửi thông báo realtime qua WebSocket
        messagingTemplate.convertAndSend("/topic/notifications/" + savedNotification.getUserId(), dto);

        return dto;
    }

    @Override
    public Notification createNotificationInstance(Long targetUserId, Long actorUserId, String type, String content) {
        return Notification.builder()
                .userId(targetUserId)
                .actorId(actorUserId)
                .type(type)
                .content(content)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public void handleLikeAction(Long postId, Long targetUserId, Long actorUserId) throws UserException {
        createAndSaveNotification(createNotificationInstance(targetUserId, actorUserId, "like", "liked your post"));
    }



    @Override
    public void handleCommentAction(Long postId, Long targetUserId, Long actorUserId, String commentContent) {
        createAndSaveNotification(
                createNotificationInstance(
                        targetUserId,
                        actorUserId,
                        "comment",
                        commentContent
                )
        );
    }

    @Override
    public void handleFollowAction(Long targetUserId, Long actorUserId) throws UserException {
        createAndSaveNotification(createNotificationInstance(targetUserId, actorUserId, "follow", "started following you"));
    }
}
