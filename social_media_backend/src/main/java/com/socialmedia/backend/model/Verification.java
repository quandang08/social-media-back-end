package com.socialmedia.backend.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Embeddable
public class Verification {
    private boolean status = false;
    private LocalDateTime started;
    private LocalDateTime endsAt;
    private String planType;
}
