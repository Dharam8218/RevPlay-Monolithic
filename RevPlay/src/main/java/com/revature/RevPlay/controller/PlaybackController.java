package com.revature.RevPlay.controller;

import com.revature.RevPlay.Enum.RepeatMode;
import com.revature.RevPlay.dto.request.PlaybackProgressRequest;
import com.revature.RevPlay.dto.request.QueueUpdateRequest;
import com.revature.RevPlay.dto.response.NowPlayingResponse;
import com.revature.RevPlay.service.PlaybackService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/revplay/playback")
@RequiredArgsConstructor
public class PlaybackController {

    private final PlaybackService playbackService;

    private static final Logger logger =
            LoggerFactory.getLogger(PlaybackController.class);

    @PostMapping("/play/{songId}")
    public ResponseEntity<NowPlayingResponse> play(@PathVariable Long songId) {
        logger.info("playing song");
        return ResponseEntity.ok(playbackService.play(songId));
    }

    @GetMapping("/now-playing")
    public ResponseEntity<NowPlayingResponse> nowPlaying() {
        return ResponseEntity.ok(playbackService.getNowPlaying());
    }

    @PostMapping("/queue")
    public ResponseEntity<NowPlayingResponse> setQueue(@RequestBody QueueUpdateRequest req) {
        return ResponseEntity.ok(playbackService.setQueue(req.getSongIds(), req.getStartIndex()));
    }

    @PostMapping("/next")
    public ResponseEntity<NowPlayingResponse> next() {
        return ResponseEntity.ok(playbackService.next());
    }

    @PostMapping("/previous")
    public ResponseEntity<NowPlayingResponse> previous() {
        return ResponseEntity.ok(playbackService.previous());
    }

    @PutMapping("/repeat")
    public ResponseEntity<NowPlayingResponse> setRepeat(@RequestParam RepeatMode mode) {
        return ResponseEntity.ok(playbackService.setRepeat(mode));
    }

    @PutMapping("/shuffle")
    public ResponseEntity<NowPlayingResponse> setShuffle(@RequestParam boolean enabled) {
        return ResponseEntity.ok(playbackService.setShuffle(enabled));
    }

    @PutMapping("/progress")
    public ResponseEntity<Void> updateProgress(@RequestBody PlaybackProgressRequest req) {
        playbackService.updateProgress(req.getSongId(), req.getPositionSec());
        return ResponseEntity.ok().build();
    }
}
