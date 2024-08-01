package com.example.playlistmaker.domain.player

import com.example.playlistmaker.Track
import com.example.playlistmaker.domain.player.models.PlayerProgressStatus

interface MediaPlayerInteractor {
    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun getPlayerProgressStatus(): PlayerProgressStatus
    fun getCurrentPosition() : Int
    fun setOnCompletionListener(listener: () -> Unit)
    fun destroyPlayer()
}