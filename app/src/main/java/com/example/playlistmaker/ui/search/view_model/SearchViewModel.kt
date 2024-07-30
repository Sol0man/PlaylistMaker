package com.example.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import java.util.function.Consumer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.model.SearchStatus
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.model.TrackSearchResult

class SearchViewModel (
    val searchHistoryInteractor: SearchHistoryInteractor,
    val trackInteractor: TrackInteractor,

) : ViewModel(), Consumer<TrackSearchResult> {
    private var requestText = ""
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private var tracksHistory = searchHistoryInteractor.getTrackHistory()

    private val _foundTracks: MutableLiveData<TrackSearchResult> =
        MutableLiveData(TrackSearchResult(results = emptyList(), SearchStatus.DEFAULT))
    
    private val searchRunnable = Runnable { 
        _foundTracks.postValue(getLoadingStatus())
        sendRequest()
    }
    fun getFoundTracks(): LiveData<TrackSearchResult> = _foundTracks
    
    fun removeCallbacks() {
        handler.removeCallbacks(searchRunnable)
    }
    
    fun getTracksHistory(): ArrayList<Track> = tracksHistory

    fun updateTrackHistory() {
        tracksHistory = searchHistoryInteractor.getTrackHistory()
    }

    fun changeRequestText(text: String) {
        requestText = text
    }

    fun addTrackInSearchHistory(track: Track) {
        searchHistoryInteractor.addTrack(track)
    }

    fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(
            searchRunnable,
            SEARCH_DEBOUNCE_DELAY
        )
    }

    fun cleanHistory() {
        searchHistoryInteractor.clean()
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed(
                { isClickAllowed = true },
                CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun sendRequest() {
        trackInteractor.searchTracks(requestText, this)
    }
    
    private fun getLoadingStatus(): TrackSearchResult {
        return TrackSearchResult(
            results = emptyList(),
            SearchStatus.LOADING
        )
    }

    override fun accept(trackSearchResult: TrackSearchResult) {
        when (trackSearchResult.status) {
            SearchStatus.RESPONSE_RECEIVED -> {
                _foundTracks.postValue(trackSearchResult)
            }

            SearchStatus.NETWORK_ERROR, SearchStatus.DEFAULT, SearchStatus.LIST_IS_EMPTY -> {
                _foundTracks.postValue(trackSearchResult)
            }

            SearchStatus.LOADING -> {

            }
        }
    }

    private companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}