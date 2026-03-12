package com.revature.RevPlay.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class AlbumResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private String coverArtUrl;
    private String artistName;

    private List<SongResponse> songs;
}
