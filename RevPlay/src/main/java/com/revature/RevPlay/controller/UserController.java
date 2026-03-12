package com.revature.RevPlay.controller;

import com.revature.RevPlay.dto.request.*;
import com.revature.RevPlay.dto.response.LoginResponse;
import com.revature.RevPlay.dto.response.UserResponse;
import com.revature.RevPlay.model.User;
import com.revature.RevPlay.repository.UserRepository;
import com.revature.RevPlay.security.JwtUtils;
import com.revature.RevPlay.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/revplay")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    private static final Logger logger =
            LoggerFactory.getLogger(UserController.class);


    @PostMapping(value = "/register/user",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UserResponse> registerUser(
            @RequestParam("user") String data,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture
    ) {

        logger.info("registering user");
        UserRequest userRequest = new ObjectMapper().readValue(data, UserRequest.class);
        return ResponseEntity.ok(userService.registerUser(userRequest, profilePicture));
    }

    @PostMapping(value = "/register/artist",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> registerArtist(
            @RequestParam("user") String data,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture
    ) {
        logger.info("registering artist");
        UserRequest userRequest = new ObjectMapper().readValue(data, UserRequest.class);
        return ResponseEntity.ok(userService.registerArtist(userRequest, profilePicture));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        logger.info("stating login");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow();

        return ResponseEntity.ok(
                new LoginResponse(jwt, user.getUsername(), user.getEmail(), roles)
        );
    }

    @PutMapping(value = "/update-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> updateMyProfile(
            @RequestParam("data") String data,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage
    ) throws Exception {
        logger.info("updating user");
        UserProfileUpdateRequest request =
                new ObjectMapper().readValue(data, UserProfileUpdateRequest.class);

        return ResponseEntity.ok(
                userService.updateProfile(request, profileImage)
        );
    }

    @GetMapping("/get-profile")
    public ResponseEntity<UserResponse> getMyProfile() {
        logger.info("fetching profile");
        return ResponseEntity.ok(userService.getProfile());
    }

}