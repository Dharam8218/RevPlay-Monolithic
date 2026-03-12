package com.revature.RevPlay.service;

import com.revature.RevPlay.dto.response.UserStatisticsResponse;
import com.revature.RevPlay.exception.UserNotFoundException;
import com.revature.RevPlay.model.User;
import com.revature.RevPlay.repository.PlaylistRepository;
import com.revature.RevPlay.repository.UserRepository;
import com.revature.RevPlay.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStatisticsService {

    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;


    public UserStatisticsResponse getUserStatistics() {

        String username = SecurityUtils.getCurrentUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new UserNotFoundException("User not found")
        );

        long playlistCount = playlistRepository.countByUserId(user.getId());

        long favoriteCount = userRepository.countFavoriteSongs(user.getId());

        return new UserStatisticsResponse(
                playlistCount,
                favoriteCount
        );
    }
}
