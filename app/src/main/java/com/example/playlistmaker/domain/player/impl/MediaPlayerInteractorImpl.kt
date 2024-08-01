package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.Track
import com.example.playlistmaker.domain.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.player.models.PlayerProgressStatus

class MediaPlayerInteractorImpl(
    private val playerRepository: MediaPlayerRepository
) : MediaPlayerInteractor {
    override fun preparePlayer(track: Track) {
        playerRepository.preparePlayer(track)
    }

    override fun startPlayer() {
        playerRepository.startPlayer()
    }

    override fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    override fun getPlayerProgressStatus(): PlayerProgressStatus {
        return playerRepository.getPlayerProgressStatus()
    }

    override fun getCurrentPosition(): Int {
        return playerRepository.getCurrentPosition()
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        playerRepository.setOnCompletionListener(listener)
    }

    override fun destroyPlayer() {
        playerRepository.destroyPlayer()
    }
}