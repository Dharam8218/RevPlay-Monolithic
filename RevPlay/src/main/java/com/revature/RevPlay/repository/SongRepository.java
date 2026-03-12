package com.revature.RevPlay.repository;

import com.revature.RevPlay.Enum.Genre;
import com.revature.RevPlay.Enum.Visibility;
import com.revature.RevPlay.dto.response.FavoritedUserResponse;
import com.revature.RevPlay.dto.response.SongPlayCountResponse;
import com.revature.RevPlay.model.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long>, JpaSpecificationExecutor<Song> {
    List<Song> findByArtistId(Long artistId);

    boolean existsByIdAndArtistId(Long songId, Long artistId);

    long countByAlbumId(Long albumId);

    List<Song> findByVisibilityOrderByIdDesc(Visibility visibility);

    Page<Song> findByVisibility(Visibility visibility, Pageable pageable);

    List<Song> findByArtistIdAndVisibility(Long artistId, Visibility visibility);

    @Query("""
                select s from Song s
                where s.visibility = :visibility
                  and (lower(s.title) like lower(concat('%', :q, '%'))
                       or lower(s.artist.artistName) like lower(concat('%', :q, '%'))
                       or lower(s.album.albumName) like lower(concat('%', :q, '%')))
            """)
    Page<Song> searchPublicSongs(String q, Visibility visibility, Pageable pageable);

    Page<Song> findByGenreAndVisibility(
            Genre genre,
            Visibility visibility,
            Pageable pageable
    );

    Page<Song> findByArtistIdAndVisibility(
            Long artistId,
            Visibility visibility,
            Pageable pageable
    );

    Page<Song> findByAlbumIdAndVisibility(
            Long albumId,
            Visibility visibility,
            Pageable pageable
    );

    List<Song> findByAlbumIdAndVisibility(Long albumId, Visibility visibility);


    long countByArtistId(Long artistId);

    @Query("SELECT COALESCE(SUM(s.playCount),0) FROM Song s WHERE s.artist.id = :artistId")
    long getTotalPlaysByArtist(@Param("artistId") Long artistId);

    @Query("""
            SELECT COUNT(u)
            FROM Song s
            JOIN s.favoritedBy u
            WHERE s.artist.id = :artistId
            """)
    long countFavoritesForArtist(@Param("artistId") Long artistId);

    @Query("""
            SELECT new com.revature.RevPlay.dto.response.SongPlayCountResponse(
                s.id, s.title, s.playCount
            )
            FROM Song s
            WHERE s.id = :songId AND s.artist.id = :artistId
            """)
    Optional<SongPlayCountResponse> getSongPlayStats(
            @Param("songId") Long songId,
            @Param("artistId") Long artistId);


    @Query("""
            SELECT new com.revature.RevPlay.dto.response.SongPlayCountResponse(
                s.id,
                s.title,
                s.playCount
            )
            FROM Song s
            WHERE s.artist.id = :artistId
            ORDER BY s.playCount DESC
            """)
    List<SongPlayCountResponse> getSongsSortedByPopularity(
            @Param("artistId") Long artistId
    );

    @Query("""
            SELECT new com.revature.RevPlay.dto.response.SongPlayCountResponse(
                s.id,
                s.title,
                s.playCount
            )
            FROM Song s
            WHERE s.artist.id = :artistId
            ORDER BY s.playCount DESC
            """)
    Page<SongPlayCountResponse> getSongsSortedByPopularity(
            @Param("artistId") Long artistId,
            Pageable pageable
    );

    @Query("""
            SELECT new com.revature.RevPlay.dto.response.FavoritedUserResponse(
                u.id,
                u.username,
                u.displayName,
                u.profilePicture
            )
            FROM Song s
            JOIN s.favoritedBy u
            WHERE s.id = :songId
            AND s.artist.id = :artistId
            """)
    List<FavoritedUserResponse> getFavoritedUsersForSong(
            @Param("songId") Long songId,
            @Param("artistId") Long artistId
    );
}


