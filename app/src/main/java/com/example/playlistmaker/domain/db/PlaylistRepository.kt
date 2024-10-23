package com.example.playlistmaker.domain.db

import com.example.playlistmaker.data.media.entity.TrackInPlaylistEntity
import com.example.playlistmaker.domain.playlist.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun insertPlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun updateTracksCount(playlistId: Int, tracksCount: Int)

    suspend fun insertTrack(track: TrackInPlaylistEntity)

}