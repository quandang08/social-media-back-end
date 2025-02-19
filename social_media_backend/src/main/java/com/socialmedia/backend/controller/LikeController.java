package com.socialmedia.backend.controller;

import com.socialmedia.backend.dto.LikeDto;
import com.socialmedia.backend.exception.TwitException;
import com.socialmedia.backend.exception.UserException;
import com.socialmedia.backend.mapper.LikeDtoMapper;
import com.socialmedia.backend.model.Like;
import com.socialmedia.backend.model.User;
import com.socialmedia.backend.service.LikeService;
import com.socialmedia.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api")
public class LikeController {

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @PostMapping("/{twitId}/likes")
    public ResponseEntity<LikeDto> likeTwit(@PathVariable Long twitId,
                                            @RequestHeader("Authorization") String jwt) throws UserException, TwitException {
        User user = userService.findUserProfileByJwt(jwt);
        Optional<Like> like = likeService.likeTwit(twitId, user);

        LikeDto likeDto = LikeDtoMapper.toLikeDto(like, user);

        return new ResponseEntity<>(likeDto, HttpStatus.CREATED);
    }

    @GetMapping("/{twitId}/likes")
    public ResponseEntity<List<LikeDto>> getAllLikes(@PathVariable Long twitId,
                                                     @RequestHeader("Authorization") String jwt) throws UserException, TwitException {
        User user = userService.findUserProfileByJwt(jwt);
        List<Like> likes = likeService.getAllLikes(twitId);

        List<LikeDto> likeDtos = LikeDtoMapper.toLikeDtos(likes, user);

        return new ResponseEntity<>(likeDtos, HttpStatus.OK);
    }
}
