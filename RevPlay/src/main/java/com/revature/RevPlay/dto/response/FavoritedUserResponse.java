package com.revature.RevPlay.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoritedUserResponse {

    private Long userId;
    private String username;
    private String displayName;
    private String profilePicture;
}