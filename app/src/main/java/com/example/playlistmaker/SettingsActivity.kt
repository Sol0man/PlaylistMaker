package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
       val backButton = findViewById<ImageButton>(R.id.back_button)

        backButton.setOnClickListener{
            finish()
        }
        val shareButton = findViewById<ImageButton>(R.id.share_button)
        val supportButton = findViewById<ImageButton>(R.id.support_button)
        val agreementButton = findViewById<ImageButton>(R.id.agreement_button)
        val darkModeSwitch = findViewById<Switch>(R.id.dark_mode)

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
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Включить темную тему
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                // Включить светлую тему
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}