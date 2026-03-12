package com.revature.RevPlay.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrendResponse {

    private String period;   // e.g. 2026-02-15 OR 2026-W07 OR 2026-02
    private Long playCount;
}