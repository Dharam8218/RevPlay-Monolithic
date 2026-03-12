package com.revature.RevPlay.service;

import com.revature.RevPlay.Enum.RoleName;
import com.revature.RevPlay.dto.request.ArtistRequest;
import com.revature.RevPlay.dto.request.UserProfileUpdateRequest;
import com.revature.RevPlay.dto.request.UserRequest;
import com.revature.RevPlay.dto.response.UserResponse;
import com.revature.RevPlay.exception.UserNotFoundException;
import com.revature.RevPlay.model.Role;
import com.revature.RevPlay.model.User;
import com.revature.RevPlay.repository.ArtistRepository;
import com.revature.RevPlay.repository.RoleRepository;
import com.revature.RevPlay.repository.UserRepository;
import com.revature.RevPlay.transformer.ArtistTransformer;
import com.revature.RevPlay.transformer.UserTransformer;
import com.revature.RevPlay.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ArtistRepository artistRepository;
    private final CloudinaryService cloudinaryService;


    public UserResponse registerUser(UserRequest request, MultipartFile profilePicture) {
        User user = UserTransformer.userRequestToUser(request);

        if (profilePicture != null && !profilePicture.isEmpty()) {
            String url = cloudinaryService.uploadFile(profilePicture, "profile_images");
            user.setProfilePicture(url);
        }
        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new RuntimeException("Role USER not found"));
        user.getRoles().add(userRole);
        return UserTransformer.userToUserResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse registerArtist(UserRequest request, MultipartFile profilePicture) {
        User user = UserTransformer.userRequestToUser(request);
        if (profilePicture != null && !profilePicture.isEmpty()) {
            String url = cloudinaryService.uploadFile(profilePicture, "profile_images");
            user.setProfilePicture(url);
        }
        Role userRole = roleRepository.findByName(RoleName.ARTIST)
                .orElseThrow(() -> new RuntimeException("Role ARTIST not found"));
        user.getRoles().add(userRole);
        User savedUser = userRepository.save(user);

        UserResponse savedUserResponse = UserTransformer.userToUserResponse(savedUser);

        artistRepository.save(ArtistTransformer.artistRequestToArtist(request, savedUser));
        return savedUserResponse;
    }

    @Transactional
    public UserResponse updateProfile(
            UserProfileUpdateRequest req,
            MultipartFile profileImage
    ) {

        String username = SecurityUtils.getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (req.getDisplayName() != null && !req.getDisplayName().isBlank()) {
            user.setDisplayName(req.getDisplayName().trim());
        }

        if (req.getBio() != null) {
            user.setBio(req.getBio());
        }

        // Cloudinary upload
        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = cloudinaryService.uploadFile(profileImage, "profile_images");
            user.setProfilePicture(imageUrl);
        }

        return UserTransformer.userToUserResponse(user);
    }

    public UserResponse getProfile() {

        String username = SecurityUtils.getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return UserTransformer.userToUserResponse(user);
    }

}

