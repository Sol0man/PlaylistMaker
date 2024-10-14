package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.media.entity.PlaylistEntity
import com.example.playlistmaker.domain.playlist.Playlist

class PlaylistDbConvertor {

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.id,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.playlistImage,
            playlist.tracksId,
            playlist.tracksCount
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.playlistImage,
            playlist.tracksId,
            playlist.tracksCount
        )
    }
}