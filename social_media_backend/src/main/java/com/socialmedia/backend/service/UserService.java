package com.socialmedia.backend.service;

import com.socialmedia.backend.exception.UserException;
import com.socialmedia.backend.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service interface định nghĩa các phương thức liên quan đến User.
 */
@Service
public interface UserService {

    /**
     * Tìm người dùng theo ID.
     *
     * @param userId ID của người dùng cần tìm.
     * @return Thông tin User tương ứng.
     * @throws UserException Nếu không tìm thấy User.
     */
    User findUserById(Long userId) throws UserException;

    /**
     * Lấy thông tin User dựa trên JWT token.
     *
     * @param jwt Token xác thực của người dùng.
     * @return Thông tin User tương ứng.
     * @throws UserException Nếu không thể xác thực người dùng.
     */
    User findUserProfileByJwt(String jwt) throws UserException;

    /**
     * Cập nhật thông tin người dùng.
     *
     * @param userId ID của người dùng cần cập nhật.
     * @param user Thông tin mới của User.
     * @return User sau khi được cập nhật.
     * @throws UserException Nếu User không tồn tại hoặc không thể cập nhật.
     */
    User updateUser(Long userId, User user) throws UserException;

    /**
     * Thực hiện follow một người dùng khác.
     *
     * @param userId ID của người dùng muốn follow.
     * @param user Người dùng hiện tại thực hiện follow.
     * @return User sau khi cập nhật danh sách followers.
     * @throws UserException Nếu User không tồn tại hoặc có lỗi xảy ra.
     */
    User followUser(Long userId, User user) throws UserException;

    /**
     * Tìm kiếm người dùng theo từ khóa.
     *
     * @param query Từ khóa tìm kiếm.
     * @return Danh sách User phù hợp với từ khóa.
     */
    List<User> searchUser(String query);


    User findUserByEmail(String email) throws UserException;
}
