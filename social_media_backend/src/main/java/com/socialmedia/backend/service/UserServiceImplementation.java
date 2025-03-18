package com.socialmedia.backend.service;

import com.socialmedia.backend.exception.UserException;
import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.repository.UserRepository;
import com.socialmedia.backend.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User findUserById(Long userId) throws UserException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found with ID: " + userId));
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email = jwtProvider.getEmailFromToken(jwt);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("User not found with email: " + email));
    }

    @Override
    public User updateUser(Long userId, User req) throws UserException {
        User user = findUserById(userId);

        // Cập nhật nếu có giá trị mới
        Optional.ofNullable(req.getFullName()).ifPresent(user::setFullName);
        Optional.ofNullable(req.getEmail()).ifPresent(user::setEmail);
        Optional.ofNullable(req.getImage()).ifPresent(user::setImage);
        Optional.ofNullable(req.getBackgroundImage()).ifPresent(user::setBackgroundImage);
        Optional.ofNullable(req.getBirthDate()).ifPresent(user::setBirthDate);
        Optional.ofNullable(req.getLocation()).ifPresent(user::setLocation);
        Optional.ofNullable(req.getBio()).ifPresent(user::setBio);
        Optional.ofNullable(req.getWebsite()).ifPresent(user::setWebsite);

        return userRepository.save(user);
    }

    @Override
    public User followUser(Long userId, User user) throws UserException {
        User followToUser = findUserById(userId);

        boolean isFollowing = user.getFollowing().contains(followToUser);

        if (isFollowing) {
            user.getFollowing().remove(followToUser);
            followToUser.getFollowers().remove(user);
        } else {
            user.getFollowing().add(followToUser);
            followToUser.getFollowers().add(user);
        }

        userRepository.save(followToUser);
        userRepository.save(user);

        return followToUser;
    }

    @Override
    public List<User> searchUser(String query) {
        System.out.println("🔍 Searching for users with query: " + query);
        return userRepository.searchUser(query);
    }

    @Override
    public User findUserByEmail(String email) throws UserException {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public List<User> findUsersNotFollowedBy(Long userId) {
        return userRepository.findUsersNotFollowedBy(userId);
    }

    @Override
    public User followUserAndNotify(Long targetUserId, String jwt) throws UserException {
        // Lấy user thực hiện follow (actor)
        User actorUser = findUserProfileByJwt(jwt);

        //Lấy user được follow (targetUser)
        User targetUser = findUserById(targetUserId);

        // Kiểm tra follow/unfollow
        boolean isFollowing = actorUser.getFollowing().contains(targetUser);
        if (isFollowing) {
            // Đang follow => unfollow
            actorUser.getFollowing().remove(targetUser);
            targetUser.getFollowers().remove(actorUser);
        } else {
            // Chưa follow => follow
            actorUser.getFollowing().add(targetUser);
            targetUser.getFollowers().add(actorUser);

            // Gửi notification
            if (!actorUser.getId().equals(targetUser.getId())) {
                notificationService.handleFollowAction(targetUser.getId(), actorUser.getId());
            }
        }

        //Lưu DB
        userRepository.save(actorUser);
        userRepository.save(targetUser);

        //Trả về user được follow (targetUser)
        return targetUser;
    }
}
