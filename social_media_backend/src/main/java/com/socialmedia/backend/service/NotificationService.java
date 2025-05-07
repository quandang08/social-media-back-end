package com.socialmedia.backend.service;

import com.socialmedia.backend.models.dto.NotificationDto;
import com.socialmedia.backend.exception.UserException;
import com.socialmedia.backend.entities.Notification;

import java.util.List;

public interface NotificationService {
    List<NotificationDto> getNotificationsForUser(Long userId) throws UserException;

    NotificationDto markAsRead(Long notificationId, Long userId) throws UserException;

    void deleteNotification(Long notificationId, Long userId) throws UserException;

    void markAllAsRead(Long userId) throws UserException;

    // Tạo và lưu notification vào DB, sau đó trả về DTO
    NotificationDto createAndSaveNotification(Notification notification) throws UserException;

    // Tạo một instance Notification nhưng chưa lưu vào DB
    Notification createNotificationInstance(Long targetUserId, Long actorUserId, String type, String content) throws UserException;

    void handleLikeAction(Long postId, Long targetUserId, Long actorUserId) throws UserException;
    void handleCommentAction(Long postId, Long targetUserId, Long actorUserId, String commentContent) throws UserException;
    void handleFollowAction(Long targetUserId, Long actorUserId) throws UserException;

}
