package com.socialmedia.backend.response;

public class AuthResponse {
    private String jwt;
    private boolean status;

    // Constructor không tham số
    public AuthResponse() {
    }

    // Constructor có tham số
    public AuthResponse(String jwt, boolean status) {
        this.jwt = jwt;
        this.status = status;
    }

    // Getter cho jwt
    public String getJwt() {
        return jwt;
    }

    // Setter cho jwt
    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    // Getter cho status
    public boolean isStatus() {
        return status;
    }

    // Setter cho status
    public void setStatus(boolean status) {
        this.status = status;
    }
}
