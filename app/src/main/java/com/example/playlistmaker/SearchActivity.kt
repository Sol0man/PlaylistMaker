package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    private lateinit var searchText: String
    private lateinit var recyclerView : RecyclerView
    private lateinit var trackListAdapter : TrackListAdapter
    private lateinit var messageImage: ImageView
    private lateinit var buttonUpdate: Button
    private lateinit var textViewMessageError: TextView
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ItunesApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        trackListAdapter = TrackListAdapter(ArrayList())
        recyclerView.adapter = trackListAdapter


        messageImage = findViewById(R.id.message_image)
        buttonUpdate = findViewById(R.id.button_update)
        textViewMessageError = findViewById(R.id.text_view_message_error)


        val backButton = findViewById<ImageButton>(R.id.back_button)
        val clearButton = findViewById<ImageView>(R.id.clearButton)
        val searchEditText = findViewById<EditText>(R.id.searchEditText)

        backButton.setOnClickListener {
            finish()
        }
        clearButton.setOnClickListener {
            searchEditText.text.clear()
            clearButton.isVisible = false
            hideKeyboard(searchEditText)
        }
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = if (s.isNullOrEmpty()) false else true
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {}
        })

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                performSearch(searchEditText.text.toString())
                true
            }
            false
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
        val call = iTunesService.search(query)
        call.enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                if (response.isSuccessful) {
                    val trackResponse = response.body()
                    if (trackResponse?.results?.isEmpty() == true) {
                        showNotFoundError()
                    } else {
                        val trackList = trackResponse?.results ?: emptyList()
                        showSearchResults(trackList)
                    }
                } else {
                    showNetworkError()
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                showNetworkError()
            }
        })
    }

    private fun showSearchResults(trackList: List<Track>) {
        trackListAdapter.updateList(trackList)
        recyclerView.visibility = View.VISIBLE
        messageImage.visibility = View.GONE
        buttonUpdate.visibility = View.GONE
        textViewMessageError.visibility = View.GONE
    }

    private fun showNotFoundError() {
        messageImage.setImageResource(if (isNightModeOn()) R.drawable.not_found_dark else R.drawable.not_found_light)
        messageImage.visibility = View.VISIBLE
        buttonUpdate.visibility = View.GONE
        textViewMessageError.visibility = View.GONE
        recyclerView.visibility = View.GONE
    }

    private fun showNetworkError() {
        messageImage.setImageResource(if (isNightModeOn()) R.drawable.network_problem_dark else R.drawable.network_problem_light)
        messageImage.visibility = View.VISIBLE
        buttonUpdate.visibility = View.VISIBLE
        textViewMessageError.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}