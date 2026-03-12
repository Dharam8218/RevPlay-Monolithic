package com.revature.RevPlay.repository;

import com.revature.RevPlay.dto.response.TopListenerResponse;
import com.revature.RevPlay.dto.response.TrendResponse;
import com.revature.RevPlay.model.ListeningHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListeningHistoryRepository extends JpaRepository<ListeningHistory, Long> {

    Page<ListeningHistory> findByUserIdOrderByPlayedAtDesc(Long userId, Pageable pageable);

    Optional<ListeningHistory> findByUser_IdAndSong_Id(Long userId, Long songId);

    List<ListeningHistory> findTop50ByUserIdOrderByPlayedAtDesc(Long userId);

    void deleteByUserId(Long userId);

    @Query("""
            SELECT new com.revature.RevPlay.dto.response.TrendResponse(
                CONCAT(
                    CAST(YEAR(h.playedAt) AS string), '-',
                    LPAD(CAST(MONTH(h.playedAt) AS string), 2, '0'), '-',
                    LPAD(CAST(DAY(h.playedAt) AS string), 2, '0')
                ),
                COUNT(h)
            )
            FROM ListeningHistory h
            WHERE h.song.artist.id = :artistId
            GROUP BY 
                CONCAT(
                    CAST(YEAR(h.playedAt) AS string), '-',
                    LPAD(CAST(MONTH(h.playedAt) AS string), 2, '0'), '-',
                    LPAD(CAST(DAY(h.playedAt) AS string), 2, '0')
                )
            ORDER BY 
                CONCAT(
                    CAST(YEAR(h.playedAt) AS string), '-',
                    LPAD(CAST(MONTH(h.playedAt) AS string), 2, '0'), '-',
                    LPAD(CAST(DAY(h.playedAt) AS string), 2, '0')
                )
            """)
    List<TrendResponse> getDailyTrends(@Param("artistId") Long artistId);

    @Query("""
            SELECT new com.revature.RevPlay.dto.response.TrendResponse(
                CONCAT(
                    CAST(YEAR(h.playedAt) AS string),
                    '-W',
                    LPAD(CAST(WEEK(h.playedAt) AS string), 2, '0')
                ),
                COUNT(h)
            )
            FROM ListeningHistory h
            WHERE h.song.artist.id = :artistId
            GROUP BY 
                CONCAT(
                    CAST(YEAR(h.playedAt) AS string),
                    '-W',
                    LPAD(CAST(WEEK(h.playedAt) AS string), 2, '0')
                )
            ORDER BY 
                CONCAT(
                    CAST(YEAR(h.playedAt) AS string),
                    '-W',
                    LPAD(CAST(WEEK(h.playedAt) AS string), 2, '0')
                )
            """)
    List<TrendResponse> getWeeklyTrends(@Param("artistId") Long artistId);

    @Query("""
            SELECT new com.revature.RevPlay.dto.response.TrendResponse(
                CONCAT(
                    CAST(YEAR(h.playedAt) AS string), '-',
                    LPAD(CAST(MONTH(h.playedAt) AS string), 2, '0')
                ),
                COUNT(h)
            )
            FROM ListeningHistory h
            WHERE h.song.artist.id = :artistId
            GROUP BY 
                CONCAT(
                    CAST(YEAR(h.playedAt) AS string), '-',
                    LPAD(CAST(MONTH(h.playedAt) AS string), 2, '0')
                )
            ORDER BY 
                CONCAT(
                    CAST(YEAR(h.playedAt) AS string), '-',
                    LPAD(CAST(MONTH(h.playedAt) AS string), 2, '0')
                )
            """)
    List<TrendResponse> getMonthlyTrends(@Param("artistId") Long artistId);

    @Query("""
            SELECT new com.revature.RevPlay.dto.response.TopListenerResponse(
                u.id,
                u.username,
                u.displayName,
                COUNT(lh)
            )
            FROM ListeningHistory lh
            JOIN lh.user u
            JOIN lh.song s
            WHERE s.artist.id = :artistId
            GROUP BY u.id, u.username, u.displayName
            ORDER BY COUNT(lh) DESC
            """)
    Page<TopListenerResponse> findTopListenersByArtist(
            @Param("artistId") Long artistId,
            Pageable pageable
    );
}

