package com.example.playlistmaker.domain.search.model

data class TrackSearchResult(
    var results: List<Track>,
    var status: SearchStatus
)
