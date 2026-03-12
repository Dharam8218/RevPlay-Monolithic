package com.revature.RevPlay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserStatisticsResponse {

    private long totalPlaylists;
    private long totalFavorites;
}