package com.socialmedia.backend.controller;

import com.socialmedia.backend.models.dto.LikeDto;
import com.socialmedia.backend.exception.TwitException;
import com.socialmedia.backend.exception.UserException;
import com.socialmedia.backend.mapper.LikeDtoMapper;
import com.socialmedia.backend.entities.Like;
import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.models.dto.WrapperResponse;
import com.socialmedia.backend.service.LikeService;
import com.socialmedia.backend.service.NotificationService;
import com.socialmedia.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LikeController {

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private NotificationService notificationService;

    /*
        jwt: always send in request Header
     */
    @PostMapping("/{twitId}/likes")
    public WrapperResponse<Object> likeTwit(@PathVariable Long twitId, @RequestHeader("Authorization") String jwt)
            throws TwitException, UserException {
        return WrapperResponse.builder()
                .statusCode(200)
                .result(likeService.handleLikeTwit(twitId, jwt))
                .build();
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
