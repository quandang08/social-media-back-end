package com.socialmedia.backend.model;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String location;
    private String website;
    private LocalDate birthDate;
    private String email;
    private String password;
    private String mobile;
    private String image;
    private String backgroundImage;
    private String bio;
    private boolean reqUser;
    private boolean loginWithGoogle;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Twit> twits = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @Embedded
    private Verification verification;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_followers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private List<User> followers = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "followers")

    private List<User> following = new ArrayList<>();
}
