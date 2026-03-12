package com.revature.RevPlay.dto.request;

import lombok.Data;

@Data
public class PlaybackProgressRequest {
    private Long songId;
    private Integer positionSec;
}
