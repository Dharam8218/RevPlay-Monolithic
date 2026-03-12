package com.revature.RevPlay.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.revature.RevPlay.Enum.Genre;
import com.revature.RevPlay.Enum.Visibility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genre genre;

    private int duration; // seconds

    private String audioUrl;

    private String coverImageUrl;

    private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Visibility visibility = Visibility.PUBLIC;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = "album_id", nullable = true)
    private Album album;

    @Builder.Default
    @ManyToMany(mappedBy = "songs")
    private Set<Playlist> playlists = new HashSet<>();

    private long playCount = 0;

    @ManyToMany(mappedBy = "favoriteSongs")
    @JsonIgnore
    private Set<User> favoritedBy = new HashSet<>();
}

