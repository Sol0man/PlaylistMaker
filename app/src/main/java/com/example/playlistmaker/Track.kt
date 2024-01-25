package com.example.playlistmaker

import java.time.Duration

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String)
