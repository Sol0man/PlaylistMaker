package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.TrackSearchResult

interface TrackRepository {
    fun searchTracks(expression: String): TrackSearchResult
}