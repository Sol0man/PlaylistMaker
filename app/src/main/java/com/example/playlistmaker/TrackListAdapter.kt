package com.example.playlistmaker

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackListAdapter(): RecyclerView.Adapter<TrackHolder>() {
    var trackList =  ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_track, parent, false)
        return TrackHolder(view)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.bind(trackList[position])
    }


}

