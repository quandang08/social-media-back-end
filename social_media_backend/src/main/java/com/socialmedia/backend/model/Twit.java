package com.socialmedia.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Twit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String content;
    private String image;
    private String video;

    @OneToMany(mappedBy = "replyFor", cascade = CascadeType.ALL)
    private List<Twit> replyTwits = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "reply_for_id")
    private Twit replyFor;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "twit_retwit_user",
            joinColumns = @JoinColumn(name = "twit_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> retweetedBy = new ArrayList<>();

    private boolean reply;
    private boolean twit;
}
