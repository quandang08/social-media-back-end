package com.socialmedia.backend.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class TwitReplyRequest {

    // Getter & Setter thủ công
    private String content;
    private Long twitId;
    private LocalDateTime createdAt;
    private String image;

}
