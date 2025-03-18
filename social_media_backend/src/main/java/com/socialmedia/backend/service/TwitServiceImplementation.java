package com.socialmedia.backend.service;

import com.socialmedia.backend.exception.TwitException;
import com.socialmedia.backend.exception.UserException;
import com.socialmedia.backend.entities.Twit;
import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.mapper.TwitDtoMapper;
import com.socialmedia.backend.models.TwitDto;
import com.socialmedia.backend.repository.TwitRepository;
import com.socialmedia.backend.request.TwitReplyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class TwitServiceImplementation implements TwitService {

    @Autowired
    private TwitRepository twitRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;

    @Override
    public Twit createTwit(Twit req, User user) throws UserException {
        Twit twit = new Twit();

        twit.setContent(req.getContent());
        twit.setCreatedAt(LocalDateTime.now());
        twit.setImage(req.getImage());
        twit.setUser(user);
        twit.setReply(false);
        twit.setTwit(true);
        twit.setVideo(req.getVideo());

        return twitRepository.save(twit);
    }

    @Override
    public List<Twit> findAllTwit() {
        return twitRepository.findAllByIsTwitTrueOrderByCreatedAtDesc();
    }

    @Override
    public Twit retwit(Long twitId, User user) throws UserException, TwitException {
        if (user == null || user.getId() == null) {
            throw new UserException("User must be authenticated to retweet");
        }

        Twit twit = findById(twitId);
        if (twit.getRetwitUser().contains(user)) {
            twit.getRetwitUser().remove(user);
        } else {
            twit.getRetwitUser().add(user);
        }
        return twitRepository.save(twit);
    }

    @Override
    public Twit findById(Long twitId) throws TwitException {
        return twitRepository.findById(twitId)
                .orElseThrow(() -> new TwitException("Twit not found with id: " + twitId));
    }

    @Override
    public void deleteTwitById(Long twitId, Long userId) throws UserException, TwitException {
        Twit twit = findById(twitId);

        if (!twit.getUser().getId().equals(userId)) {
            throw new UserException("User is not authorized to delete this Twit");
        }
        twitRepository.delete(twit);
    }

    @Override
    public Twit removeFromRetwit(Long twitId, User user) throws UserException, TwitException {
        Twit twit = findById(twitId);
        if (!twit.getRetwitUser().contains(user)) {
            throw new UserException("User has not retweeted this Twit");
        }
        twit.getRetwitUser().remove(user);
        return twitRepository.save(twit);
    }

    @Override
    public Twit createdReply(TwitReplyRequest req, User user) throws TwitException {
        Twit parentTwit = findById(req.getTwitId());

        // Tạo reply mới
        Twit reply = new Twit();
        reply.setContent(req.getContent());
        reply.setCreatedAt(LocalDateTime.now());
        reply.setImage(req.getImage());
        reply.setUser(user);
        reply.setReply(true);
        reply.setTwit(false);
        reply.setReplyFor(parentTwit);

        // Lưu reply mới vào DB
        Twit savedReply = twitRepository.save(reply);

        // Cập nhật danh sách reply của bài đăng gốc
        parentTwit.getReplyTwits().add(savedReply);
        twitRepository.save(parentTwit);

        // Trả về reply mới được tạo (đã lưu)
        return savedReply;
    }

    @Override
    public TwitDto replyTwitAndNotify(TwitReplyRequest req, String jwt) throws TwitException, UserException {
        // 1. Lấy user từ JWT thông qua UserService
        User user = userService.findUserProfileByJwt(jwt);

        // 2. Tạo reply mới bằng cách gọi createdReply (đã được định nghĩa ở trên)
        Twit savedReply = createdReply(req, user);

        // 3. Lấy bài đăng gốc mà reply này trả lời
        Twit parentTwit = savedReply.getReplyFor();
        if (parentTwit != null) {
            Long twitOwnerId = parentTwit.getUser().getId();
            // 4. Nếu người reply khác chủ của bài đăng gốc, gửi thông báo comment
            if (!user.getId().equals(twitOwnerId)) {
                notificationService.handleCommentAction(parentTwit.getId(), twitOwnerId, user.getId(), req.getContent());
            }
        }

        // 5. Chuyển đổi reply (savedReply) sang TwitDto và trả về
        return TwitDtoMapper.toTwitDto(savedReply, user);
    }


    @Override
    public List<Twit> getUserTwit(User user) {
        return twitRepository.findByRetwitUserContainingOrUserIdAndIsTwitTrueOrderByCreatedAtDesc(user, user.getId());
    }

    @Override
    public List<Twit> findByLikesContainsUser(User user) {
        return twitRepository.findByLikesUserId(user.getId());
    }



}
