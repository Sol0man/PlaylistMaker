package com.example.playlistmaker.di

import com.example.playlistmaker.presentation.ui.media.view_model.MediaFavoriteFragmentViewModel
import com.example.playlistmaker.presentation.ui.media.view_model.MediaPlaylistsFragmentViewModel
import com.example.playlistmaker.presentation.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.presentation.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.presentation.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val activityModule = module {

    viewModel {
        PlayerViewModel(get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        MediaFavoriteFragmentViewModel()
    }

    viewModel {
        MediaPlaylistsFragmentViewModel()
    }
}