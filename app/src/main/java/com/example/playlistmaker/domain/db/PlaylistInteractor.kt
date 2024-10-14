package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.playlist.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun insertPlaylist(playlist: Playlist)

//    suspend fun deletePlaylist(id: Int)

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun updateTracksId(playlistId: Int, tracksId: String, tracksCount: Int)

}