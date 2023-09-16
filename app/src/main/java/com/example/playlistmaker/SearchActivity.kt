package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.addTextChangedListener

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val backButton = findViewById<ImageButton>(R.id.back_button)
        val clearButton = findViewById<ImageButton>(R.id.clearButton)
        val searchEditText = findViewById<EditText>(R.id.searchEditText)

        backButton.setOnClickListener{
            finish()
        }
        clearButton.setOnClickListener {
            searchEditText.text.clear()
            clearButton.visibility = View.GONE
        }
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {}
        })

        if (savedInstanceState != null) {
            val searchText = savedInstanceState.getString("searchText")
            searchEditText.setText(searchText)
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val searchText = searchEditText.text.toString()
        outState.putString("searchText", searchText)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val searchText = savedInstanceState.getString("searchText")
        searchEditText.setText(searchText)
    }

}