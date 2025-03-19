package com.example.playlistmaker.items

data class TrackResponse(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
)



