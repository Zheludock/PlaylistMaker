package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.items.Track
import com.google.gson.Gson

class SearchHistory(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("search_history", Context.MODE_PRIVATE)
    private val maxHistorySize = 10

    fun addTrack(track: Track) {
        val history = getHistory().toMutableList()

        val existingTrackIndex = history.indexOfFirst { it.trackId == track.trackId }

        if (existingTrackIndex != -1) {
            history.removeAt(existingTrackIndex)
        }

        history.add(0, track)

        if (history.size > maxHistorySize) {
            history.removeAt(history.size - 1)
        }

        saveHistory(history)
    }

    fun getHistory(): List<Track> {
        val historyJson = sharedPreferences.getString("history", null)
        return if (historyJson != null) {
            Gson().fromJson(historyJson, Array<Track>::class.java).toList()
        } else {
            emptyList()
        }
    }

    fun clearHistory() {
        sharedPreferences.edit().remove("history").apply()
    }

    private fun saveHistory(history: List<Track>) {
        val historyJson = Gson().toJson(history)
        sharedPreferences.edit().putString("history", historyJson).apply()
    }
}