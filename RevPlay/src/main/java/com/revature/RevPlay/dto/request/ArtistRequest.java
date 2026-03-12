package com.revature.RevPlay.dto.request;

import com.revature.RevPlay.Enum.Gender;
import com.revature.RevPlay.Enum.Genre;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ArtistRequest {
    private String username;
    private String email;
    private String password;
    private String artistName;
    private String bio;
    private Genre genre;
    private Gender gender;
    private String displayName;
}
