package com.example.playlistmaker.ui.settings.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    val sharingInteractor: SharingInteractor,
    val settingsInteractor: SettingsInteractor,
) : ViewModel() {
    private val _nightMode = MutableLiveData(getNightModeSettings())
    fun nightMode(): LiveData<Boolean> = _nightMode

    private fun getNightModeSettings() : Boolean {
        return settingsInteractor.getNightModeSettings()
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun writeToSupport() {
        sharingInteractor.openSupport()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun putNightModeSettings(checked: Boolean) {
        settingsInteractor.putNightModeSettings(checked)
    }

    fun switchTheme(checked: Boolean) {
        settingsInteractor.switchTheme(checked)
    }

}