package com.socialmedia.backend.service;

import com.socialmedia.backend.exception.TwitException;
import com.socialmedia.backend.exception.UserException;
import com.socialmedia.backend.entities.Twit;
import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.models.dto.TwitDto;
import com.socialmedia.backend.request.TwitReplyRequest;

import java.util.List;

public interface TwitService {

    // Tạo một Twit mới từ yêu cầu (req) và người dùng (user).
    public Twit createTwit(Twit req, User user) throws UserException;

    // Lấy danh sách tất cả các Twit
    public List<Twit> findAllTwit();

    // Thực hiện retwit (chia sẻ lại Twit) với twitId từ người dùng (user).
    public Twit retwit(Long twitId, User user) throws UserException, TwitException;

    // Tìm kiếm Twit theo ID.
    public Twit findById(Long twitId) throws TwitException;

    // Xóa một Twit dựa trên ID và người dùng yêu cầu xóa.
    public void deleteTwitById(Long twitId, Long userId) throws UserException, TwitException;

    // Loại bỏ một Twit khỏi danh sách retwit của người dùng.
    public Twit removeFromRetwit(Long twitId, User user) throws UserException, TwitException;

    // Tạo phản hồi cho một Twit từ yêu cầu (req) và người dùng (user).
    public Twit createdReply(TwitReplyRequest req, User user) throws TwitException;

    TwitDto replyTwitAndNotify(TwitReplyRequest req, String jwt) throws UserException;

    // Lấy tất cả các Twit của người dùng (user).
    public List<Twit> getUserTwit(User user);

    // Tìm các Twit mà người dùng (user) đã thích.
    public List<Twit> findByLikesContainsUser(User user);


}


