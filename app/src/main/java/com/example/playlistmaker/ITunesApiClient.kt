package com.example.playlistmaker

import com.example.playlistmaker.items.ITunesApiService
import com.example.playlistmaker.items.ITunesResponse
import com.example.playlistmaker.items.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ITunesApiClient {

    private val iTunesApiService = createITunesApiService()

    fun searchSongs(term: String, callback: (List<Track>?, Throwable?) -> Unit) {
        iTunesApiService.searchSongs(term).enqueue(object : Callback<ITunesResponse> {
            override fun onResponse(call: Call<ITunesResponse>, response: Response<ITunesResponse>) {
                if (response.isSuccessful) {
                    val tracks = response.body()?.results?.map {
                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                        )
                    } ?: emptyList()
                    callback(tracks, null)
                } else {
                    callback(null, RuntimeException("API call failed"))
                }
            }

            override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    private fun createITunesApiService(): ITunesApiService {
        return Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }
}