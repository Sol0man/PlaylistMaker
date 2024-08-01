package com.example.playlistmaker.domain.settings


interface SettingsInteractor {
    fun putNightModeSettings(mode: Boolean)

    fun switchTheme(darkThemeEnabled: Boolean)

    fun getNightModeSettings(): Boolean
}