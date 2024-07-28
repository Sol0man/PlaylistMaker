package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.domain.player.MediaPlayerInteractor

class PlayerViewModelFactory (
    val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlayerViewModel(mediaPlayerInteractor = mediaPlayerInteractor) as T
    }
}