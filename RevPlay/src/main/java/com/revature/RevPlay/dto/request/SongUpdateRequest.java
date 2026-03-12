package com.revature.RevPlay.dto.request;

import com.revature.RevPlay.Enum.Genre;
import lombok.Data;

@Data
public class SongUpdateRequest {
    private String title;
    private Genre genre;
    private Long albumId; // null => remove from album
}
