package com.example.playlistmaker.presentation.ui.media.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.MediaFavoriteFragmentBinding
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.presentation.isNightModeOn
import com.example.playlistmaker.presentation.ui.BindingFragment
import com.example.playlistmaker.presentation.ui.media.view_model.MediaFavoriteFragmentViewModel
import com.example.playlistmaker.presentation.ui.player.activity.PlayerActivity
import com.example.playlistmaker.presentation.ui.search.common.TrackListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFavoriteFragment: BindingFragment<MediaFavoriteFragmentBinding>() {

    private lateinit var adapter: TrackListAdapter
    private lateinit var tracks: ArrayList<Track>
    private val viewModel by viewModel<MediaFavoriteFragmentViewModel>()

    private val onClick: (track: Track) -> Unit = {
        if (viewModel.clickDebounce()) {
            startPlayerActivity(it)
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): MediaFavoriteFragmentBinding {
        return MediaFavoriteFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tracks = ArrayList<Track>()
        adapter = TrackListAdapter(tracks, onClick)
        viewModel.onCreate()
        binding.rvFavoriteTracks.adapter = adapter

        viewModel.getFavoriteTracks().observe(viewLifecycleOwner) {
            showFavoriteTrackList(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    private fun showFavoriteTrackList(tracksFromDb: List<Track>) {
        if (tracksFromDb.isEmpty()) {
            binding.ivImageError.visibility = View.VISIBLE
            binding.tvMessageError.visibility = View.VISIBLE
            binding.rvFavoriteTracks.visibility = View.GONE
            showErrorImage()
            tracks.clear()
        } else {
            binding.ivImageError.visibility = View.GONE
            binding.tvMessageError.visibility = View.GONE
            binding.rvFavoriteTracks.visibility = View.VISIBLE
            tracks.clear()
            tracks.addAll(tracksFromDb)
            adapter.notifyDataSetChanged()
        }
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

    private fun startPlayerActivity(track: Track) {
        Intent(requireContext(), PlayerActivity::class.java).apply {
            putExtra(MediaFavoriteFragment.TRACK_KEY, track)
            startActivity(this)
        }
    }

    companion object {
        fun newInstance() = MediaFavoriteFragment()
        private const val TRACK_KEY = "track"
    }
}