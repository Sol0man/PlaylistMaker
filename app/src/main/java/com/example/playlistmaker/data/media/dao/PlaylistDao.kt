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

    @Query("UPDATE playlists_table SET tracks_count = :tracksCount, tracks_id = :tracksId WHERE playlist_id = :playlistId")
    suspend fun updateTracksCount(playlistId: Int, tracksCount: Int, tracksId: String)         //Обновить количество треков

    @Query("SELECT tracks_id FROM playlists_table WHERE playlist_id = :playlistId")                            //получить сущности треков
    suspend fun getTracksId(playlistId: Int): List<String>


}