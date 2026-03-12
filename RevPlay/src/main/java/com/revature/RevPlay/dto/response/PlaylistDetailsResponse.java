package com.revature.RevPlay.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PlaylistDetailsResponse {

    private Long id;
    private String name;
    private String description;
    private boolean isPublic;
    private String ownerUsername;
    private int totalSongs;

    private List<SongResponse> songs;
}
