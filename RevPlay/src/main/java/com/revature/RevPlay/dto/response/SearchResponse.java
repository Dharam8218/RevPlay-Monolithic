package com.revature.RevPlay.dto.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@Builder
public class SearchResponse {
    private String query;

    private Page<SongResponse> songs;
    private Page<ArtistResponse> artists;
    private Page<AlbumResponse> albums;
    private Page<PlaylistResponse> playlists;
}
