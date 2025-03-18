package com.socialmedia.backend.mapper;

import com.socialmedia.backend.models.NotificationDto;
import com.socialmedia.backend.entities.Notification;

public class NotificationMapper {
    public static NotificationDto toNotificationDto(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .actorId(notification.getActorId())
                .type(notification.getType())
                .content(notification.getContent())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt() != null ? notification.getCreatedAt().toString() : null)
                .updatedAt(notification.getUpdatedAt() != null ? notification.getUpdatedAt().toString() : null)
                .build();
    }
}
