package com.example.mmpplayer.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.mmpplayer.database.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MusicPlayerViewModel @Inject constructor(private val repository: MusicRepository) :
    ViewModel() {


    fun getAllFavourites() = liveData {
        repository.allFavourites.collect {
            emit(it)
        }
    }

    fun addToFavourites(music: Media) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addToFavourite(music)
        }
    }

    fun removeFromFavourites(music: Media) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavourite(music)
        }
    }

    fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createPlaylist(playlist)
        }
    }


    fun getAllPlaylists() = liveData {
        repository.allPlaylists.collect {
            emit(it)
        }
    }

    fun deletePlaylists(playlist: Playlist){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePlaylist(playlist)
        }
    }

    fun addToPlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addToPlaylist(playlist)
        }
    }
}