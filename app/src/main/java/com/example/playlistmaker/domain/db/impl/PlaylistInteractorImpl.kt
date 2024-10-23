package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.data.media.entity.TrackInPlaylistEntity
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.playlist.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    val playlistRepository: PlaylistRepository
) : PlaylistInteractor {
    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistRepository.insertPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun updateTracksCount(playlistId: Int, tracksCount: Int) {
        playlistRepository.updateTracksCount(playlistId, tracksCount)
    }

    override suspend fun insertTrack(track: TrackInPlaylistEntity) {
        playlistRepository.insertTrack(track)
    }

}