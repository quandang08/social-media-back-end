package com.socialmedia.backend.service;

import com.socialmedia.backend.exception.TwitException;
import com.socialmedia.backend.exception.UserException;
import com.socialmedia.backend.entities.Like;
import com.socialmedia.backend.entities.Twit;
import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.models.reaction.LikeResponse;
import com.socialmedia.backend.repository.LikeRepository;
import com.socialmedia.backend.repository.TwitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeServiceImplementation implements LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private TwitService twitService;

    @Autowired
    private TwitRepository twitRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Override
    public Optional<Like> likeTwit(Long twitId, User user) throws UserException, TwitException {
        Optional<Like> isLikeExist = likeRepository.isLikeExists(user.getId(), twitId);

        if (isLikeExist.isPresent()) {
            // Nếu đã like -> bỏ like
            likeRepository.deleteById(isLikeExist.get().getId());
            return isLikeExist;
        }

        // Nếu chưa like -> tạo like mới
        Twit twit = twitService.findById(twitId);
        Like like = new Like();
        like.setTwit(twit);
        like.setUser(user);

        Like savedLike = likeRepository.save(like);
        return Optional.of(savedLike);
    }

    @Override
    public List<Like> getAllLikes(Long twitId) {
        return likeRepository.findByTwitId(twitId);
    }

    @Override
    public int countLikes(Long twitId) {
        return likeRepository.countLikesByTwitId(twitId);
    }

    @Override
    public LikeResponse handleLikeTwit(Long twitId, String jwt) throws UserException, TwitException {
        User user = userService.findUserProfileByJwt(jwt);

        Optional<Like> like = likeTwit(twitId, user);

        Twit twit = twitService.findById(twitId);
        Long twitOwnerId = twit.getUser().getId();

        if (!user.getId().equals(twitOwnerId) && like.isPresent()) {
            notificationService.handleLikeAction(twitId, twitOwnerId, user.getId());
        }

        int totalLikes = countLikes(twitId);

        return LikeResponse.builder()
                .twitId(twitId)
                .userLikeName(user.getFullName())
                .likeCount(totalLikes)
                .build();
    }
}

