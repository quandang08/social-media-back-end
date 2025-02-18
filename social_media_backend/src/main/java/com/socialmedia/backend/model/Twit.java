package com.socialmedia.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Twit {

    // Getter and Setter for id
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Getter and Setter for user
    @Setter
    @Getter
    @ManyToOne
    private User user;

    // Getter and Setter for content
    @Setter
    @Getter
    private String content;
    // Getter and Setter for image
    @Setter
    @Getter
    private String image;
    // Getter and Setter for video
    @Setter
    @Getter
    private String video;

    // Getter and Setter for likes
    @Setter
    @Getter
    @OneToMany(mappedBy = "twit", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    // Getter and Setter for replyTwits
    @Setter
    @Getter
    @OneToMany
    private List<Twit> replyTwits = new ArrayList<>();

    // Getter and Setter for retwitUser
    @Setter
    @Getter
    @ManyToMany
    private List<User> retwitUser = new ArrayList<>();

    // Getter and Setter for replyFor
    @Setter
    @Getter
    @ManyToOne
    private Twit replyFor;

    private boolean isReply;
    private boolean isTwit;

    // Getter and Setter for createdAt
    @Setter
    @Getter
    private LocalDateTime createdAt;

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

}
