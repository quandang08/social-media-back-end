package com.socialmedia.backend.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthResponse {
    private String jwt;
    private boolean status;

    public AuthResponse(String jwt, boolean status) {
        this.jwt = jwt;
        this.status = status;
    }

}
