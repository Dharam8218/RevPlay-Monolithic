package com.revature.RevPlay.model;

import com.revature.RevPlay.Enum.RepeatMode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaybackSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private Long currentSongId;   // store song id (simple)
    private Integer positionSec;  // last known position

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RepeatMode repeatMode = RepeatMode.OFF;

    @Builder.Default
    private boolean shuffle = false;

    @Builder.Default
    private Integer queueIndex = 0; // current index in queue
}
