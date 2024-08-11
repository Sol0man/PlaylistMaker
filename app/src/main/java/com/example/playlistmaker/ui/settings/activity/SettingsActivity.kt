package com.example.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nightMode: Boolean? = viewModel.nightMode.value

        binding.themeSwitcher.isChecked = nightMode ?: false

        binding.shareButton.setOnClickListener {
            viewModel.shareApp()
        }

        binding.supportButton.setOnClickListener {
            viewModel.writeToSupport()
        }

        binding.agreementButton.setOnClickListener {
            viewModel.openTerms()
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.putNightModeSettings(checked)
            viewModel.switchTheme(checked)
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}
