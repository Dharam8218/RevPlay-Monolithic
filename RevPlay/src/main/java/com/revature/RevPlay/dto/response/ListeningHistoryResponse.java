package com.revature.RevPlay.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ListeningHistoryResponse {
    private Long songId;
    private String title;
    private String artistName;
    private String coverImageUrl;
    private LocalDateTime playedAt;
}

