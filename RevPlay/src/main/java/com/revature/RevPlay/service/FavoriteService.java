package com.revature.RevPlay.service;

import com.revature.RevPlay.dto.response.SongResponse;
import com.revature.RevPlay.exception.SongNotFoundException;
import com.revature.RevPlay.exception.UserNotFoundException;
import com.revature.RevPlay.model.Song;
import com.revature.RevPlay.model.User;
import com.revature.RevPlay.repository.SongRepository;
import com.revature.RevPlay.repository.UserRepository;
import com.revature.RevPlay.transformer.SongTransformer;
import com.revature.RevPlay.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final UserRepository userRepository;
    private final SongRepository songRepository;

    @Transactional
    public void addToFavorites(Long songId) {
        String username = SecurityUtils.getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));

        user.getFavoriteSongs().add(song);
        userRepository.save(user);
    }

    @Transactional
    public void removeFromFavorites(Long songId) {
        String username = SecurityUtils.getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));

        user.getFavoriteSongs().remove(song);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<SongResponse> getMyFavorites() {
        String username = SecurityUtils.getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return user.getFavoriteSongs().stream()
                .map(SongTransformer::songToSongResponse)
                .toList();
    }

}

