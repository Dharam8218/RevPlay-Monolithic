package com.revature.RevPlay.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistDashboardResponse {

    private long totalSongs;
    private long totalPlays;
    private long totalFavorites;
    private long totalAlbums;
}