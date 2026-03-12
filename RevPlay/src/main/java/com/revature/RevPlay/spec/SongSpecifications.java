package com.revature.RevPlay.spec;

import com.revature.RevPlay.Enum.Genre;
import com.revature.RevPlay.Enum.Visibility;
import com.revature.RevPlay.model.Song;
import org.springframework.data.jpa.domain.Specification;

public class SongSpecifications {

    public static Specification<Song> isPublic() {
        return (root, query, cb) ->
                cb.equal(root.get("visibility"), Visibility.PUBLIC);
    }

    public static Specification<Song> hasGenre(Genre genre) {
        return (root, query, cb) ->
                cb.equal(root.get("genre"), genre);
    }

    public static Specification<Song> hasArtistId(Long artistId) {
        return (root, query, cb) ->
                cb.equal(root.get("artist").get("id"), artistId);
    }

    public static Specification<Song> hasAlbumId(Long albumId) {
        return (root, query, cb) ->
                cb.equal(root.get("album").get("id"), albumId);
    }

    public static Specification<Song> hasReleaseYear(Integer year) {
        return (root, query, cb) ->
                cb.equal(
                        cb.function("year", Integer.class, root.get("releaseDate")),
                        year
                );
    }
}
