package com.socialmedia.backend.util;

import com.socialmedia.backend.entities.Twit;
import com.socialmedia.backend.entities.User;

public class TwitUtil {

    /**
     * Kiểm tra xem người dùng đã like bài đăng (twit) chưa.
     *
     * @param reqUser Người dùng đang kiểm tra.
     * @param twit Bài đăng cần kiểm tra.
     * @return true nếu người dùng đã like bài đăng, ngược lại trả về false.
     */
    public static boolean isLikedByReqUser(User reqUser, Twit twit) {
        return twit.getLikes().stream()
                .anyMatch(like -> like.getUser().getId().equals(reqUser.getId()));
    }

    /**
     * Kiểm tra xem người dùng đã retweet bài đăng chưa.
     *
     * @param reqUser Người dùng đang kiểm tra.
     * @param twit Bài đăng cần kiểm tra.
     * @return true nếu người dùng đã retweet bài đăng, ngược lại trả về false.
     */
    public static boolean isRetwitedByUser(User reqUser, Twit twit) {
        return twit.getRetwitUser().stream()
                .anyMatch(user -> user.getId().equals(reqUser.getId()));
    }
}
