package com.revature.RevPlay.service;

import com.revature.RevPlay.Enum.RepeatMode;
import com.revature.RevPlay.Enum.Visibility;
import com.revature.RevPlay.dto.response.NowPlayingResponse;
import com.revature.RevPlay.dto.response.QueueSongResponse;
import com.revature.RevPlay.exception.SongNotFoundException;
import com.revature.RevPlay.exception.UserNotFoundException;
import com.revature.RevPlay.model.PlaybackSession;
import com.revature.RevPlay.model.QueueItem;
import com.revature.RevPlay.model.Song;
import com.revature.RevPlay.model.User;
import com.revature.RevPlay.repository.PlaybackSessionRepository;
import com.revature.RevPlay.repository.QueueItemRepository;
import com.revature.RevPlay.repository.SongRepository;
import com.revature.RevPlay.repository.UserRepository;
import com.revature.RevPlay.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PlaybackService {

    private final PlaybackSessionRepository playbackSessionRepository;
    private final QueueItemRepository queueItemRepository;
    private final SongRepository songRepository;
    private final UserRepository userRepository;
    private final ListeningHistoryService listeningHistoryService;

    public PlaybackSession getOrCreateSession() {
        String username = SecurityUtils.getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return playbackSessionRepository.findByUserId(user.getId()).orElseGet(() -> {
            User user2 = userRepository.findById(user.getId())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            PlaybackSession s = PlaybackSession.builder().user(user2).build();
            return playbackSessionRepository.save(s);
        });
    }

    @Transactional
    public NowPlayingResponse play(Long songId) {

        PlaybackSession session = getOrCreateSession();

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));

        // enforce visibility
        if (song.getVisibility() == Visibility.UNLISTED) {
            throw new RuntimeException("Song is not public");
        }

        session.setCurrentSongId(songId);
        session.setPositionSec(0);

        // increment play count
        song.setPlayCount(song.getPlayCount() + 1);

        // TODO: create listening history row here (we’ll plug it into your History module)
        listeningHistoryService.recordPlay(songId);

        playbackSessionRepository.save(session);
        songRepository.save(song);

        return buildNowPlaying(session);
    }

    public NowPlayingResponse getNowPlaying() {
        PlaybackSession session = getOrCreateSession();
        return buildNowPlaying(session);
    }

    @Transactional
    public NowPlayingResponse setQueue(List<Long> songIds, Integer startIndex) {

        PlaybackSession session = getOrCreateSession();

        queueItemRepository.deleteBySessionId(session.getId());

        int pos = 0;
        for (Long id : songIds) {
            queueItemRepository.save(QueueItem.builder()
                    .session(session)
                    .songId(id)
                    .position(pos++)
                    .build());
        }

        if (startIndex != null) session.setQueueIndex(startIndex);
        playbackSessionRepository.save(session);
        return buildNowPlaying(session);
    }

    @Transactional
    public NowPlayingResponse next() {
        PlaybackSession session = getOrCreateSession();
        List<QueueItem> queue = queueItemRepository.findBySessionIdOrderByPositionAsc(session.getId());

        if (queue.isEmpty()) throw new RuntimeException("Queue is empty");

        int current = session.getQueueIndex() == null ? 0 : session.getQueueIndex();
        int size = queue.size();

        if (session.getRepeatMode() == RepeatMode.ONE) {
            // keep same song
            return buildNowPlaying(session);
        }

        int nextIndex;
        if (session.isShuffle()) {
            nextIndex = (int) (Math.random() * size);
        } else {
            nextIndex = current + 1;
            if (nextIndex >= size) {
                if (session.getRepeatMode() == RepeatMode.ALL) nextIndex = 0;
                else nextIndex = size - 1; // stop at end
            }
        }

        session.setQueueIndex(nextIndex);
        session.setCurrentSongId(queue.get(nextIndex).getSongId());
        session.setPositionSec(0);

        // increment playCount when switching
        Song song = songRepository.findById(session.getCurrentSongId())
                .orElseThrow(() -> new RuntimeException("Song not found"));
        song.setPlayCount(song.getPlayCount() + 1);

        playbackSessionRepository.save(session);
        songRepository.save(song);

        return buildNowPlaying(session);
    }

    @Transactional
    public NowPlayingResponse previous() {
        PlaybackSession session = getOrCreateSession();
        List<QueueItem> queue = queueItemRepository.findBySessionIdOrderByPositionAsc(session.getId());
        if (queue.isEmpty()) throw new RuntimeException("Queue is empty");

        int current = session.getQueueIndex() == null ? 0 : session.getQueueIndex();
        int prevIndex = Math.max(current - 1, 0);

        session.setQueueIndex(prevIndex);
        session.setCurrentSongId(queue.get(prevIndex).getSongId());
        session.setPositionSec(0);
        playbackSessionRepository.save(session);

        return buildNowPlaying(session);
    }

    @Transactional
    public NowPlayingResponse setShuffle(boolean shuffle) {
        PlaybackSession session = getOrCreateSession();
        session.setShuffle(shuffle);
        playbackSessionRepository.save(session);
        return buildNowPlaying(session);
    }

    @Transactional
    public NowPlayingResponse setRepeat(RepeatMode mode) {
        PlaybackSession session = getOrCreateSession();
        session.setRepeatMode(mode);
        playbackSessionRepository.save(session);
        return buildNowPlaying(session);
    }

    @Transactional
    public void updateProgress(Long songId, Integer positionSec) {
        PlaybackSession session = getOrCreateSession();
        if (session.getCurrentSongId() != null && session.getCurrentSongId().equals(songId)) {
            session.setPositionSec(positionSec);
            playbackSessionRepository.save(session);
        }
    }

    public NowPlayingResponse buildNowPlaying(PlaybackSession session) {
        Song song = null;
        if (session.getCurrentSongId() != null) {
            song = songRepository.findById(session.getCurrentSongId()).orElse(null);
        }

        List<QueueItem> items = queueItemRepository.findBySessionIdOrderByPositionAsc(session.getId());
        List<QueueSongResponse> queueDtos = items.stream().map(q -> {
            Song s = songRepository.findById(q.getSongId()).orElse(null);
            if (s == null) return null;
            return QueueSongResponse.builder()
                    .songId(s.getId())
                    .title(s.getTitle())
                    .artistName(s.getArtist() != null ? s.getArtist().getArtistName() : null)
                    .duration(s.getDuration())
                    .coverImageUrl(s.getCoverImageUrl())
                    .build();
        }).filter(Objects::nonNull).toList();

        return NowPlayingResponse.builder()
                .songId(song != null ? song.getId() : null)
                .title(song != null ? song.getTitle() : null)
                .audioUrl(song != null ? song.getAudioUrl() : null)
                .coverImageUrl(song != null ? song.getCoverImageUrl() : null)
                .artistName(song != null && song.getArtist() != null ? song.getArtist().getArtistName() : null)
                .albumName(song != null && song.getAlbum() != null ? song.getAlbum().getAlbumName() : null)
                .durationSec(song != null ? song.getDuration() : null)
                .positionSec(session.getPositionSec())
                .shuffle(session.isShuffle())
                .repeatMode(session.getRepeatMode())
                .queue(queueDtos)
                .queueIndex(session.getQueueIndex())
                .build();
    }

}
