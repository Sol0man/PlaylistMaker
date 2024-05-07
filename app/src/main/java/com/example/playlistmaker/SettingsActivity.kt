package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.app.App
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private val sharedPreferences by lazy {
        getSharedPreferences("theme_preferences", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val backButton = findViewById<ImageButton>(R.id.back_button)

        backButton.setOnClickListener {
            finish()
        }
        val shareButton = findViewById<ImageButton>(R.id.share_button)
        val supportButton = findViewById<ImageButton>(R.id.support_button)
        val agreementButton = findViewById<ImageButton>(R.id.agreement_button)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        //загрузка текущей настройки темы из SP
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        themeSwitcher.isChecked = isDarkMode

        shareButton.setOnClickListener {
            val urlShare = getString(R.string.url_share)
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, urlShare)
            sendIntent.type = "text/plain"

            startActivity(sendIntent)
        }
        supportButton.setOnClickListener {
            val emails = getString(R.string.emails_blank)
            val forDevelopers = getString(R.string.for_developer)
            val feedback = getString(R.string.feedback)

            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:")

            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emails))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, forDevelopers)
            emailIntent.putExtra(Intent.EXTRA_TEXT, feedback)

            startActivity(emailIntent)
        }
        agreementButton.setOnClickListener {
            val urlAgreement = getString(R.string.url_agreement)
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(urlAgreement))

            startActivity(browserIntent)
        }

        themeSwitcher.setOnCheckedChangeListener { _, isCheked ->
            saveThemeSetting(isCheked)              //сохранение настройки темы в SP
            applyTheme(isCheked)                    //применение темы
        }
    }
    private fun saveThemeSetting(isDarkMode: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean("dark_mode", isDarkMode)
            .apply()
    }

    private fun applyTheme(isDarkMode: Boolean) {
        val mode =
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
        recreate()                                  // Пересоздание активити для применения новой темы
    }
}