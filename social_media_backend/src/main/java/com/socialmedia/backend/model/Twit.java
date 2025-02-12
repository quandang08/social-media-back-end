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
    private User user;

    private String content;
    private String image;
    private String video;

    @OneToMany(mappedBy = "twit", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "replyFor", cascade = CascadeType.ALL)
    private List<Twit> replyTwits = new ArrayList<>();

    @ManyToOne
    private Twit replyFor;

    private boolean reply;
    private boolean twit;
}
