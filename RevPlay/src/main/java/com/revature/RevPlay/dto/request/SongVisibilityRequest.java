package com.revature.RevPlay.dto.request;

import com.revature.RevPlay.Enum.Visibility;
import lombok.Data;

@Data
public class SongVisibilityRequest {
    private Visibility visibility; // PUBLIC / UNLISTED
}
