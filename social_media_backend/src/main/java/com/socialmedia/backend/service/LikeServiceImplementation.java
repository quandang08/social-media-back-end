package com.socialmedia.backend.service;

import com.socialmedia.backend.exception.TwitException;
import com.socialmedia.backend.exception.UserException;
import com.socialmedia.backend.model.Like;
import com.socialmedia.backend.model.Twit;
import com.socialmedia.backend.model.User;
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

    @Override
    public Optional<Like> likeTwit(Long twitId, User user) throws UserException, TwitException {
        Optional<Like> isLikeExist = likeRepository.isLikeExists(user.getId(), twitId);

        if (isLikeExist.isPresent()) {
            likeRepository.deleteById(isLikeExist.get().getId());
            return isLikeExist;
        }

        Twit twit = twitService.findById(twitId);

        Like like = new Like();
        like.setTwit(twit);
        like.setUser(user);

        Like savedLike = likeRepository.save(like);

        twit.getLikes().add(savedLike);
        twitRepository.save(twit);

        return Optional.of(savedLike);
    }

    @Override
    public List<Like> getAllLikes(Long twitId) {
        return likeRepository.findByTwitId(twitId);
    }
}
