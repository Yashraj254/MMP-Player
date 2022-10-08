package com.example.mmpplayer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val playlistName: String,
    val playlistMusic: ArrayList<Media>,
    var isSelected: Boolean = false,
)