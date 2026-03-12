package com.revature.RevPlay.transformer;

import com.revature.RevPlay.dto.request.SongRequest;
import com.revature.RevPlay.dto.response.SongDetailsResponse;
import com.revature.RevPlay.dto.response.SongResponse;
import com.revature.RevPlay.model.Artist;
import com.revature.RevPlay.model.Song;

public class SongTransformer {

    public static Song songRequestToSong(SongRequest songRequest, String audioUrl, String imageUrl, Artist artist){
        return Song.builder()
                .title(songRequest.getTitle())
                .genre(songRequest.getGenre())
                .duration(songRequest.getDuration())
                .audioUrl(audioUrl)
                .coverImageUrl(imageUrl)
                .releaseDate(songRequest.getReleaseDate())
                .artist(artist)
                .build();
    }

    public static SongResponse songToSongResponse(Song song){
        return SongResponse.builder()
                .id(song.getId())
                .title(song.getTitle())
                .genre(song.getGenre())
                .duration(song.getDuration())
                .audioUrl(song.getAudioUrl())
                .coverArtUrl(song.getCoverImageUrl())
                .artistName(song.getArtist().getArtistName())
                .artistId(song.getArtist().getId())
                .albumId(song.getAlbum()!=null?song.getAlbum().getId():null)
                .albumName(song.getAlbum()!=null?song.getAlbum().getAlbumName():null)
                .visibility(song.getVisibility())
                .build();
    }

    public static SongDetailsResponse songToSongDetailsResponse(Song song, SongDetailsResponse.ArtistMini artistMini, SongDetailsResponse.AlbumMini albumMini){
        return SongDetailsResponse.builder()
                .id(song.getId())
                .title(song.getTitle())
                .genre(song.getGenre())
                .duration(song.getDuration())
                .audioUrl(song.getAudioUrl())
                .coverImageUrl(song.getCoverImageUrl())
                .releaseDate(song.getReleaseDate())
                .artist(artistMini)
                .album(albumMini)
                .build();
    }

    public static SongDetailsResponse.ArtistMini songToArtistMini(Song song){
        return SongDetailsResponse.ArtistMini.builder()
                .id(song.getArtist().getId())
                .artistName(song.getArtist().getArtistName())
                .profilePicture(song.getArtist().getProfilePicture())
                .build();
    }

    public static SongDetailsResponse.AlbumMini songToAlbumMini(Song song){
        return SongDetailsResponse.AlbumMini.builder()
                .id(song.getAlbum().getId())
                .albumName(song.getAlbum().getAlbumName())
                .coverArtUrl(song.getAlbum().getCoverArtUrl())
                .releaseDate(song.getAlbum().getReleaseDate())
                .build();
    }
}
