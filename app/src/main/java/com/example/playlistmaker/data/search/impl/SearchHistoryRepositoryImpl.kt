package com.example.playlistmaker.data.search.impl

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.app.App
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl (val app: App) : SearchHistoryRepository {

    private var sharedPreferences: SharedPreferences = app.getSharedPreferences(SEARCH_HISTORY, MODE_PRIVATE)
    private val tracks: ArrayList<Track> = readSearchHistory()

    override fun addTrack(track: Track){

        if(tracks.isEmpty()){
            tracks.add(track)
            writeSearchHistory(tracks)
            return
        }
        if(tracks.isNotEmpty()){
            for( i  in tracks){
                if(i.trackId.equals(track.trackId)){
                    tracks.remove(i)
                    tracks.add(0, track)
                    writeSearchHistory(tracks)
                    return
                }
            }
        }
        if(tracks.size < MAX_LENGTH_OF_TRACKLIST){
            tracks.add(0, track)
        } else{
            tracks.removeLast()
            tracks.add(0, track)
        }
        writeSearchHistory(tracks)
    }

    override fun getTracksHistory(): ArrayList<Track> {
        return tracks
    }

    override fun clean() {
        tracks.clear()
        writeSearchHistory(tracks)
    }


    private fun readSearchHistory():ArrayList<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY, null) ?: return ArrayList()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, type)
    }

    private fun writeSearchHistory(searchList: ArrayList<Track>){
        val json = Gson().toJson(searchList)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY, json)
            .apply()
    }

    companion object {
        private const val SEARCH_HISTORY = "search_history"
        const val MAX_LENGTH_OF_TRACKLIST = 10
    }
}
