package com.socialmedia.backend.mapper;

import com.socialmedia.backend.models.TwitDto;
import com.socialmedia.backend.models.UserDto;
import com.socialmedia.backend.entities.Twit;
import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.util.TwitUtil;

import java.util.List;
import java.util.stream.Collectors;

public class TwitDtoMapper {

    /**
     * Chuyển đổi từ đối tượng Twit sang TwitDto để trả về dữ liệu theo đúng định dạng cần thiết.
     * @param twit Đối tượng Twit cần chuyển đổi.
     * @param reqUser Người dùng hiện tại đang gửi request.
     * @return TwitDto chứa thông tin bài đăng.
     */
    public static TwitDto toTwitDto(Twit twit, User reqUser) {
        UserDto user = UserDtoMapper.toUserDto(twit.getUser());

        boolean isLiked = TwitUtil.isLikedByReqUser(reqUser, twit);
        boolean isRetwited = TwitUtil.isRetwitedByUser(reqUser, twit);

        List<Long> retwitUserId = twit.getRetwitUser().stream()
                .map(User::getId)
                .collect(Collectors.toList());

        TwitDto twitDto = new TwitDto();
        twitDto.setId(twit.getId());
        twitDto.setContent(twit.getContent());
        twitDto.setCreatedAt(twit.getCreatedAt());
        twitDto.setImage(twit.getImage());
        twitDto.setVideo(twit.getVideo());
        twitDto.setTotalLikes(twit.getLikes().size());
        twitDto.setTotalReplies(twit.getReplyTwits().size());
        twitDto.setTotalRetweets(twit.getRetwitUser().size());
        twitDto.setUser(user);
        twitDto.setLiked(isLiked);
        twitDto.setRetwit(isRetwited);
        twitDto.setRetwitUserId(retwitUserId);
        twitDto.setReplyTwits(toTwitDtos(twit.getReplyTwits(), reqUser));

        return twitDto;
    }

    /**
     * Chuyển đổi danh sách các Twit sang danh sách TwitDto.
     * @param twits Danh sách các bài đăng cần chuyển đổi.
     * @param reqUser Người dùng hiện tại.
     * @return Danh sách TwitDto đã được ánh xạ.
     */
    public static List<TwitDto> toTwitDtos(List<Twit> twits, User reqUser) {
        return twits.stream()
                .map(twit -> toReplyTwitDto(twit, reqUser))
                .collect(Collectors.toList());
    }

    /**
     * Chuyển đổi một bài Twit thành TwitDto nhưng dành riêng cho trường hợp reply (trả lời).
     * @param twit Đối tượng Twit cần chuyển đổi.
     * @param reqUser Người dùng hiện tại.
     * @return TwitDto của bài trả lời.
     */
    private static TwitDto toReplyTwitDto(Twit twit, User reqUser) {
        UserDto user = UserDtoMapper.toUserDto(twit.getUser());

        boolean isLiked = TwitUtil.isLikedByReqUser(reqUser, twit);
        boolean isRetwited = TwitUtil.isRetwitedByUser(reqUser, twit);

        List<Long> retwitUserId = twit.getRetwitUser().stream()
                .map(User::getId)
                .collect(Collectors.toList());

        TwitDto twitDto = new TwitDto();
        twitDto.setId(twit.getId());
        twitDto.setContent(twit.getContent());
        twitDto.setCreatedAt(twit.getCreatedAt());
        twitDto.setImage(twit.getImage());
        twitDto.setVideo(twit.getVideo());
        twitDto.setTotalLikes(twit.getLikes().size());
        twitDto.setTotalReplies(twit.getReplyTwits().size());
        twitDto.setTotalRetweets(twit.getRetwitUser().size());
        twitDto.setUser(user);
        twitDto.setLiked(isLiked);
        twitDto.setRetwit(isRetwited);
        twitDto.setRetwitUserId(retwitUserId);

        return twitDto;
    }
}
