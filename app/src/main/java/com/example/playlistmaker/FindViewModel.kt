package com.example.playlistmaker

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.items.Track

class FindViewModel : ViewModel() {

    private val _tracks = MutableLiveData<List<Track>?>()
    val tracks: LiveData<List<Track>?> get() = _tracks

    private val _state = MutableLiveData<FindState>()
    val state: LiveData<FindState> get() = _state

    private val iTunesApiClient = ITunesApiClient()
    private lateinit var searchHistory: SearchHistory

    fun initSearchHistory(context: Context) {
        searchHistory = SearchHistory(context)
    }

    fun searchSongs(term: String) {
        if (term.isEmpty()) {
            if (searchHistory.getHistory().isEmpty()) {
                _state.value = FindState.Loading
            } else {
                _state.value = FindState.Hystory
            }
            return
        }

        _state.value = FindState.Loading

        iTunesApiClient.searchSongs(term) { tracks, error ->
            if (error != null) {
                _state.value = FindState.Error
            } else if (tracks.isNullOrEmpty()) {
                _state.value = FindState.Empty
            } else {
                _tracks.value = tracks
                _state.value = FindState.Success
            }
        }
    }

    fun getSearchHistory() {
        val history = searchHistory.getHistory()
        if (history.isEmpty()) {
            _state.value = FindState.Loading
        } else {
            _tracks.value = history
            _state.value = FindState.Hystory
        }
    }

    fun clearSearchHistory() {
        searchHistory.clearHistory()
        _tracks.value = emptyList()
        _state.value = FindState.Loading
    }

    fun addTrackToHistory(track: Track) {
        searchHistory.addTrack(track)
    }

    sealed class FindState {
        object Loading : FindState()
        object Success : FindState()
        object Empty : FindState()
        object Error : FindState()
        object Hystory : FindState()
    }
}