package com.socialmedia.backend.service;

import com.socialmedia.backend.dto.NotificationDto;
import com.socialmedia.backend.exception.UserException;

import java.util.List;

public interface NotificationService {
    List<NotificationDto> getNotificationsForUser(Long userId) throws UserException;
    NotificationDto markAsRead(Long notificationId, Long userId) throws UserException;
}
