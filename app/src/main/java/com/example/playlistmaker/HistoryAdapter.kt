package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter : RecyclerView.Adapter<TrackHolder>() {
    var searchList = ArrayList<Track>()
    var itemClickListener: ((Track) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_track, parent, false)
        return TrackHolder(view)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.bind(searchList[position])
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(searchList[position])
        }
    }
    fun setTracks(tracks: List<Track>) {
        searchList.clear()
        searchList.addAll(tracks)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (Track) -> Unit) {
        itemClickListener = listener
    }
}