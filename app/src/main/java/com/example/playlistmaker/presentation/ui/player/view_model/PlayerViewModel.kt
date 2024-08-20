package com.example.playlistmaker.presentation.ui.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.models.MediaPlayerStatus
import com.example.playlistmaker.domain.player.models.PlayerProgressStatus
import com.example.playlistmaker.domain.search.model.Track

class PlayerViewModel(
    val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel() {
    private val mainThreadHandler = Handler(Looper.getMainLooper())


    private val playerProgressStatus: MutableLiveData<PlayerProgressStatus> =
        MutableLiveData(updatePlayerProgressStatus())
    fun getPlayerProgressStatus(): LiveData<PlayerProgressStatus> = playerProgressStatus

    fun onCreate(track: Track) {
        mediaPlayerInteractor.preparePlayer(track)
        playerProgressStatus.value = updatePlayerProgressStatus()
    }

    private fun updatePlayerProgressStatus(): PlayerProgressStatus {
        return mediaPlayerInteractor.getPlayerProgressStatus()
    }

    fun pauseMediaPlayer() {
        mediaPlayerInteractor.pausePlayer()
    }

    fun destroyMediaPlayer() {
        mainThreadHandler.removeCallbacks(updateTimeOfPlay())
        mediaPlayerInteractor.destroyPlayer()
    }

    fun updateTimeOfPlay(): Runnable {                   //Обновленеи времени проигрования трека
        return object : Runnable {
            override fun run() {
                playerProgressStatus.value = updatePlayerProgressStatus()

                when (playerProgressStatus.value!!.mediaPlayerStatus) {
                    MediaPlayerStatus.STATE_DEFAULT,
                    MediaPlayerStatus.STATE_PREPARED,
                    MediaPlayerStatus.STATE_PAUSED,
                    MediaPlayerStatus.STATE_DEFAULT,
                    MediaPlayerStatus.STATE_ERROR -> {
                        mainThreadHandler.removeCallbacks(updateTimeOfPlay())
                    }
                    MediaPlayerStatus.STATE_PLAYING -> {
                        mainThreadHandler.postDelayed(this, UPDATE)
                    }
                }
            }
        }
    }


    fun playbackControl() {
        playerProgressStatus.value = updatePlayerProgressStatus()
        when (playerProgressStatus.value!!.mediaPlayerStatus) {
            MediaPlayerStatus.STATE_PLAYING -> {
                pausePlayer()
            }
            MediaPlayerStatus.STATE_PREPARED, MediaPlayerStatus.STATE_PAUSED -> {
                startPlayer()
            }
            MediaPlayerStatus.STATE_ERROR, MediaPlayerStatus.STATE_DEFAULT -> {
            }
        }
    }

    private fun startPlayer() {
        mediaPlayerInteractor.startPlayer()
        mainThreadHandler.post(updateTimeOfPlay())
        playerProgressStatus.value = updatePlayerProgressStatus()
    }

    private fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        playerProgressStatus.value = updatePlayerProgressStatus()
    }
    companion object {
        private const val UPDATE = 250L
    }
}