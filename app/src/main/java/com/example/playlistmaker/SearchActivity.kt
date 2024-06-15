package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ItunesApi::class.java)

    private lateinit var searchText: String
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageImage: ImageView
    private lateinit var buttonUpdate: Button
    private lateinit var textViewMessageError: TextView
    private lateinit var clearHistoryButton: Button
    private lateinit var searchHistory: SearchHistory
    private lateinit var rvSearchHistory: RecyclerView
    private lateinit var tvSearchHistoryTitle: TextView
    private lateinit var historyLayout: LinearLayout
    private lateinit var trackListLayout: LinearLayout
    private lateinit var progressBar: ProgressBar


    private val trackList = ArrayList<Track>()
    private val trackListAdapter = TrackListAdapter()
    private val historyAdapter = HistoryAdapter()

    private var lastFailedQuery: String? = null

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        recyclerView = findViewById(R.id.rvTracks)
        searchEditText = findViewById(R.id.searchEditText)
        messageImage = findViewById(R.id.message_image)
        buttonUpdate = findViewById(R.id.button_update)
        textViewMessageError = findViewById(R.id.tvMessageError)
        clearHistoryButton = findViewById(R.id.clear_history_button)
        rvSearchHistory = findViewById(R.id.rv_search_history)
        tvSearchHistoryTitle = findViewById(R.id.tv_search_history_title)
        historyLayout = findViewById(R.id.history_layout)
        trackListLayout = findViewById(R.id.tracklist_layout)
        progressBar = findViewById(R.id.progress_bar)

        trackListAdapter.trackList = trackList

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = trackListAdapter
        rvSearchHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvSearchHistory.adapter = historyAdapter


        val backButton = findViewById<ImageButton>(R.id.back_button)
        val clearButton = findViewById<ImageView>(R.id.clearButton)
        val sharedPreferences = getSharedPreferences("search_history", Context.MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)
        historyAdapter.setTracks(searchHistory.getTracksHistory())


        backButton.setOnClickListener {
            finish()                                //кнопка назад
        }

        clearButton.setOnClickListener {
            searchEditText.text.clear()
            clearFoundTracks()
            clearButton.isVisible = false            //кнопка очистить строку поиска
            visibleAndGoneHistoryLayout(true)
            hideKeyboard(searchEditText)
        }

        buttonUpdate.setOnClickListener {
            lastFailedQuery?.let { query ->   //кнопка обновить запрос
                performSearch(query)
            }
        }

        clearHistoryButton.setOnClickListener {   //кнопка очистить историю поиска
            searchHistory.clearHistory()
            historyAdapter.setTracks(searchHistory.getTracksHistory())
            visibleAndGoneHistoryLayout(false)
        }

        trackListAdapter.setOnItemClickListener { track ->
            if (clickDebounce()) {
                searchHistory.addTrack(track)
                historyAdapter.setTracks(searchHistory.getTracksHistory())           // нажатие на элемент поиска
                startPlayerActivity(track)
            }
        }

        historyAdapter.setOnItemClickListener { track ->
            if (clickDebounce()) {
                searchHistory.addTrack(track)
                historyAdapter.setTracks(searchHistory.getTracksHistory())             // нажатие на элемент истории поиска
                startPlayerActivity(track)
            }
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
                historyLayout.isVisible =
                        // поисковой запрос
                    (searchEditText.hasFocus()
                            && s?.isEmpty() == true
                            && searchHistory.getTracksHistory().isNotEmpty()
                            )
                searchDebounce()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {}
        })


        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchHistory.getTracksHistory()
                    .isNotEmpty() && searchEditText.text.isEmpty()
            ) {
                updateRecyclerViewSearchHistory()
            } else {
                visibleAndGoneHistoryLayout(false)
            }
        }

        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString("searchText", "").toString()
            searchEditText.setText(searchText)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        searchText = searchEditText.text.toString()
        outState.putString("searchText", searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        searchText = savedInstanceState.getString("searchText").toString()
        searchEditText.setText(searchText)
    }

    private fun performSearch(query: String) {

        runOnUiThread {
            progressBar.isVisible = true
        }
        iTunesService.search(query).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                runOnUiThread {
                    progressBar.isVisible = false
                }
                if (response.isSuccessful) {
                    val trackResponse = response.body()
                    trackListLayout.visibility = View.VISIBLE
                    if (trackResponse?.results?.isEmpty() == true) {
                        showNotFoundError()
                    } else {
                        val resultTrackList = trackResponse?.results ?: emptyList()
                        showSearchResults(resultTrackList)
                    }
                } else {
                    lastFailedQuery = query
                    showNetworkError()
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                runOnUiThread {
                    progressBar.isVisible = false
                }
                lastFailedQuery = query
                showNetworkError()
            }
        })

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearchResults(trackList: List<Track>) {
        this.trackList.clear()
        this.trackList.addAll(trackList)                  //показать результат
        trackListAdapter.notifyDataSetChanged()

        recyclerView.isVisible = true
        messageImage.isVisible = false
        buttonUpdate.isVisible = false
        textViewMessageError.isVisible = false
    }

    private fun showNotFoundError() {
        messageImage.setImageResource(if (isNightModeOn()) R.drawable.not_found_dark else R.drawable.not_found_light)
        messageImage.isVisible = true
        buttonUpdate.isVisible = false
        textViewMessageError.isVisible = true                   //ошибка ничего не найдено
        recyclerView.isVisible = false
        textViewMessageError.setText(R.string.not_found_error)
    }

    private fun showNetworkError() {
        messageImage.setImageResource(if (isNightModeOn()) R.drawable.network_problem_dark else R.drawable.network_problem_light)
        messageImage.isVisible = true
        buttonUpdate.isVisible = true
        textViewMessageError.isVisible = true                   //ошибка нет соединения
        recyclerView.isVisible = false
        textViewMessageError.setText(R.string.network_error)
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearFoundTracks() {
        trackList.clear()
        trackListAdapter.notifyDataSetChanged()
    }

    private fun visibleAndGoneHistoryLayout(action: Boolean) {
        if (action && searchHistory.getTracksHistory().isNotEmpty() && searchEditText.hasFocus()) {
            historyLayout.visibility = View.VISIBLE
            trackListLayout.visibility = View.GONE
        } else {
            historyLayout.visibility = View.GONE
        }
    }

    private fun updateRecyclerViewSearchHistory() {
        visibleAndGoneHistoryLayout(true)
        searchHistory.getTracksHistory()
    }

    private fun startPlayerActivity(track: Track) {
        val gson = Gson()
        val trackJson = gson.toJson(track)
        val displayIntent = Intent(this, PlayerActivity::class.java)
        displayIntent.putExtra("track", trackJson)
        startActivity(displayIntent)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)

        }
        return current
    }

    private fun searchDebounce() {
        searchRunnable?.let { handler.removeCallbacks(it) }
        searchRunnable = Runnable {
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                performSearch(query)
            }
        }
        handler.postDelayed(searchRunnable!!, SEARCH_DEBOUNCE_DELAY)

    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}