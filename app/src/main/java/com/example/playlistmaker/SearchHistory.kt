package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.items.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("search_history", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val maxHistorySize = 10

    fun addTrack(track: Track) {
        val history = getHistory().toMutableList()
        history.removeAll { it.trackId == track.trackId }
        history.add(0, track)
        val trimmedHistory = if (history.size > maxHistorySize) history.subList(0, maxHistorySize) else history
        saveHistory(trimmedHistory)
    }


    fun getHistory(): List<Track> {
        val json = sharedPreferences.getString("history", null)
        return try {
            if (json != null) {
                val type = object : TypeToken<List<Track>>() {}.type
                gson.fromJson(json, type)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun clearHistory() {
        sharedPreferences.edit().remove("history").apply()
    }

    private fun saveHistory(history: List<Track>) {
        val json = gson.toJson(history)
        sharedPreferences.edit().putString("history", json).apply()
    }
}