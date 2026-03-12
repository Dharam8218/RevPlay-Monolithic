package com.revature.RevPlay.transformer;

import com.revature.RevPlay.dto.response.ListeningHistoryResponse;
import com.revature.RevPlay.model.ListeningHistory;
import com.revature.RevPlay.model.Song;
import com.revature.RevPlay.model.User;

import java.time.LocalDateTime;

public class ListeningHistoryTransformer {

    public static ListeningHistoryResponse listeningHistoryToListeningHistoryResponse(ListeningHistory listeningHistory) {
        return ListeningHistoryResponse.builder()
                .songId(listeningHistory.getSong().getId())
                .title(listeningHistory.getSong().getTitle())
                .artistName(listeningHistory.getSong().getArtist() != null ? listeningHistory.getSong().getArtist().getArtistName() : null)
                .coverImageUrl(listeningHistory.getSong().getCoverImageUrl())
                .playedAt(listeningHistory.getPlayedAt())
                .build();
    }

    public static ListeningHistory toListeningHistory(User user, Song song) {
        return ListeningHistory.builder()
                .user(user)
                .song(song)
                .playedAt(LocalDateTime.now())
                .build();

    }
}
