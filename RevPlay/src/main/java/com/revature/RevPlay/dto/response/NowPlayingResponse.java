package com.revature.RevPlay.dto.response;

import com.revature.RevPlay.Enum.RepeatMode;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NowPlayingResponse {
    private Long songId;
    private String title;
    private String audioUrl;
    private String coverImageUrl;
    private String artistName;
    private String albumName;

    private Integer positionSec;
    private Integer durationSec;

    private boolean shuffle;
    private RepeatMode repeatMode;

    private List<QueueSongResponse> queue;
    private Integer queueIndex;
}

