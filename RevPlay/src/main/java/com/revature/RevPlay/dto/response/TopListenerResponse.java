package com.revature.RevPlay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopListenerResponse {

    private Long userId;
    private String username;
    private String displayName;
    private Long playCount;

}
