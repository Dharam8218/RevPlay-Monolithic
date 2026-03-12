package com.revature.RevPlay.transformer;

import com.revature.RevPlay.dto.request.AlbumRequest;
import com.revature.RevPlay.dto.response.AlbumDetailsResponse;
import com.revature.RevPlay.dto.response.AlbumResponse;
import com.revature.RevPlay.dto.response.AlbumTrackResponse;
import com.revature.RevPlay.dto.response.SongResponse;
import com.revature.RevPlay.model.Album;
import com.revature.RevPlay.model.Artist;
import com.revature.RevPlay.model.Song;

import java.time.LocalDate;
import java.util.List;

public class AlbumTransformer {

    public static Album albumRequestToAlbum(AlbumRequest albumRequest, String coverUrl, Artist artist){
      return Album.builder()
              .albumName(albumRequest.getName())
              .description(albumRequest.getDescription())
              .releaseDate(LocalDate.parse(albumRequest.getReleaseDate()))
              .coverArtUrl(coverUrl)
              .artist(artist)
              .build();
    }

    public static AlbumResponse albumToAlbumResponse(Album album){

        List<SongResponse> songs = album.getSongs()
                .stream()
                .map(SongTransformer::songToSongResponse).toList();

        return AlbumResponse.builder()
                .id(album.getId())
                .name(album.getAlbumName())
                .description(album.getDescription())
                .releaseDate(album.getReleaseDate())
                .coverArtUrl(album.getCoverArtUrl())
                .artistName(album.getArtist().getArtistName())
                .songs(songs)
                .build();
    }

    public static AlbumTrackResponse songToAlbumTrackResponse(Song song){
      return AlbumTrackResponse.builder()
              .id(song.getId())
              .title(song.getTitle())
              .genre(song.getGenre())
              .duration(song.getDuration())
              .audioUrl(song.getAudioUrl())
              .coverImageUrl(song.getCoverImageUrl())
              .releaseDate(song.getReleaseDate())
              .visibility(song.getVisibility())
              .build();
    }

    public static AlbumDetailsResponse albumToAlbumDetailsResponse(Album album, List<AlbumTrackResponse> trackResponses){
        return AlbumDetailsResponse.builder()
                .id(album.getId())
                .albumName(album.getAlbumName())
                .description(album.getDescription())
                .releaseDate(album.getReleaseDate())
                .coverArtUrl(album.getCoverArtUrl())
                .artistId(album.getArtist() != null ? album.getArtist().getId() : null)
                .artistName(album.getArtist() != null ? album.getArtist().getArtistName() : null)
                .tracks(trackResponses)
                .build();
    }
}
