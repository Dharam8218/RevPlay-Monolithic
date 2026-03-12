package com.revature.RevPlay.repository;

import com.revature.RevPlay.model.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByArtistId(Long artistId);
    boolean existsByIdAndArtistId(Long albumId, Long artistId);

    Page<Album> findByAlbumNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String albumName, String description, Pageable pageable
    );

    long countByArtistId(Long artistId);

}
