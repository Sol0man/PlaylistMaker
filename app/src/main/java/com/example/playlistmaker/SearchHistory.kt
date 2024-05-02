package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val SEARCH_HISTORY = "search_history"
const val MAX_LENGHT_OF_TRACKLIST = 10
class SearchHistory (private val sharedPreferences: SharedPreferences) {
    val searchList:ArrayList<Track> = readSearchHistory()

    fun addTrack(track: Track){

        if(searchList.isEmpty()){
            searchList.add(track)
            writeSearchHistory(searchList)
            return
        }
        if(searchList.isNotEmpty()){
            for( i  in searchList){
                if(i.trackId.equals(track.trackId)){
                    searchList.remove(i)
                    searchList.add(0, track)
                    writeSearchHistory(searchList)
                    return
                }
            }
        }
        if(searchList.size < MAX_LENGHT_OF_TRACKLIST){
            searchList.add(0, track)
        } else{
            searchList.removeLast()
            searchList.add(0, track)
        }
        writeSearchHistory(searchList)
    }

    fun getTracksHistory(): ArrayList<Track> {
        return searchList
    }

    fun clearHistory() {
        searchList.clear()
        writeSearchHistory(searchList)
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
}
