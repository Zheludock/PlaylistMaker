package com.example.playlistmaker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.items.Track

class MediaViewModel : ViewModel() {
    private val _trackData = MutableLiveData<Track?>()
    val trackData: LiveData<Track?> = _trackData

    fun setTrack(track: Track?) {
        _trackData.value = track
    }

    fun getHighResImageUrl(url: String?): String? {
        return url?.let {
            if (it.endsWith("100x100bb.jpg")) it.replace("100x100bb.jpg", "512x512bb.jpg") else it
        }
    }

}