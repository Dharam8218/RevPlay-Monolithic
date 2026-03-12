package com.revature.RevPlay.repository;

import com.revature.RevPlay.model.Playlist;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    Page<Playlist> findByIsPublicTrueAndNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

    List<Playlist> findByUserUsernameOrderByCreatedAtDesc(String username);

    Optional<Playlist> findByIdAndUser_Id(Long playlistId, Long userId);

    Page<Playlist> findByUserId(Long userId, Pageable pageable);

    Page<Playlist> findByIsPublicTrueAndUser_IdNot(Long userId, Pageable pageable);

    @Query("""
                SELECT DISTINCT p FROM Playlist p
                LEFT JOIN FETCH p.songs
                WHERE p.id = :id
            """)
    Optional<Playlist> findByIdWithSongs(@Param("id") Long id);

    long countByUserId(Long userId);
}
