package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

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

        shareButton.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/profile/android-developer/")
            sendIntent.type = "text/plain"

            startActivity(sendIntent)
        }
        supportButton.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:")

            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("адрес_студента@example.com"))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Сообщение разработчикам и разработчицам приложения Playlist Maker")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Спасибо разработчикам и разработчицам за крутое приложение!")

            startActivity(emailIntent)
        }
        agreementButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/legal/practicum_offer/"))

            startActivity(browserIntent)
        }
    }
}