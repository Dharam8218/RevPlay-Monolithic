package com.revature.RevPlay.controller;

import com.revature.RevPlay.dto.response.SongResponse;
import com.revature.RevPlay.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/revplay/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    private static final Logger logger =
            LoggerFactory.getLogger(FavoriteController.class);

    @PostMapping("/{songId}")
    public ResponseEntity<String> add(@PathVariable Long songId) {
        logger.info("adding song to favourite");
        favoriteService.addToFavorites(songId);
        return ResponseEntity.ok("Added to favorites");
    }

    @DeleteMapping("/{songId}")
    public ResponseEntity<String> remove(@PathVariable Long songId) {
        logger.info("removing song from favourite");
        favoriteService.removeFromFavorites(songId);
        return ResponseEntity.ok("Removed from favorites");
    }

    @GetMapping
    public ResponseEntity<List<SongResponse>> myFavorites() {
        logger.info("fetching all favorites songs of user");
        return ResponseEntity.ok(favoriteService.getMyFavorites());
    }
}

