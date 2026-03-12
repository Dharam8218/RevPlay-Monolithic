package com.revature.RevPlay.controller;

import com.revature.RevPlay.dto.response.UserStatisticsResponse;
import com.revature.RevPlay.service.UserStatisticsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/revplay/user")
@RequiredArgsConstructor
public class UserStatisticsController {

    private final UserStatisticsService userStatisticsService;

    private static final Logger logger =
            LoggerFactory.getLogger(UserStatisticsController.class);

    @GetMapping("/statistics")
    public ResponseEntity<UserStatisticsResponse> getMyStatistics() {
     logger.info("fetching users statistics");
        return ResponseEntity.ok(
                userStatisticsService.getUserStatistics()
        );
    }
}