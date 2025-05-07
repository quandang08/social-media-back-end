package com.socialmedia.backend.models.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@Builder
public class WrapperResponse<T> {
    int statusCode;
    T result;

    public WrapperResponse(int statusCode, T result) {
        this.statusCode = statusCode;
        this.result = result;
    }

}
