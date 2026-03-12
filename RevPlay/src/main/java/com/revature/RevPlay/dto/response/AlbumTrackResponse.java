package com.revature.RevPlay.dto.response;

import com.revature.RevPlay.Enum.Genre;
import com.revature.RevPlay.Enum.Visibility;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AlbumTrackResponse {
    private Long id;
    private String title;
    private Genre genre;
    private int duration;
    private String audioUrl;
    private String coverImageUrl;
    private LocalDate releaseDate;
    private Visibility visibility;
}
