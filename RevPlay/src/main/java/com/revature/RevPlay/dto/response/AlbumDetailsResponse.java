package com.revature.RevPlay.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class AlbumDetailsResponse {
    private Long id;
    private String albumName;
    private String description;
    private LocalDate releaseDate;
    private String coverArtUrl;

    private Long artistId;
    private String artistName;

    private List<AlbumTrackResponse> tracks;
}
