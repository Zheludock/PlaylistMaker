package com.example.playlistmaker.items

import android.icu.text.SimpleDateFormat
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import java.util.Locale

class TrackAdapter(
    private var tracks: List<Track>,
    private val onTrackClick: (Track) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_TRACK = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_header, parent, false)
                HeaderViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_song, parent, false)
                TrackViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind("Вы искали")
            is TrackViewHolder -> {
                val track = tracks[position - 1]
                holder.bind(track)
                holder.itemView.setOnClickListener { onTrackClick(track) }
            }
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_HEADER else TYPE_TRACK
    }

    fun updateTracks(newTracks: List<Track>?) {
        tracks = newTracks ?: emptyList()
        notifyDataSetChanged()
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headerText: TextView = itemView.findViewById(R.id.header_text)

        fun bind(header: String) {
            headerText.text = header
        }
    }

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trackName: TextView = itemView.findViewById(R.id.track_name)
        private val artistName: TextView = itemView.findViewById(R.id.track_auctor)
        private val trackTime: TextView = itemView.findViewById(R.id.track_duration)
        private val trackImage: ImageView = itemView.findViewById(R.id.track_image)

        fun bind(track: Track) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text = track.trackTime
            Glide.with(itemView.context)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .centerInside()
                .transform(
                    RoundedCorners(
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 2f, itemView.context.resources.displayMetrics
                        ).toInt()
                    )
                )
                .into(trackImage)
        }
    }
}

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
){
    val trackTime: String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
}