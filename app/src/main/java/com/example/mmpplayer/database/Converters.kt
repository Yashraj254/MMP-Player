package com.example.mmpplayer.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.mmpplayer.model.Media
import com.example.mmpplayer.model.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Converters {

 @TypeConverter
    fun stringToPlaylist(json: String?): Playlist? {
        val type: Type = object : TypeToken<Playlist?>() {}.type
        return Gson().fromJson(json,type)
    }

    @TypeConverter
    fun playlistToString(date: Playlist?): String? {
        return Gson().toJson(date)
    }

    @TypeConverter
    fun stringToMedia(json: String?): ArrayList<Media>? {
        val type: Type = object : TypeToken<ArrayList<Media?>?>() {}.type
        return Gson().fromJson(json,type)
    }

    @TypeConverter
    fun mediaToString(list: ArrayList<Media?>?): String? {
        return Gson().toJson(list)
    }
}