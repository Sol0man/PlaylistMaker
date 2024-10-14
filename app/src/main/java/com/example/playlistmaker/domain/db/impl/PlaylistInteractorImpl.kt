package com.example.playlistmaker.domain.db.impl

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

//    override suspend fun deletePlaylist(id: Int) {
//        playlistRepository.deletePlaylist(id)
//    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun updateTracksId(playlistId: Int, tracksId: String, tracksCount: Int) {
        playlistRepository.updateTracksId(playlistId, tracksId, tracksCount)
    }
}