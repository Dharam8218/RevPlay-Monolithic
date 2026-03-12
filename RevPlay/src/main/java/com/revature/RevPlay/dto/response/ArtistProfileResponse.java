package com.revature.RevPlay.dto.response;

import com.revature.RevPlay.Enum.Genre;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ArtistProfileResponse {

    private Long id;
    private String artistName;
    private Genre genre;
    private String bio;

    private String profilePicture;
    private String bannerImage;

    private String instagram;
    private String twitter;
    private String spotify;
    private String youtube;
    private String website;

    private List<SongMini> songs;
    private List<AlbumMini> albums;

    @Data
    @Builder
    public static class SongMini {
        private Long id;
        private String title;
        private Genre genre;
        private int duration;
        private String coverImageUrl;
        private LocalDate releaseDate;
    }

    @Data
    @Builder
    public static class AlbumMini {
        private Long id;
        private String albumName;
        private String coverArtUrl;
        private LocalDate releaseDate;
    }
}
