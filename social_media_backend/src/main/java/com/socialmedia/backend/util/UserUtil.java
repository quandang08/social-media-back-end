package com.socialmedia.backend.util;

import com.socialmedia.backend.model.User;

public class UserUtil {
    public static boolean isReqUser(User reqUser, User user2){
        return reqUser.getId().equals(user2.getId());
    }

    public static boolean isFollowedByReqUser(User reqUser, User user2){
        return reqUser.getFollowing().contains(user2);
    }
}
