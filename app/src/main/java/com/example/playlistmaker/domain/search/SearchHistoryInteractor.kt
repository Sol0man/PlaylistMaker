package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.Track

interface SearchHistoryInteractor {
    fun addTrack(track: Track)
    fun getTrackHistory(): ArrayList<Track>
    fun clean()
}