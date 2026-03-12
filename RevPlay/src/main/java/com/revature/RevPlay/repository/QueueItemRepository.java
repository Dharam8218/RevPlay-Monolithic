package com.revature.RevPlay.repository;

import com.revature.RevPlay.model.QueueItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QueueItemRepository extends JpaRepository<QueueItem, Long> {
    List<QueueItem> findBySessionIdOrderByPositionAsc(Long sessionId);
    void deleteBySessionId(Long sessionId);
}
