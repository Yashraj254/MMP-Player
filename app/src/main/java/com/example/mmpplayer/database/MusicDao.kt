package com.example.mmpplayer.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mmpplayer.model.Folder
import com.example.mmpplayer.model.Media
import com.example.mmpplayer.model.Playlist
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {

    @Insert
    suspend fun addToFavourite(music: Media)

    @Delete
    suspend fun deleteMusic(music: Media)

    @Query("select * from favourite_music")
    fun getAllFavourite(): Flow<List<Media>>

    @Insert
    suspend fun createPlaylist(playlist:Playlist)

    @Delete
    suspend fun deletePlaylist(playlist:Playlist)

    @Update
    suspend fun addToPlaylist(playlist: Playlist)

    @Query("select * from playlists")
    fun getAllPlaylists(): Flow<List<Playlist>>

}