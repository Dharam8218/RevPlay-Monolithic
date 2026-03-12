package com.revature.RevPlay.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String displayName;
    private String email;
    private String bio;
    private String profilePicture;
}
