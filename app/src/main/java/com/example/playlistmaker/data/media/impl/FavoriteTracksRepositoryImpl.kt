package com.example.playlistmaker.data.media.impl

import com.example.playlistmaker.data.converters.TrackDbConbertor
import com.example.playlistmaker.data.media.AppDatabase
import com.example.playlistmaker.data.media.entity.TrackEntity
import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConbertor: TrackDbConbertor
) : FavoriteTracksRepository {
    override suspend fun insertTrackToFavorite(track: Track) {
        val trackEntity = trackDbConbertor.map(track)
        appDatabase.trackDao().insertTrackToFavorite(trackEntity)
    }

    override suspend fun deleteTrackFromFavorite(track: Track) {
        val trackEntity = trackDbConbertor.map(track)
        appDatabase.trackDao().deleteTrackFromFavorite(trackEntity)
    }

    override fun getAllFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getAllFavoriteTracks()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConbertor.map(track) }
    }
}