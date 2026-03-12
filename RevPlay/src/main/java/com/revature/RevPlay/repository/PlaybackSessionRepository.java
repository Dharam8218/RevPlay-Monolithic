package com.revature.RevPlay.repository;

import com.revature.RevPlay.model.PlaybackSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaybackSessionRepository extends JpaRepository<PlaybackSession, Long> {
    Optional<PlaybackSession> findByUserId(Long userId);
}
