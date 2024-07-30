package com.example.playlistmaker.ui.search.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.search.model.SearchStatus
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.model.TrackSearchResult
import com.example.playlistmaker.isNightModeOn
import com.example.playlistmaker.ui.player.activity.PlayerActivity
import com.example.playlistmaker.ui.search.common.TrackListAdapter
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModelFactory


class SearchActivity : AppCompatActivity() {
    private var editTextValue = ""
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchAdapter: TrackListAdapter
    private lateinit var historyAdapter: TrackListAdapter
    private lateinit var viewModel: SearchViewModel
    private lateinit var tracks: ArrayList<Track>

    private val onClick: (track: Track) -> Unit = {
        if (viewModel.clickDebounce()) {
            viewModel.addTrackInSearchHistory(it)
            historyAdapter.notifyDataSetChanged()
            startPlayerActivity(it)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, SearchViewModelFactory())[SearchViewModel::class.java]
        tracks = ArrayList<Track>()
        searchAdapter = TrackListAdapter(tracks, onClick)
        historyAdapter = TrackListAdapter(viewModel.getTracksHistory(), onClick)

        viewModel.getFoundTracks().observe(this) {it ->
            processingSearchStatus(it)

        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.clearButton.setOnClickListener {
            binding.searchEditText.setText("")
            hideKeyboard()
            changeStateWhenSearchBarIsEmpty()
        }

        binding.buttonUpdate.setOnClickListener {
            viewModel.changeRequestText(binding.searchEditText.text.toString())
            viewModel.searchDebounce()
        }

        binding.clearHistoryButton.setOnClickListener {   //кнопка очистить историю поиска
            showAndHideHistoryLayout(false)
            viewModel.cleanHistory()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearButton.visibility = clearButtonVisibility(s)
                binding.historyLayout.visibility =
                    if (binding.searchEditText.hasFocus()
                        && s?.isEmpty() == true
                            && viewModel.getTracksHistory().isNotEmpty()
                    ) View.VISIBLE else View.GONE

                editTextValue = binding.searchEditText.text.toString()
                if (editTextValue.isEmpty()) {
                    changeStateWhenSearchBarIsEmpty()
                    viewModel.removeCallbacks()
                } else {
                    viewModel.changeRequestText(binding.searchEditText.text.toString())
                    viewModel.searchDebounce()
                    hideErrorElements()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.searchEditText.addTextChangedListener(simpleTextWatcher)

        binding.rvTracks.adapter = searchAdapter
        binding.rvSearchHistory.adapter = historyAdapter
        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus
                && binding.searchEditText.text.isEmpty()
                && viewModel.getTracksHistory().isNotEmpty()
                ) {
                updateRecyclerViewSearchHistory()
            } else {
                showAndHideHistoryLayout(false)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeCallbacks()
    }

    private fun startPlayerActivity(track: Track) {
        Intent(this, PlayerActivity::class.java).apply {
            putExtra(TRACK_KEY, track)
            startActivity(this)
        }
    }

    private fun updateRecyclerViewSearchHistory() {
        showAndHideHistoryLayout(true)
        viewModel.updateTrackHistory()
        historyAdapter.notifyDataSetChanged()
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun showImageError(typeError: SearchStatus) {
        if (typeError == SearchStatus.LIST_IS_EMPTY) {
            showNotFoundError()
        } else {
            showNetworkError()
            binding.buttonUpdate.visibility = View.VISIBLE
        }
        binding.messageImage.visibility = View.VISIBLE
        binding.tvMessageError.visibility = View.VISIBLE
    }

    private fun showNotFoundError() {
        binding.tvMessageError.text = getString(R.string.not_found_error)
        if (this.isNightModeOn()) {
            Glide.with(this)
                .load(R.drawable.not_found_dark)
                .into(binding.messageImage)
        } else {
            Glide.with(this)
                .load(R.drawable.not_found_light)
                .into(binding.messageImage)
        }
    }

    private fun showNetworkError() {
        binding.tvMessageError.text = getString(R.string.network_error)
        if (this.isNightModeOn()) {
            Glide.with(this)
                .load(R.drawable.network_problem_dark)
                .into(binding.messageImage)
        } else {
            Glide.with(this)
                .load(R.drawable.network_problem_light)
                .into(binding.messageImage)
        }
    }

    private fun hideErrorElements() {
        binding.messageImage.visibility = View.GONE
        binding.tvMessageError.visibility = View.GONE
        binding.buttonUpdate.visibility = View.GONE
    }

    private fun hideRecyclerView() {
        binding.rvTracks.visibility = View.GONE
    }

    private fun changeStateWhenSearchBarIsEmpty() {
        hideErrorElements()
        hideRecyclerView()
        updateRecyclerViewSearchHistory()
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }

    private fun showAndHideHistoryLayout(action: Boolean) {
        if (action && viewModel.getTracksHistory().isNotEmpty()) {
            binding.historyLayout.visibility = View.VISIBLE
        } else {
            binding.historyLayout.visibility = View.GONE
        }
    }

    private fun processingSearchStatus (trackSearchResult: TrackSearchResult) {
        tracks.clear()
        hideRecyclerView()
        when(trackSearchResult.status) {
            SearchStatus.DEFAULT -> {
                binding.progressBar.visibility = View.GONE
            }
            SearchStatus.RESPONSE_RECEIVED -> {
                binding.progressBar.visibility = View.GONE
                tracks.addAll(trackSearchResult.results)
                binding.rvTracks.visibility = View.VISIBLE
                searchAdapter.notifyDataSetChanged()
            }
            SearchStatus.LIST_IS_EMPTY -> {
                binding.progressBar.visibility = View.GONE
                showImageError(SearchStatus.LIST_IS_EMPTY)
            }
            SearchStatus.NETWORK_ERROR -> {
                binding.progressBar.visibility = View.GONE
                showImageError(SearchStatus.NETWORK_ERROR)
            }
            SearchStatus.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        private const val TRACK_KEY = "track"
    }
}