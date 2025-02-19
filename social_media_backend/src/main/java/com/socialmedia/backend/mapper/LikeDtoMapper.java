package com.socialmedia.backend.mapper;

import com.socialmedia.backend.dto.LikeDto;
import com.socialmedia.backend.dto.TwitDto;
import com.socialmedia.backend.dto.UserDto;
import com.socialmedia.backend.model.Like;
import com.socialmedia.backend.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LikeDtoMapper {

    public static LikeDto toLikeDto(Optional<Like> like, User reqUser) {
        if (like.isEmpty()) {
            return null; // hoặc throw new IllegalArgumentException("Like not found");
        }

        Like likeObj = like.get(); // Giờ đây đã an toàn để gọi get()

        UserDto user = UserDtoMapper.toUserDto(likeObj.getUser());
        TwitDto twit = TwitDtoMapper.toTwitDto(likeObj.getTwit(), reqUser);

        LikeDto likeDto = new LikeDto();
        likeDto.setId(likeObj.getId());
        likeDto.setTwit(twit);
        likeDto.setUser(user);

        return likeDto;
    }

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