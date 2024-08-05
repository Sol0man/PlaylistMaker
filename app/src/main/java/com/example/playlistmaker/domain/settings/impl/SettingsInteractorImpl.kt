package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository): SettingsInteractor {
    override fun putNightModeSettings(mode: Boolean) {
        settingsRepository.putNightModeSettings(mode)
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        settingsRepository.switchTheme(darkThemeEnabled)
    }

    override fun getNightModeSettings(): Boolean {
        return settingsRepository.getNightModeSettings()
    }
}