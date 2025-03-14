package com.socialmedia.backend.controller;

import com.socialmedia.backend.dto.UserDto;
import com.socialmedia.backend.exception.UserException;
import com.socialmedia.backend.mapper.UserDtoMapper;
import com.socialmedia.backend.model.User;
import com.socialmedia.backend.service.UserService;
import com.socialmedia.backend.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile(@RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        UserDto userDto = UserDtoMapper.toUserDto(user);
        userDto.setReq_user(true);

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId,
                                               @RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserById(userId);
        User reqUser = userService.findUserProfileByJwt(jwt);

        UserDto userDto = UserDtoMapper.toUserDto(user);
        userDto.setReq_user(UserUtil.isReqUser(reqUser, user));
        userDto.setFollowed(UserUtil.isFollowedByReqUser(reqUser, user));

        return new ResponseEntity<>(userDto, HttpStatus.ACCEPTED);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUser(@RequestParam String query,
                                                    @RequestHeader("Authorization") String jwt) throws UserException {
        User reqUser = userService.findUserProfileByJwt(jwt);
        List<User> users = userService.searchUser(query);

        List<UserDto> userDtos = users.stream()
                .map(user -> {
                    UserDto dto = UserDtoMapper.toUserDto(user);
                    dto.setFollowed(UserUtil.isFollowedByReqUser(reqUser, user));
                    return dto;
                })
                .toList();

        return ResponseEntity.ok(userDtos);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUser(@RequestBody User req,
                                              @RequestHeader("Authorization") String jwt) throws UserException {
        User reqUser = userService.findUserProfileByJwt(jwt);
        User user = userService.updateUser(reqUser.getId(), req);

        return ResponseEntity.ok(UserDtoMapper.toUserDto(user));
    }

    @PutMapping("/{userId}/follow")
    public ResponseEntity<UserDto> followUser(@PathVariable Long userId,
                                              @RequestHeader("Authorization") String jwt) throws UserException {
        User reqUser = userService.findUserProfileByJwt(jwt);
        User user = userService.followUser(userId, reqUser);

        UserDto userDto = UserDtoMapper.toUserDto(user);
        userDto.setFollowed(UserUtil.isFollowedByReqUser(reqUser, user));

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/not-followed")
    public ResponseEntity<List<UserDto>> getUsersNotFollowed(@RequestHeader("Authorization") String jwt) throws UserException {
        User reqUser = userService.findUserProfileByJwt(jwt);
        List<User> users = userService.findUsersNotFollowedBy(reqUser.getId());

        List<UserDto> userDtos = users.stream()
                .map(UserDtoMapper::toUserDto)
                .toList();

        return ResponseEntity.ok(userDtos);
    }

}
