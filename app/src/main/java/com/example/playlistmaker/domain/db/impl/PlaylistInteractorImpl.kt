package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.playlist.Playlist
import com.example.playlistmaker.domain.search.model.Track
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

    override suspend fun updateTracksCount(playlistId: Int, tracksCount: Int, tracksId: String) {
        playlistRepository.updateTracksCount(playlistId, tracksCount, tracksId)
    }

    override suspend fun insertTrackInPlaylist(track: Track) {
        playlistRepository.insertTrackInPlaylist(track)
    }

}