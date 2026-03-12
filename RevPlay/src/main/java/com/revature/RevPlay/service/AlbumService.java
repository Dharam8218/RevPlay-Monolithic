package com.revature.RevPlay.service;

import com.revature.RevPlay.dto.request.AlbumRequest;
import com.revature.RevPlay.dto.request.AlbumUpdateRequest;
import com.revature.RevPlay.dto.response.AlbumResponse;
import com.revature.RevPlay.exception.AlbumNotFoundException;
import com.revature.RevPlay.exception.ArtistNotFoundException;
import com.revature.RevPlay.exception.SongNotFoundException;
import com.revature.RevPlay.exception.UserNotFoundException;
import com.revature.RevPlay.model.Album;
import com.revature.RevPlay.model.Artist;
import com.revature.RevPlay.model.Song;
import com.revature.RevPlay.model.User;
import com.revature.RevPlay.repository.AlbumRepository;
import com.revature.RevPlay.repository.ArtistRepository;
import com.revature.RevPlay.repository.SongRepository;
import com.revature.RevPlay.repository.UserRepository;
import com.revature.RevPlay.transformer.AlbumTransformer;
import com.revature.RevPlay.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final CloudinaryService cloudinaryService;
    private final SongRepository songRepository;
    private final UserRepository userRepository;

    private Long getArtistId() {
        String username = SecurityUtils.getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return user.getArtist() != null ? user.getArtist().getId() : null;
    }


    public AlbumResponse createAlbum(AlbumRequest request, MultipartFile coverArt) {

        Artist artist = artistRepository.findById(Objects.requireNonNull(getArtistId()))
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found"));
        String coverUrl = null;
        if (coverArt != null && !coverArt.isEmpty()) {
            coverUrl = cloudinaryService.uploadFile(coverArt, "album_covers");
        }
        Album saved = albumRepository.save(AlbumTransformer.albumRequestToAlbum(request, coverUrl, artist));
        return AlbumTransformer.albumToAlbumResponse(saved);
    }

    @Transactional
    public void addSongToAlbum(Long albumId, Long songId) {

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException("Album not found"));

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));

        // SECURITY CHECK
        if (!album.getArtist().getId().equals(getArtistId()) ||
                !song.getArtist().getId().equals(getArtistId())) {
            throw new RuntimeException("You can only manage your own songs/albums");
        }
        song.setAlbum(album);
        songRepository.save(song);
    }

    @Transactional
    public void removeSongFromAlbum(Long albumId, Long songId) {

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException("Album not found"));

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));

        if (!album.getArtist().getId().equals(getArtistId()) ||
                !song.getArtist().getId().equals(getArtistId())) {
            throw new RuntimeException("Unauthorized");
        }

        if (!song.getAlbum().getId().equals(albumId)) {
            throw new RuntimeException("Song does not belong to this album");
        }

        song.setAlbum(null);

        songRepository.save(song);
    }

    public List<AlbumResponse> getAllAlbums() {
        return albumRepository.findByArtistId(getArtistId()).stream()
                .map(AlbumTransformer::albumToAlbumResponse)
                .toList();
    }

    @Transactional
    public AlbumResponse updateAlbum(Long albumId, AlbumUpdateRequest request, MultipartFile cover) {

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException("Album not found"));

        // ownership check
        if (!album.getArtist().getId().equals(getArtistId())) {
            throw new RuntimeException("You can update only your own albums");
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            album.setAlbumName(request.getName().trim());
        }

        if (request.getDescription() != null) {
            album.setDescription(request.getDescription());
        }

        if (cover != null && !cover.isEmpty()) {
            String coverUrl = cloudinaryService.uploadFile(cover, "album_covers");
            album.setCoverArtUrl(coverUrl);
        }

        Album saved = albumRepository.save(album);

        return AlbumTransformer.albumToAlbumResponse(saved);
    }

    @Transactional
    public void deleteAlbum(Long albumId) {

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException("Album not found"));

        if (!album.getArtist().getId().equals(getArtistId())) {
            throw new RuntimeException("You can delete only your own albums");
        }

        long songsCount = songRepository.countByAlbumId(albumId);
        if (songsCount > 0) {
            throw new RuntimeException("Cannot delete album. Remove songs first.");
        }

        albumRepository.delete(album);
    }

    public List<AlbumResponse> getAllAlbum() {
        return albumRepository.findAll().stream().map(AlbumTransformer::albumToAlbumResponse).toList();
    }
}

