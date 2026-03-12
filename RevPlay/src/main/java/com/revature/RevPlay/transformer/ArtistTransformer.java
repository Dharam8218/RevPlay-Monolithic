package com.revature.RevPlay.transformer;

import com.revature.RevPlay.dto.request.ArtistProfileRequest;
import com.revature.RevPlay.dto.request.ArtistRequest;
import com.revature.RevPlay.dto.request.UserRequest;
import com.revature.RevPlay.dto.response.ArtistProfileResponse;
import com.revature.RevPlay.dto.response.ArtistResponse;
import com.revature.RevPlay.model.Album;
import com.revature.RevPlay.model.Artist;
import com.revature.RevPlay.model.Song;
import com.revature.RevPlay.model.User;

import java.util.List;

public class ArtistTransformer {

    public static Artist artistRequestToArtist(UserRequest artistRequest, User user) {
        return Artist.builder()
                .artistName(artistRequest.getUsername())
                .user(user)
                .build();
    }

    public static Artist setArtistProfile(Artist artist, User user, ArtistProfileRequest request) {

        return artist.builder()
                .id(artist.getId())
                .user(user)
                .artistName(request.getArtistName())
                .bio(request.getBio())
                .genre(request.getGenre())
                .bannerImage(artist.getBannerImage())
                .profilePicture(artist.getProfilePicture())
                .instagram(request.getInstagram())
                .twitter(request.getTwitter())
                .youtube(request.getYoutube())
                .spotify(request.getSpotify())
                .website(request.getWebsite())
                .build();
    }

    public static ArtistResponse artistToArtistResponse(Artist artist) {
        return ArtistResponse.builder()
                .id(artist.getId())
                .artistName(artist.getArtistName())
                .genre(artist.getGenre())
                .bio(artist.getBio())
                .bannerImage(artist.getBannerImage())
                .profilePicture(artist.getProfilePicture())
                .instagram(artist.getInstagram())
                .spotify(artist.getSpotify())
                .twitter(artist.getTwitter())
                .youtube(artist.getYoutube())
                .website(artist.getWebsite())
                .build();
    }

    public static ArtistProfileResponse.SongMini songToSongMini(Song song) {
        return ArtistProfileResponse.SongMini.builder()
                .id(song.getId())
                .title(song.getTitle())
                .genre(song.getGenre())
                .duration(song.getDuration())
                .coverImageUrl(song.getCoverImageUrl())
                .releaseDate(song.getReleaseDate())
                .build();
    }

    public static ArtistProfileResponse.AlbumMini albumToAlbumMini(Album album) {
        return ArtistProfileResponse.AlbumMini.builder()
                .id(album.getId())
                .albumName(album.getAlbumName())
                .coverArtUrl(album.getCoverArtUrl())
                .releaseDate(album.getReleaseDate())
                .build();
    }

    public static ArtistProfileResponse artistToArtistProfileResponse(Artist artist
            , List<ArtistProfileResponse.SongMini> songDtos, List<ArtistProfileResponse.AlbumMini> albumDtos
    ) {
        return ArtistProfileResponse.builder()
                .id(artist.getId())
                .artistName(artist.getArtistName())
                .genre(artist.getGenre())
                .bio(artist.getBio())
                .profilePicture(artist.getProfilePicture())
                .bannerImage(artist.getBannerImage())
                .instagram(artist.getInstagram())
                .twitter(artist.getTwitter())
                .spotify(artist.getSpotify())
                .youtube(artist.getYoutube())
                .website(artist.getWebsite())
                .songs(songDtos)
                .albums(albumDtos)
                .build();
    }
}
