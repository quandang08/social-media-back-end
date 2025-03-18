package com.socialmedia.backend.mapper;
import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.models.NotificationDto;
import com.socialmedia.backend.entities.Notification;

public class NotificationMapper {

    public static NotificationDto toNotificationDto(Notification notification, User actor) {
        return NotificationDto.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .actorId(notification.getActorId())
                .type(notification.getType())
                .content(notification.getContent())
                .read(notification.isRead())
                .image(actor != null ? actor.getImage() : null)
                .createdAt(notification.getCreatedAt() != null ? notification.getCreatedAt().toString() : null)
                .updatedAt(notification.getUpdatedAt() != null ? notification.getUpdatedAt().toString() : null)
                .build();
    }

}
