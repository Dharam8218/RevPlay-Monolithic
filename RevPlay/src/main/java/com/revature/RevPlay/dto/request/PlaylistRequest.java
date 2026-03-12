package com.revature.RevPlay.dto.request;

import lombok.Data;
@Data
public class PlaylistRequest {
    private String name;
    private String description;
    private boolean isPublic; // true=public false=private
}

