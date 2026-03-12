package com.revature.RevPlay.controller;

import com.revature.RevPlay.Enum.Genre;
import com.revature.RevPlay.dto.response.AlbumDetailsResponse;
import com.revature.RevPlay.dto.response.ArtistProfileResponse;
import com.revature.RevPlay.dto.response.SearchResponse;
import com.revature.RevPlay.dto.response.SongResponse;
import com.revature.RevPlay.service.BrowseService;
import com.revature.RevPlay.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/revplay/library")
@RequiredArgsConstructor
public class LibraryController {

    private final BrowseService browseService;
    private final SearchService searchService;

    private static final Logger logger =
            LoggerFactory.getLogger(LibraryController.class);

    /*@GetMapping("/songs")
    public ResponseEntity<List<SongResponse>> browseSongs() {
        return ResponseEntity.ok(browseService.browseAllSongs());
    }*/

    @GetMapping("/songs")
    public ResponseEntity<Page<SongResponse>> browseSongs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        logger.info("browsing songs");
        return ResponseEntity.ok(browseService.browseAllSongs(page, size, sortBy, direction));
    }

    @GetMapping("/search")
    public ResponseEntity<SearchResponse> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        logger.info("searching songs");
        return ResponseEntity.ok(searchService.search(q, page, size));
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<Page<SongResponse>> browseByGenre(
            @PathVariable Genre genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        logger.info("browsing songs by genre");
        return ResponseEntity.ok(
                browseService.browseByGenre(genre, page, size)
        );
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<Page<SongResponse>> browseByArtist(
            @PathVariable Long artistId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        logger.info("browsing songs by artist");
        return ResponseEntity.ok(
                browseService.browseByArtist(artistId, page, size)
        );
    }

    @GetMapping("/album/{albumId}")
    public ResponseEntity<Page<SongResponse>> browseByAlbum(
            @PathVariable Long albumId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        logger.info("browsing songs by albums");
        return ResponseEntity.ok(
                browseService.browseByAlbum(albumId, page, size)
        );
    }

    @GetMapping("/songs/filter")
    public ResponseEntity<Page<SongResponse>> filterSongs(
            @RequestParam(required = false) Genre genre,
            @RequestParam(required = false) Long artistId,
            @RequestParam(required = false) Long albumId,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        logger.info("filtering songs");
        return ResponseEntity.ok(
                browseService.filterSongs(
                        genre, artistId, albumId, year, page, size, sortBy, direction
                )
        );
    }

    @GetMapping("/artists/{id}")
    public ResponseEntity<ArtistProfileResponse> getArtistProfile(@PathVariable Long id) {
        logger.info("fetching artist profile");
        return ResponseEntity.ok(browseService.getArtistProfile(id));
    }

    @GetMapping("/albums/{albumId}")
    public ResponseEntity<AlbumDetailsResponse> getAlbumDetails(@PathVariable Long albumId) {
        logger.info("fetching album details");
        return ResponseEntity.ok(browseService.getAlbumDetails(albumId));
    }
}
