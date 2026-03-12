package com.revature.RevPlay.transformer;

import com.revature.RevPlay.dto.request.ArtistRequest;
import com.revature.RevPlay.dto.request.UserRequest;
import com.revature.RevPlay.dto.response.UserResponse;
import com.revature.RevPlay.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

public class UserTransformer {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static User userRequestToUser(UserRequest userRequest) {

        return User
                .builder()
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .gender(userRequest.getGender())
                .password(passwordEncoder().encode(userRequest.getPassword()))
                .displayName(userRequest.getDisplayName())
                .bio(userRequest.getBio())
                .enabled(true)
                .roles(new HashSet<>())
                .build();
    }

    public static User userRequestToUser(ArtistRequest artistRequest) {

        return User
                .builder()
                .username(artistRequest.getUsername())
                .email(artistRequest.getEmail())
                .gender(artistRequest.getGender())
                .password(passwordEncoder().encode(artistRequest.getPassword()))
                .displayName(artistRequest.getDisplayName())
                .bio(artistRequest.getBio())
                .enabled(true)
                .roles(new HashSet<>())
                .build();
    }

    public static UserResponse userToUserResponse(User user) {
        return UserResponse
                .builder()
                .id(user.getId())
                .profilePicture(user.getProfilePicture())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .email(user.getEmail())
                .bio(user.getBio()).build();

    }
}
