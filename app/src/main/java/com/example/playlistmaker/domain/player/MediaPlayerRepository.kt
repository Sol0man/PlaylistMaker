package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.player.models.PlayerProgressStatus

interface MediaPlayerRepository {
    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun getPlayerProgressStatus(): PlayerProgressStatus
    fun destroyPlayer()
}