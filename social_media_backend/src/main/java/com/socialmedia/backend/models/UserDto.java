package com.socialmedia.backend.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long id;
    private String fullName;
    private String email;
    private String image;
    private String location;
    private String website;
    private String birthDate;
    private String mobile;
    private String backgroundImage;
    private String bio;
    private boolean req_user;
    private boolean login_with_google;

    private List<Long> followers = new ArrayList<>();
    private List<Long> following = new ArrayList<>();

    private boolean followed;
    private boolean isVerified;

    public UserDto(Long id, String fullName, String email, String image) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.image = image;
    }

}
