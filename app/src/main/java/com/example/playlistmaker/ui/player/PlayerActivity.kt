package com.example.playlistmaker.ui.player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.Track
import com.example.playlistmaker.data.player.repositoryImpl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.domain.player.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.player.models.MediaPlayerStatus

import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class  PlayerActivity : AppCompatActivity() {
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



    private val interactor = MediaPlayerInteractorImpl(MediaPlayerRepositoryImpl())

    private var playerState = MediaPlayerStatus.STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())
    private val updateTimeRunnable = object :Runnable {
        override fun run() {
                updateTimer()
                handler.postDelayed(this, 100)
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
            interactor.preparePlayer(it)
            playerState = MediaPlayerStatus.STATE_PREPARED
            addDataInPlayer(it)
            interactor.setOnCompletionListener {
                runOnUiThread {
                    playButton.setImageResource(R.drawable.button_play)
                    playerState = MediaPlayerStatus.STATE_PREPARED
                    timeOfPlay.text = "0:00"
                    handler.removeCallbacks(updateTimeRunnable)
                }
            }
        }

        playButton.setOnClickListener {
            playbackControl()
        }
    }


    override fun onPause() {
        super.onPause()
        if (playerState == MediaPlayerStatus.STATE_PLAYING){
            interactor.pausePlayer()
            playerState = MediaPlayerStatus.STATE_PAUSED
        }
        playButton.setImageResource(R.drawable.button_play)
        handler.removeCallbacks(updateTimeRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        interactor.destroyPlayer()
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

    private fun playbackControl() {                                                 // контроль воспроизведения

        when (playerState) {
            MediaPlayerStatus.STATE_DEFAULT -> {
            }
            MediaPlayerStatus.STATE_PREPARED -> {
                interactor.startPlayer()
                playButton.setImageResource(R.drawable.button_pause )
                playerState = MediaPlayerStatus.STATE_PLAYING
                handler.post(updateTimeRunnable)
            }
            MediaPlayerStatus.STATE_PLAYING -> {
                interactor.pausePlayer()
                playButton.setImageResource(R.drawable.button_play)
                playerState = MediaPlayerStatus.STATE_PAUSED
                handler.removeCallbacks(updateTimeRunnable)
            }
            MediaPlayerStatus.STATE_PAUSED -> {
                interactor.startPlayer()
                playButton.setImageResource(R.drawable.button_pause)
                playerState = MediaPlayerStatus.STATE_PLAYING
                handler.post(updateTimeRunnable)
            }
            MediaPlayerStatus.STATE_ERROR -> {
                Toast.makeText(this, "Отсутствует аудио дорожка", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun updateTimer(){
        if (playerState == MediaPlayerStatus.STATE_PLAYING){
            val currentPosition = interactor.getCurrentPosition()
            timeOfPlay.text = SimpleDateFormat("m:ss", Locale.getDefault()).format(currentPosition)
        }
    }
}