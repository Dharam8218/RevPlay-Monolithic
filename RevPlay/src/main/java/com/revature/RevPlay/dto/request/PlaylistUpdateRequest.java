package com.revature.RevPlay.dto.request;

import lombok.Data;

@Data
public class PlaylistUpdateRequest {
    private String name;
    private String description;
    private Boolean isPublic;
}

