package com.example.playlistmaker.items

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {
    @GET("/search?entity=song")
    fun searchSongs(
        @Query("term", encoded = false) term: String,
    ): Call<ITunesResponse>
}