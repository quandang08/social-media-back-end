package com.socialmedia.backend.service;

import com.socialmedia.backend.exception.TwitException;
import com.socialmedia.backend.exception.UserException;
import com.socialmedia.backend.model.Twit;
import com.socialmedia.backend.model.User;
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
        Twit twit = findById(twitId);
        if(twit.getRetwitUser().contains(user)){
            twit.getRetwitUser().remove(user);
        }else{
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
        Twit replyFor =findById(req.getTwitId());
        Twit twit = new Twit();

        twit.setContent(req.getContent());
        twit.setCreatedAt(LocalDateTime.now());
        twit.setImage(req.getImage());
        twit.setUser(user);

        twit.setReply(true);
        twit.setTwit(false);
        twit.setReplyFor(replyFor);
        Twit savedReply = twitRepository.save(twit);

        twit.getReplyTwits().add(savedReply);
        twitRepository.save(replyFor);

        return replyFor;
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
