package com.revature.RevPlay.service;

import com.revature.RevPlay.Enum.Genre;
import com.revature.RevPlay.Enum.Visibility;
import com.revature.RevPlay.dto.response.AlbumDetailsResponse;
import com.revature.RevPlay.dto.response.AlbumTrackResponse;
import com.revature.RevPlay.dto.response.ArtistProfileResponse;
import com.revature.RevPlay.dto.response.SongResponse;
import com.revature.RevPlay.model.Album;
import com.revature.RevPlay.model.Artist;
import com.revature.RevPlay.model.Song;
import com.revature.RevPlay.repository.AlbumRepository;
import com.revature.RevPlay.repository.ArtistRepository;
import com.revature.RevPlay.repository.SongRepository;
import com.revature.RevPlay.spec.SongSpecifications;
import com.revature.RevPlay.transformer.AlbumTransformer;
import com.revature.RevPlay.transformer.ArtistTransformer;
import com.revature.RevPlay.transformer.SongTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrowseService {

    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;

    public List<SongResponse> browseAllSongs() {
        return songRepository.findByVisibilityOrderByIdDesc(Visibility.PUBLIC)
                .stream()
                .map(SongTransformer::songToSongResponse)
                .toList();
    }

    public Page<SongResponse> browseAllSongs(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return songRepository.findByVisibility(Visibility.PUBLIC, pageable)
                .map(SongTransformer::songToSongResponse);
    }


    public Page<SongResponse> browseByGenre(
            Genre genre,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        return songRepository
                .findByGenreAndVisibility(genre, Visibility.PUBLIC, pageable)
                .map(SongTransformer::songToSongResponse);
    }

    public Page<SongResponse> browseByArtist(
            Long artistId,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return songRepository
                .findByArtistIdAndVisibility(artistId, Visibility.PUBLIC, pageable)
                .map(SongTransformer::songToSongResponse);
    }

    public Page<SongResponse> browseByAlbum(
            Long albumId,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return songRepository
                .findByAlbumIdAndVisibility(albumId, Visibility.PUBLIC, pageable)
                .map(SongTransformer::songToSongResponse);
    }

    public Page<SongResponse> filterSongs(
            Genre genre,
            Long artistId,
            Long albumId,
            Integer year,
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Song> spec = Specification.where(SongSpecifications.isPublic());

        if (genre != null)
            spec = spec.and(SongSpecifications.hasGenre(genre));

        if (artistId != null)
            spec = spec.and(SongSpecifications.hasArtistId(artistId));

        if (albumId != null)
            spec = spec.and(SongSpecifications.hasAlbumId(albumId));

        if (year != null)
            spec = spec.and(SongSpecifications.hasReleaseYear(year));

        return songRepository.findAll(spec, pageable)
                .map(SongTransformer::songToSongResponse);
    }

    public ArtistProfileResponse getArtistProfile(Long artistId) {

        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artist not found with id: " + artistId));

        List<Song> songs = songRepository.findByArtistIdAndVisibility(artistId, Visibility.PUBLIC);
        List<Album> albums = albumRepository.findByArtistId(artistId);

        List<ArtistProfileResponse.SongMini> songDtos = songs.stream()
                .map(ArtistTransformer::songToSongMini)
                .toList();

        List<ArtistProfileResponse.AlbumMini> albumDtos = albums.stream()
                .map(ArtistTransformer::albumToAlbumMini)
                .toList();

        return ArtistTransformer.artistToArtistProfileResponse(artist,songDtos,albumDtos);
    }

    public AlbumDetailsResponse getAlbumDetails(Long albumId) {

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found with id " + albumId));

        // only PUBLIC songs visible to user
        List<Song> tracks = songRepository.findByAlbumIdAndVisibility(albumId, Visibility.PUBLIC);

        List<AlbumTrackResponse> trackResponses = tracks.stream()
                .map(AlbumTransformer::songToAlbumTrackResponse)
                .toList();

        return AlbumTransformer.albumToAlbumDetailsResponse(album,trackResponses);
    }



}
