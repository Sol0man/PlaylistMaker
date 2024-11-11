package com.example.playlistmaker.data.converters

import android.icu.util.Calendar
import com.example.playlistmaker.data.media.entity.TrackInPlaylistEntity
import com.example.playlistmaker.domain.search.model.Track

class TrackInPlaylistConvertor {

    fun map(track: TrackInPlaylistEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.previewUrl,
            track.country,
            track.isFavorite
        )
    }

    fun map(track: Track): TrackInPlaylistEntity {
        return TrackInPlaylistEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.previewUrl,
            track.country,
            track.isFavorite,
            Calendar.getInstance().time.toString()
        )
    }
}