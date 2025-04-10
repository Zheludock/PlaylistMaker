package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.res.Configuration
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
        applyTheme(darkThemeEnabled)
    }

    private fun saveThemeState(darkThemeEnabled: Boolean) {
        sharedPreferences.edit { putBoolean("dark_theme", darkThemeEnabled) }
    }

    private fun loadThemeState() {
        val isThemeSet = sharedPreferences.contains("dark_theme")

        darkTheme = if (isThemeSet) {
            sharedPreferences.getBoolean("dark_theme", false)
        } else {
            val isSystemDark = (resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            saveThemeState(isSystemDark)
            isSystemDark
        }

        applyTheme(darkTheme)
    }

    private fun applyTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun isDarkTheme(): Boolean = darkTheme
}
