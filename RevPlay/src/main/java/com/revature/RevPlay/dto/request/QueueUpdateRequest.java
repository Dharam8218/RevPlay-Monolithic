package com.revature.RevPlay.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class QueueUpdateRequest {
    private List<Long> songIds; // full replacement
    private Integer startIndex; // optional
}
