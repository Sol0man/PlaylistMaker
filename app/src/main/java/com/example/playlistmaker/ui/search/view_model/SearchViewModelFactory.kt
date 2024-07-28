package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator

class SearchViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            searchHistoryInteractor = Creator.provideSearchHistoryInteractor(),
            trackInteractor = Creator.provideTrackInteractor()
        ) as T
    }
}