package com.socialmedia.backend.service;

import com.socialmedia.backend.exception.TwitException;
import com.socialmedia.backend.exception.UserException;
import com.socialmedia.backend.model.Like;
import com.socialmedia.backend.model.User;

import java.util.List;
import java.util.Optional;

public interface LikeService {

    public Optional<Like> likeTwit(Long twitId, User user)throws UserException, TwitException;

    public List<Like> getAllLikes(Long twitId)throws TwitException;
}
