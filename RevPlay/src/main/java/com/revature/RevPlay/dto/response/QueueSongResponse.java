package com.revature.RevPlay.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueueSongResponse {

    private Long songId;
    private String title;
    private String artistName;
    private int duration;
    private String coverImageUrl;

}
