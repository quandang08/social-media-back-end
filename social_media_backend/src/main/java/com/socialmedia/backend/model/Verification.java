package com.socialmedia.backend.model;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class Verification {
    private boolean status = false;
    private LocalDateTime started;
    private LocalDateTime endsAt;
    private String planType;

    // Getter và Setter
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDateTime getStarted() {
        return started;
    }

    public void setStarted(LocalDateTime started) {
        this.started = started;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(LocalDateTime endsAt) {
        this.endsAt = endsAt;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }
}
