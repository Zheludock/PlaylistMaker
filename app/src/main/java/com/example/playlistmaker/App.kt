package com.example.playlistmaker

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

class App : Application() {

    private var darkTheme = false
    private val sharedPreferences by lazy {
        getSharedPreferences("theme_preferences", Context.MODE_PRIVATE)
    }

    override fun onCreate() {
        super.onCreate()
        loadThemeState()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        saveThemeState(darkThemeEnabled)
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun saveThemeState(darkThemeEnabled: Boolean) {
        sharedPreferences.edit { putBoolean("dark_theme", darkThemeEnabled) }
    }

    private fun loadThemeState() {
        darkTheme = sharedPreferences.getBoolean("dark_theme", false)
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun isDarkTheme(): Boolean {
        return darkTheme
    }
}
