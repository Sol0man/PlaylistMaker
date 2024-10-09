package com.example.playlistmaker.presentation.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.MediaPlaylistsFragmentBinding
import com.example.playlistmaker.presentation.isNightModeOn
import com.example.playlistmaker.presentation.ui.BindingFragment
import com.example.playlistmaker.presentation.ui.media.view_model.MediaPlaylistsFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaPlaylistsFragment: BindingFragment<MediaPlaylistsFragmentBinding>() {

    private val viewModel = viewModel<MediaPlaylistsFragmentViewModel>()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): MediaPlaylistsFragmentBinding {
        return MediaPlaylistsFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showErrorImage()
    }

    private fun showErrorImage() {
        if (requireContext().isNightModeOn()) {
            Glide.with(this)
                .load(R.drawable.not_found_dark)
                .into(binding.ivImageError)
        } else {
            Glide.with(this)
                .load(R.drawable.not_found_light)
                .into(binding.ivImageError)
        }
    }

    companion object {
        fun newInstance() = MediaPlaylistsFragment()
    }
}