package com.example.playlistmaker.presentation.ui.player.activity

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.player.models.MediaPlayerStatus
import com.example.playlistmaker.domain.player.models.PlayerProgressStatus
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.presentation.ui.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class  PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding

    private val viewModel by viewModel<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.timeOfPlay.text = getString(R.string.player_dafault_time)


        @Suppress("DEPRECATION") val track =
            if (SDK_INT >= 33) {                        //Проверяем версию SDK и в зависимости от верстии применяем тот или иной метод для работы с intent
                intent.getParcelableExtra(TRACK_KEY, Track::class.java)!!
            } else {
                intent.getParcelableExtra<Track>(TRACK_KEY)!!
            }

        addDataInPlayer(track)
        viewModel.onCreate(track)

        viewModel.getPlayerProgressStatus().observe(this) { playerProgressStatus ->
            playbackControl(playerProgressStatus)
        }

        binding.backButtonIv.setOnClickListener {
            finish()
        }

        binding.buttonPlay.setOnClickListener {
            viewModel.playbackControl()
        }
    }


    override fun onPause() {
        super.onPause()
        viewModel.pauseMediaPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroyMediaPlayer()
    }

    private fun addDataInPlayer(track: Track) {
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.albumName.text = track.collectionName
        binding.trackYear.text = if (!track.releaseDate.equals(R.string.unknown)) {
            track.releaseDate.substring(0, 4)
        } else getString(R.string.not_found)
        binding.genreName.text = track.primaryGenreName
        binding.countryName.text = track.country
        binding.trackDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        val artworkUrl512 = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

        Glide.with(this)
            .load(artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.track_corner_radius)))
            .into(binding.albumPage)
    }

    private fun playbackControl(playerProgressStatus: PlayerProgressStatus) {
        when (playerProgressStatus.mediaPlayerStatus) {
            MediaPlayerStatus.STATE_PLAYING -> {
                binding.buttonPlay.setImageResource(R.drawable.button_pause)
                binding.timeOfPlay.text =
                    SimpleDateFormat(
                        "m:ss",
                        Locale.getDefault()
                    ).format(playerProgressStatus.currentPosition)
            }
            MediaPlayerStatus.STATE_PAUSED -> {
                binding.buttonPlay.setImageResource(R.drawable.button_play)
            }
            MediaPlayerStatus.STATE_PREPARED -> {
                binding.timeOfPlay.text = "0:00"
                binding.buttonPlay.setImageResource(R.drawable.button_play)
            }
            MediaPlayerStatus.STATE_ERROR -> {
                showErrorMessage()
            }
            MediaPlayerStatus.STATE_DEFAULT -> {
            }
        }
    }
    fun showErrorMessage() {
        Toast.makeText(
            this,
            getString(R.string.audio_not_found),
            Toast.LENGTH_SHORT
        ).show()
    }
    companion object {
        private const val TRACK_KEY = "track"
    }
}

