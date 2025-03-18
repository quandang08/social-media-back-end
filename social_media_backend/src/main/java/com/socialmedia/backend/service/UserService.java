package com.socialmedia.backend.service;

import com.socialmedia.backend.exception.UserException;
import com.socialmedia.backend.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service interface định nghĩa các phương thức liên quan đến User.
 */
@Service
public interface UserService {

    User findUserById(Long userId) throws UserException;
    /**
     * Lấy thông tin User dựa trên JWT token.
     *
     * @param jwt Token xác thực của người dùng.
     * @return Thông tin User tương ứng.
     * @throws UserException Nếu không thể xác thực người dùng.
     */
    User findUserProfileByJwt(String jwt) throws UserException;

    User updateUser(Long userId, User user) throws UserException;

    User followUser(Long userId, User user) throws UserException;

    List<User> searchUser(String query);

    User findUserByEmail(String email) throws UserException;

    /**
     * Lấy danh sách người dùng mà user hiện tại chưa follow.
     *
     * @param userId ID của người dùng hiện tại.
     * @return Danh sách User chưa được follow.
     */
    List<User> findUsersNotFollowedBy(Long userId);

    User followUserAndNotify(Long targetUserId, String jwt) throws UserException;
}
