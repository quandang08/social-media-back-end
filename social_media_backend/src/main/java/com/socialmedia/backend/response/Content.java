package com.socialmedia.backend.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Content {
    private String role;
    private List<Part> parts;
}
