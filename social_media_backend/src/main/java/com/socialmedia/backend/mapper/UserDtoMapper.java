package com.socialmedia.backend.mapper;

import com.socialmedia.backend.models.UserDto;
import com.socialmedia.backend.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserDtoMapper {
    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFullName(user.getFullName());
        userDto.setImage(user.getImage());
        userDto.setBackgroundImage(user.getBackgroundImage());
        userDto.setBio(user.getBio());
        userDto.setBirthDate(String.valueOf(user.getBirthDate()));

        // Chỉ lưu ID thay vì Object đầy đủ
        userDto.setFollowers(user.getFollowers().stream().map(User::getId).toList());
        userDto.setFollowing(user.getFollowing().stream().map(User::getId).toList());

        userDto.setLogin_with_google(user.isLoginWithGoogle());
        userDto.setLocation(user.getLocation());
        userDto.setVerified(false);
        return userDto;
    }


    public static List<UserDto> toUserDtos(List<User> followers) {
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : followers) {
            userDtos.add(toUserDto(user));
        }
        return userDtos;
    }
}
