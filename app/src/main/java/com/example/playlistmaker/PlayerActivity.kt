package com.example.playlistmaker

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var playButton: ImageButton
    private lateinit var queueButton: Button
    private lateinit var favoriteButton: Button
    private lateinit var rootLayout: ConstraintLayout

    private lateinit var albumPage: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var collection: TextView
    private lateinit var collectionName: TextView
    private lateinit var releaseDate: TextView
    private lateinit var genre: TextView
    private lateinit var country: TextView
    private lateinit var duration: TextView
    private lateinit var timeOfPlay: TextView

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private var currentPosition = 0

    private val handler = Handler(Looper.getMainLooper())
    private val updateTimeRunnable = object :Runnable {
        override fun run() {
            if (playerState == STATE_PLAYING){
                val currentPosition = mediaPlayer.currentPosition
                timeOfPlay.text = SimpleDateFormat("m:ss", Locale.getDefault()).format(currentPosition)
                handler.postDelayed(this, 100)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        rootLayout = findViewById(R.id.main)


        albumPage = findViewById(R.id.album_page)
        trackName = findViewById(R.id.track_name)
        artistName = findViewById(R.id.artist_name)
        collection = findViewById(R.id.album)
        collectionName = findViewById(R.id.album_name)
        releaseDate = findViewById(R.id.track_year)
        genre = findViewById(R.id.genre_name)
        country = findViewById(R.id.country_name)
        duration = findViewById(R.id.track_duration)
        timeOfPlay = findViewById(R.id.time_of_play)
        playButton = findViewById(R.id.button_play)


        val backButton = findViewById<ImageView>(R.id.back_button_iv)
        backButton.setOnClickListener {
            finish()
        }

        val trackJson = intent.getStringExtra("track")
        val track = Gson().fromJson(trackJson, Track::class.java)

        track?.let {
            addDataInPlayer(it)
        }

        if (track?.previewUrl.isNullOrEmpty()) {
            Toast.makeText(this, "Отсутствует аудио дорожка", Toast.LENGTH_SHORT).show()
        } else {
                preparePlayer(track)
            }

        playButton.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(updateTimeRunnable)

    }

    private fun addDataInPlayer(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        collectionName.text = track.collectionName
        releaseDate.text = track.releaseDate?.substring(0, 4) ?: ""
        genre.text = track.primaryGenreName
        country.text = track.country
        duration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        val artworkUrl512 = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

        Glide.with(this)
            .load(artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.track_corner_radius)))
            .into(albumPage)

        hideEmptyFields()
    }

    //метод для скрывания пустых полей
    private fun hideEmptyFields() {
        val set = ConstraintSet()
        set.clone(rootLayout)

        if (country.text.isNullOrEmpty()) {
            set.setVisibility(R.id.country, ConstraintSet.GONE)
            set.setVisibility(R.id.country_name, ConstraintSet.GONE)
        } else {
            set.setVisibility(R.id.country, ConstraintSet.VISIBLE)
            set.setVisibility(R.id.country_name, ConstraintSet.VISIBLE)
        }

        if (genre.text.isNullOrEmpty()) {
            set.setVisibility(R.id.genre, ConstraintSet.GONE)
            set.setVisibility(R.id.genre_name, ConstraintSet.GONE)
        } else {
            set.setVisibility(R.id.genre, ConstraintSet.VISIBLE)
            set.setVisibility(R.id.genre_name, ConstraintSet.VISIBLE)
        }

        if (releaseDate.text.isNullOrEmpty()) {
            set.setVisibility(R.id.year, ConstraintSet.GONE)
            set.setVisibility(R.id.track_year, ConstraintSet.GONE)
        } else {
            set.setVisibility(R.id.year, ConstraintSet.VISIBLE)
            set.setVisibility(R.id.track_year, ConstraintSet.VISIBLE)
        }

        if (collectionName.text.isNullOrEmpty()) {
            set.setVisibility(R.id.album, ConstraintSet.GONE)
            set.setVisibility(R.id.album_name, ConstraintSet.GONE)
        } else {
            set.setVisibility(R.id.album, ConstraintSet.VISIBLE)
            set.setVisibility(R.id.album_name, ConstraintSet.VISIBLE)
        }
        set.applyTo(rootLayout)
    }

    private fun preparePlayer(track: Track) {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(track.previewUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playButton.isEnabled = true
                playerState = STATE_PREPARED
            }

            mediaPlayer.setOnCompletionListener {
                playerState = STATE_PREPARED
                handler.removeCallbacks(updateTimeRunnable)
                timeOfPlay.text = "0:00"
                playButton.setImageResource(R.drawable.button_play)
                mediaPlayer.seekTo(0)
            }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        playButton.setImageResource(R.drawable.button_pause)
        timeOfPlay.text = SimpleDateFormat("m:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
        handler.post(updateTimeRunnable)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        playButton.setImageResource(R.drawable.button_play)
        handler.removeCallbacks(updateTimeRunnable)
    }

    private fun playbackControl() {                                                 // контроль воспроизведения
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PAUSED -> {
                startPlayer()
            }

            STATE_PREPARED -> {
                playButton.setImageResource(R.drawable.button_pause)
                startPlayer()
            }

            STATE_DEFAULT -> {
                currentPosition = 0
            }
        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}