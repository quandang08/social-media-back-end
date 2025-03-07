package com.socialmedia.backend.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Embeddable
public class Verification {
    private boolean status = false;
    private LocalDateTime started = LocalDateTime.now();
    private LocalDateTime endsAt = LocalDateTime.now().plusMonths(1);
    private String planType = "FREE";
}

