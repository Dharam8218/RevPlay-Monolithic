package com.revature.RevPlay.service;

import com.revature.RevPlay.Enum.Visibility;
import com.revature.RevPlay.dto.request.SongRequest;
import com.revature.RevPlay.dto.request.SongUpdateRequest;
import com.revature.RevPlay.dto.request.SongVisibilityRequest;
import com.revature.RevPlay.dto.response.SongDetailsResponse;
import com.revature.RevPlay.dto.response.SongResponse;
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
import com.revature.RevPlay.transformer.SongTransformer;
import com.revature.RevPlay.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SongService {

    private final CloudinaryService cloudinaryService;
    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;

    private Long getArtistId() {
        String username = SecurityUtils.getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return user.getArtist() != null ? user.getArtist().getId() : null;
    }


    public SongResponse uploadSong(SongRequest request,
                                   MultipartFile audioFile,
                                   MultipartFile coverImage) {

        Artist artist = artistRepository.findById(Objects.requireNonNull(getArtistId()))
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found"));

        String audioUrl = cloudinaryService.uploadFile(audioFile, "songs");
        String imageUrl = coverImage != null
                ? cloudinaryService.uploadFile(coverImage, "covers")
                : null;

        Song saved = songRepository.save(SongTransformer.songRequestToSong(request, audioUrl, imageUrl, artist));
        return SongTransformer.songToSongResponse(saved);
    }

    public List<SongResponse> getAllSongs() {
        return songRepository.findByArtistId(getArtistId()).stream()
                .map(SongTransformer::songToSongResponse)
                .toList();
    }

    @Transactional
    public SongResponse updateSong(Long songId, SongUpdateRequest request) {

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));

        // ownership check
        if (!song.getArtist().getId().equals(getArtistId())) {
            throw new RuntimeException("You can update only your own songs");
        }

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            song.setTitle(request.getTitle().trim());
        }

        if (request.getGenre() != null) {
            song.setGenre(request.getGenre());
        }

        // album change
        if (request.getAlbumId() == null) {
            song.setAlbum(null); // remove from album
        } else {
            Album album = albumRepository.findById(request.getAlbumId())
                    .orElseThrow(() -> new AlbumNotFoundException("Album not found"));

            // ensure same artist owns album
            if (!album.getArtist().getId().equals(getArtistId())) {
                throw new RuntimeException("You can add songs only to your own albums");
            }

            song.setAlbum(album);
        }
        Song saved = songRepository.save(song);
        return SongTransformer.songToSongResponse(saved);
    }

    @Transactional
    public void deleteSong(Long songId) {

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));

        if (!song.getArtist().getId().equals(getArtistId())) {
            throw new RuntimeException("You can delete only your own songs");
        }

        // Optional: remove relation so album doesn't keep reference in memory
        song.setAlbum(null);

        songRepository.delete(song);
    }

    @Transactional
    public SongResponse updateVisibility(Long songId, SongVisibilityRequest req) {

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));

        if (!song.getArtist().getId().equals(getArtistId())) {
            throw new RuntimeException("Unauthorized");
        }

        if (req.getVisibility() == null) {
            throw new RuntimeException("Visibility is required");
        }

        song.setVisibility(req.getVisibility());
        Song saved = songRepository.save(song);

        return SongTransformer.songToSongResponse(saved);
    }

    public SongDetailsResponse getSongDetails(Long songId) {

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found with id: " + songId));

        SongDetailsResponse.ArtistMini artistMini = null;
        if (song.getArtist() != null) {
            artistMini = SongTransformer.songToArtistMini(song);
        }

        SongDetailsResponse.AlbumMini albumMini = null;
        if (song.getAlbum() != null) {
            albumMini = SongTransformer.songToAlbumMini(song);
        }

        return SongTransformer.songToSongDetailsResponse(song,artistMini,albumMini);
    }

}


