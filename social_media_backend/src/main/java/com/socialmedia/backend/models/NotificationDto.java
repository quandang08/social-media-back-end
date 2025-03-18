package com.socialmedia.backend.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {
    private Long id;
    private Long userId;
    private Long actorId;
    private String type;
    private String content;
    private boolean read;
    private String createdAt;
    private String updatedAt;
}
