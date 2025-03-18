package com.example.playlistmaker.items

data class ITunesResponse(
    val resultCount: Int,
    val results: List<TrackResponse>
)