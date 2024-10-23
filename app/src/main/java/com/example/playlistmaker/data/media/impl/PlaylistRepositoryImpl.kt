package com.example.playlistmaker.data.media.impl

import com.example.playlistmaker.data.converters.PlaylistDbConvertor
import com.example.playlistmaker.data.media.AppDatabase
import com.example.playlistmaker.data.media.entity.PlaylistEntity
import com.example.playlistmaker.data.media.entity.TrackInPlaylistEntity
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.playlist.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor
): PlaylistRepository {
    override suspend fun insertPlaylist(playlist: Playlist) {
        val playlistEntity = playlistDbConvertor.map(playlist)
        appDatabase.playlistDao().insertPlaylist(playlistEntity)
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override suspend fun updateTracksCount(playlistId: Int, tracksCount: Int) {
        appDatabase.playlistDao().updateTracksCount(playlistId, tracksCount)
    }

    override suspend fun insertTrack(track: TrackInPlaylistEntity) {
        appDatabase.trackInPlaylistDao().insertTrack(track)
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>) : List<Playlist> {
        return playlists.map { playlist -> playlistDbConvertor.map(playlist) }
    }
}