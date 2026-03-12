package com.revature.RevPlay.dto.response;

import com.revature.RevPlay.Enum.Genre;
import com.revature.RevPlay.Enum.Visibility;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SongResponse {

    private Long id;
    private String title;
    private Genre genre;
    private int duration;
    private String audioUrl;
    private String coverArtUrl;

    private Long artistId;
    private String artistName;

    private Long albumId;
    private String albumName;

    private Visibility visibility;
}

