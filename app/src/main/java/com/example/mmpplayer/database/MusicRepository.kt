package com.example.mmpplayer.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.mmpplayer.model.Folder
import com.example.mmpplayer.model.Media
import com.example.mmpplayer.model.Playlist
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MusicRepository @Inject constructor(private val dao: MusicDao) {

    val allFavourites = dao.getAllFavourite()
    val allPlaylists = dao.getAllPlaylists()

    suspend fun deleteFavourite(music: Media) {
        dao.deleteMusic(music)
    }

    suspend fun addToFavourite(music: Media) {
        dao.addToFavourite(music)
    }

    suspend fun createPlaylist(playlist: Playlist) {
        dao.createPlaylist(playlist)
    }

    suspend fun addToPlaylist(playlist: Playlist) {
        dao.addToPlaylist(playlist)
    }

    suspend fun deletePlaylist(playlist: Playlist) {
        dao.deletePlaylist(playlist)
    }

}