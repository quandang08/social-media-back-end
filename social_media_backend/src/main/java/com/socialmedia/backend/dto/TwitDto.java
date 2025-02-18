package com.socialmedia.backend.dto;

import com.socialmedia.backend.model.Twit;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TwitDto {
    private Long id;

    private String content;

    private String image;

    private String video;

    private UserDto user;

    private LocalDateTime createdAt;

    private int totalLikes;

    private int totalReplies;

    private int totalRetweets;

    private boolean isLiked;

    private List<Long> retwitUserId;
    private List<TwitDto> replyTwits;
}
