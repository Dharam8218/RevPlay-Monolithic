package com.revature.RevPlay.dto.response;

import com.revature.RevPlay.Enum.Genre;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class SongDetailsResponse {

    private Long id;
    private String title;
    private Genre genre;
    private int duration;
    private String audioUrl;
    private String coverImageUrl;
    private LocalDate releaseDate;

    private ArtistMini artist;
    private AlbumMini album; // can be null

    @Data
    @Builder
    public static class ArtistMini {
        private Long id;
        private String artistName;
        private String profilePicture;
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
