package com.socialmedia.backend.models.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LikeResponse {
    Long twitId;
    String userLikeName;
    Integer likeCount;
}
