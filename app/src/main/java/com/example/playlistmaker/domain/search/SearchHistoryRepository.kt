package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.Track

interface SearchHistoryRepository {
    fun addTrack(track: Track)
    fun getTracksHistory(): ArrayList<Track>
    fun clean()
}