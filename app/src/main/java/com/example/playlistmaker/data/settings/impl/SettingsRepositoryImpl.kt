package com.example.playlistmaker.data.settings.impl

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.app.App
import com.example.playlistmaker.domain.settings.SettingsRepository

class SettingsRepositoryImpl(val app: App): SettingsRepository {
    private var sharedPreferences: SharedPreferences = app.getSharedPreferences(SETTINGS_PREFERENCES, MODE_PRIVATE)
    private var darkTheme = getNightModeSettings()
    override fun putNightModeSettings(mode: Boolean) {
        sharedPreferences.edit()
            .putBoolean(NIGH_MODE_ON, mode)
            .apply()
    }

    override fun getNightModeSettings(): Boolean {
        return sharedPreferences.getBoolean(NIGH_MODE_ON, false)
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
    companion object {
        private const val SETTINGS_PREFERENCES = "settings_preferences"
        private const val NIGH_MODE_ON = "night_mode_on"
    }
}