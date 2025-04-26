package com.socialmedia.backend.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private String messageType; // "TEXT", "IMAGE", "VIDEO", "FILE"
    private String eventType; // "SENT", "DELETED", "UPDATED"
    private String attachmentUrl;
    private boolean isRead;
    private String createdAt;
    private String updatedAt;
}
