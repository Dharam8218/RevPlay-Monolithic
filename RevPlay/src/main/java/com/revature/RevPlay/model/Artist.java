package com.revature.RevPlay.model;

import com.revature.RevPlay.Enum.Genre;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
@Entity
@Table(name="artists")
public class Artist{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String artistName;

    private String bio;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private String profilePicture;
    private String bannerImage;

    private String instagram;
    private String twitter;
    private String youtube;
    private String spotify;
    private String website;

    @OneToOne
    @JoinColumn(name = "user_id" ,nullable = false)
    private User user;
}
