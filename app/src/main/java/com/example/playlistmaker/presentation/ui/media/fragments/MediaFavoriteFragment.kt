package com.example.playlistmaker.presentation.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.MediaFavoriteFragmentBinding
import com.example.playlistmaker.presentation.isNightModeOn
import com.example.playlistmaker.presentation.ui.BindingFragment
import com.example.playlistmaker.presentation.ui.media.view_model.MediaFavoriteFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFavoriteFragment: BindingFragment<MediaFavoriteFragmentBinding>() {
    private val viewModel by viewModel<MediaFavoriteFragmentViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): MediaFavoriteFragmentBinding {
        return MediaFavoriteFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showErrorImage()
    }
    private fun showErrorImage() {
        if (requireContext().isNightModeOn()) {
            Glide.with(this)
                .load(R.drawable.not_found_dark)
                .into(binding.ivMessageImage)
        } else {
            Glide.with(this)
                .load(R.drawable.not_found_light)
                .into(binding.ivMessageImage)
        }
    }

    companion object {
        fun newInstance() = MediaFavoriteFragment()
    }
}