package com.revature.RevPlay.service;

import com.revature.RevPlay.Enum.Visibility;
import com.revature.RevPlay.dto.response.*;
import com.revature.RevPlay.repository.AlbumRepository;
import com.revature.RevPlay.repository.ArtistRepository;
import com.revature.RevPlay.repository.PlaylistRepository;
import com.revature.RevPlay.repository.SongRepository;
import com.revature.RevPlay.transformer.AlbumTransformer;
import com.revature.RevPlay.transformer.ArtistTransformer;
import com.revature.RevPlay.transformer.PlaylistTransformer;
import com.revature.RevPlay.transformer.SongTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final PlaylistRepository playlistRepository;

    public SearchResponse search(String q, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<SongResponse> songs = songRepository
                .searchPublicSongs(q, Visibility.PUBLIC, pageable)
                .map(SongTransformer::songToSongResponse);

        Page<ArtistResponse> artists = artistRepository
                .findByArtistNameContainingIgnoreCase(q, pageable)
                .map(ArtistTransformer::artistToArtistResponse);

        Page<AlbumResponse> albums = albumRepository
                .findByAlbumNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(q, q, pageable)
                .map(AlbumTransformer::albumToAlbumResponse);

        Page<PlaylistResponse> playlists = playlistRepository
                .findByIsPublicTrueAndNameContainingIgnoreCase(q, pageable)
                .map(PlaylistTransformer::playlistToPlaylistResponse);

        return SearchResponse.builder()
                .query(q)
                .songs(songs)
                .artists(artists)
                .albums(albums)
                .playlists(playlists)
                .build();
    }
}
