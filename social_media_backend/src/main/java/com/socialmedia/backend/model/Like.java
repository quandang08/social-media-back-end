package com.socialmedia.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "likes")  // Đổi thành "likes"
@Data
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Twit twit;
}
