package com.socialmedia.backend.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Twit {

    // Getter and Setter for id
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    private String content;
    private String image;
    private String video;

    @OneToMany(mappedBy = "twit", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany
    private List<Twit> replyTwits = new ArrayList<>();

    @ManyToMany
    private List<User> retwitUser = new ArrayList<>();

    @ManyToOne
    private Twit replyFor;

    private boolean isReply;
    private boolean isTwit;

    private LocalDateTime createdAt;

    public void setId(Long id) {
        this.id = id;
    }

    // Getter and Setter for user
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Getter and Setter for content
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Getter and Setter for image
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // Getter and Setter for video
    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    // Getter and Setter for likes
    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    // Getter and Setter for replyTwits
    public List<Twit> getReplyTwits() {
        return replyTwits;
    }

    public void setReplyTwits(List<Twit> replyTwits) {
        this.replyTwits = replyTwits;
    }

    // Getter and Setter for retwitUser
    public List<User> getRetwitUser() {
        return retwitUser;
    }

    public void setRetwitUser(List<User> retwitUser) {
        this.retwitUser = retwitUser;
    }

    // Getter and Setter for replyFor
    public Twit getReplyFor() {
        return replyFor;
    }

    public void setReplyFor(Twit replyFor) {
        this.replyFor = replyFor;
    }

    // Getter and Setter for isReply
    public boolean isReply() {
        return isReply;
    }

    public void setReply(boolean reply) {
        isReply = reply;
    }

    // Getter and Setter for isTwit
    public boolean isTwit() {
        return isTwit;
    }

    public void setTwit(boolean twit) {
        isTwit = twit;
    }

    // Getter and Setter for createdAt
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
