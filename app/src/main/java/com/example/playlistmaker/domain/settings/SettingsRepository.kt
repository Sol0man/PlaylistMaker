package com.example.playlistmaker.domain.settings

interface SettingsRepository {
    fun putNightModeSettings(mode: Boolean)

    fun switchTheme(darkThemeEnabled: Boolean)

    fun getNightModeSettings(): Boolean
}