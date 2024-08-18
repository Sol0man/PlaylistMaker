package com.example.playlistmaker.presentation.ui.search.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.model.SearchStatus
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.model.TrackSearchResult
import java.util.function.Consumer

class SearchViewModel (
    val searchHistoryInteractor: SearchHistoryInteractor,
    val trackInteractor: TrackInteractor,

) : ViewModel(), Consumer<TrackSearchResult> {
    private var requestText = ""
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private var tracksHistory = searchHistoryInteractor.getTracksHistory()

    private val foundTracks: MutableLiveData<TrackSearchResult> =
        MutableLiveData(TrackSearchResult(results = emptyList(), SearchStatus.DEFAULT))
    
    private val searchRunnable = Runnable { 
        foundTracks.postValue(getLoadingStatus())
        sendRequest()
    }
    fun getFoundTracks(): LiveData<TrackSearchResult> = foundTracks
    
    fun removeCallbacks() {
        handler.removeCallbacks(searchRunnable)
    }
    
    fun getTracksHistory(): ArrayList<Track> = tracksHistory

    fun updateTrackHistory() {
        tracksHistory = searchHistoryInteractor.getTracksHistory()
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
                CLICK_DEBOUNCE_DELAY
            )
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
                foundTracks.postValue(trackSearchResult)
            }

            SearchStatus.NETWORK_ERROR, SearchStatus.DEFAULT, SearchStatus.LIST_IS_EMPTY -> {
                foundTracks.postValue(trackSearchResult)
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