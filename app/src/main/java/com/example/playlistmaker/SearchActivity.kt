package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    private var searchText: String = ""
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val messageImage: ImageView = findViewById(R.id.message_image)
    private val buttonUpdate: Button = findViewById(R.id.button_update)
    private val textViewMessageError: TextView = findViewById(R.id.text_view_message_error)
    private val trackListAdapter = TrackListAdapter()
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
    private lateinit var trackList: ArrayList<Track>
    private val iTunesService = retrofit.create(ItunesApi::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = trackListAdapter
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

        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString("searchText").toString()
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

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    private fun showNotFoundError() {                 //Ничего не нашлось
        if (this.isNightModeOn()) {
            Glide.with(this)
                .load(R.drawable.not_found_dark)
                .into(messageImage)
        } else {
            Glide.with(this)
                .load(R.drawable.not_found_light)
                .into(messageImage)
        }
    }

    private fun showNetworkError() {                 //Ошибка сети
        if (this.isNightModeOn()) {
            Glide.with(this)
                .load(R.drawable.network_problem_dark)
                .into(messageImage)
        } else {
            Glide.with(this)
                .load(R.drawable.network_problem_light)
                .into(messageImage)
        }
    }
}