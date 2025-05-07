package com.socialmedia.backend.mapper;

import com.socialmedia.backend.models.dto.LikeDto;
import com.socialmedia.backend.models.dto.TwitDto;
import com.socialmedia.backend.models.dto.UserDto;
import com.socialmedia.backend.entities.Like;
import com.socialmedia.backend.entities.User;
import java.util.ArrayList;
import java.util.List;

public class LikeDtoMapper {
    public static List<LikeDto> toLikeDtos(List<Like> likes, User reqUser) {
        List<LikeDto> likeDtos = new ArrayList<>();

        for (Like like : likes) {
            UserDto user = UserDtoMapper.toUserDto(like.getUser());
            TwitDto twit = TwitDtoMapper.toTwitDto(like.getTwit(), reqUser);

            LikeDto likeDto = new LikeDto();
            likeDto.setId(like.getId());
            likeDto.setTwit(twit);
            likeDto.setUser(user);
            likeDtos.add(likeDto);
        }
        return likeDtos;
    }
}