package com.revature.RevPlay.dto.request;

import com.revature.RevPlay.Enum.Gender;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UserRequest {
    private String username;
    private String email;
    private String password;
    private String displayName;
    private String bio;
    private Gender gender;
}
