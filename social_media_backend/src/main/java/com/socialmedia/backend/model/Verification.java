package com.socialmedia.backend.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Embeddable
public class Verification {
    // Getter và Setter
    private boolean status = false;
    private LocalDateTime started;
    private LocalDateTime endsAt;
    private String planType;

}
