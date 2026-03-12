package com.revature.RevPlay.dto.response;

import com.revature.RevPlay.Enum.Genre;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArtistResponse {

    private Long id;
    private String artistName;
    private Genre genre;
    private String bio;
    private String bannerImage;
    private String profilePicture;
    private String instagram;
    private String spotify;
    private String twitter;
    private String youtube;
    private String website;
}


