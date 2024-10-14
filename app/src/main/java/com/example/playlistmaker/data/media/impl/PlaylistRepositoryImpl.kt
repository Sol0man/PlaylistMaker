package com.example.playlistmaker.data.media.impl

import com.example.playlistmaker.data.converters.PlaylistDbConvertor
import com.example.playlistmaker.data.media.AppDatabase
import com.example.playlistmaker.data.media.entity.PlaylistEntity
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

//    override suspend fun deletePlaylist(id: Int) {
//        appDatabase.playlistDao().deletePlaylist(id)
//    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override suspend fun updateTracksId(playlistId: Int, tracksId: String, tracksCount: Int) {
        appDatabase.playlistDao().updateTracksId(playlistId, tracksId, tracksCount)
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>) : List<Playlist> {
        return playlists.map { playlist -> playlistDbConvertor.map(playlist) }
    }
}