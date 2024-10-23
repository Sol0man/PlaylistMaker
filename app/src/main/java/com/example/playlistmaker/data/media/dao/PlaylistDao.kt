package com.example.playlistmaker.data.media.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.media.entity.PlaylistEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)                //добавить плейлист

    @Query("SELECT * FROM playlists_table")                            //получить сущности треков
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("UPDATE playlists_table SET tracks_count = :tracksCount WHERE id = :playlistId")
    suspend fun updateTracksCount(playlistId: Int, tracksCount: Int)         //Обновить количество треков

}