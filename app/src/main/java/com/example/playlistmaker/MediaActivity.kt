package com.example.playlistmaker

import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.items.Track

class MediaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaBinding
    private val viewModel: MediaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получаем трек из Intent
        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("TRACK_EXTRA", Track::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("TRACK_EXTRA")
        }

        viewModel.setTrack(track)

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.trackData.observe(this, Observer { track ->
            track?.let {
                binding.trackName.text = it.trackName
                binding.trackAuctor.text = it.artistName
                binding.trackDuration.text = it.trackTime
                binding.trackAlbum.text = it.collectionName
                binding.trackAlbum.visibility = if (it.collectionName.isNullOrEmpty()) View.GONE else View.VISIBLE
                binding.album.visibility = binding.trackAlbum.visibility
                binding.trackYear.text = it.year
                binding.trackGenre.text = it.primaryGenreName
                binding.trackCountry.text = it.country
                loadTrackImage(it.artworkUrl100)
            }
        })
    }

    private fun loadTrackImage(imageUrl: String?) {
        val cornerRadiusPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            8f,
            resources.displayMetrics
        ).toInt()

        val highResUrl = viewModel.getHighResImageUrl(imageUrl)

        Glide.with(this)
            .load(highResUrl)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .transform(RoundedCorners(cornerRadiusPx))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.trackBigImage)
    }

    private fun setupClickListeners() {
        binding.mediaTopBar.setOnClickListener {
            finish()
        }
    }
}