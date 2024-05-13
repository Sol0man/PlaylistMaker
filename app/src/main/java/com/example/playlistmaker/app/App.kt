package com.example.playlistmaker.app

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

private const val SETTINGS_PREFERENCES = "settings_preferences"
private const val NIGHT_MODE_ON = "night_mode_on"

class App: Application() {

    private var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        sharedPrefs = getSharedPreferences(SETTINGS_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(NIGHT_MODE_ON, false)
    }
    fun switchTheme(darkThemeEnabled: Boolean){
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if(darkThemeEnabled){
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}