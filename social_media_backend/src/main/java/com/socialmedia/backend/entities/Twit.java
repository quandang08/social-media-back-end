package com.socialmedia.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Twit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private User user;

    private String content;
    private String image;
    private String video;

    @OneToMany(mappedBy = "twit", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "replyFor", cascade = CascadeType.ALL)
    private List<Twit> replyTwits = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "twit_retwit_user",
            joinColumns = @JoinColumn(name = "twit_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> retwitUser = new ArrayList<>();

    @ManyToOne
    @JsonIgnore
    private Twit replyFor;

    private boolean isReply;
    private boolean isTwit;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
