package com.example.playlistmaker.data.media

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.media.dao.PlaylistDao
import com.example.playlistmaker.data.media.dao.TrackDao
import com.example.playlistmaker.data.media.dao.TrackInPlaylistDao
import com.example.playlistmaker.data.media.entity.PlaylistEntity
import com.example.playlistmaker.data.media.entity.TrackEntity
import com.example.playlistmaker.data.media.entity.TrackInPlaylistEntity

@Database(version = 5, entities = [TrackEntity::class, PlaylistEntity::class, TrackInPlaylistEntity::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun trackInPlaylistDao(): TrackInPlaylistDao

}