package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.TrackSearchResult
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun searchTracks(expression: String) : Flow<TrackSearchResult>
}