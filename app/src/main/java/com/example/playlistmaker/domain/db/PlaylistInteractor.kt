package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.playlist.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun insertPlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun updateTracksCount(playlistId: Int, tracksCount: Int, tracksId: String)

    suspend fun insertTrackInPlaylist(track: Track)

}