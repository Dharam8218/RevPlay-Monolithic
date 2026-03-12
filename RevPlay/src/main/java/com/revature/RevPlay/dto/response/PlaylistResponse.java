package com.revature.RevPlay.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlaylistResponse {

    private Long id;
    private String name;
    private String description;
    private boolean isPublic;
    private String ownerUsername;
    private int totalSongs;
}
