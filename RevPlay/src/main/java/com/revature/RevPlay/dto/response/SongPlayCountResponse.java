package com.revature.RevPlay.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongPlayCountResponse {

    private Long songId;
    private String title;
    private long playCount;
}