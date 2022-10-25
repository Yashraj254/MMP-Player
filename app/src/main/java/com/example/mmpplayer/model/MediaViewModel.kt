package com.example.mmpplayer.model

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.mmpplayer.database.MyMediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(private val repository: MyMediaRepository) :
    ViewModel() {

    val allVideosList: LiveData<ArrayList<Media>> = liveData {
        emit(repository.getAllVideos())
    }

      @RequiresApi(Build.VERSION_CODES.Q)
      val allMusicList: LiveData<ArrayList<Media>> = liveData {
        emit(repository.getAllMusic())
    }

    val videoFoldersList: LiveData<ArrayList<Folder>> = liveData(Dispatchers.IO) {
        emit(repository.getAllVideoFolders())
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    val musicFoldersList: LiveData<ArrayList<Folder>> = liveData {
        emit(repository.getAllMusicFolders())
    }

    fun getSpecificFolderVideos(folderId: String) = liveData {
        emit(repository.getSpecificFolderVideos(folderId))
    }

     @RequiresApi(Build.VERSION_CODES.Q)
     fun getSpecificFolderMusic(folderId: String) = liveData {
        emit(repository.getSpecificFolderMusic(folderId))
    }
}