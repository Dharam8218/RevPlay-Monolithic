package com.revature.RevPlay.dto.request;

import com.revature.RevPlay.Enum.Genre;
import lombok.Data;

@Data
public class ArtistProfileRequest {
    private String artistName;
    private String bio;
    private Genre genre;
    private String instagram;
    private String twitter;
    private String youtube;
    private String spotify;
    private String website;
}