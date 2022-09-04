package com.example.mmpplayer.video

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.mmpplayer.model.Folder
import com.example.mmpplayer.model.Media

class MediaViewModel(context: Application, private val repository: MyMediaRepository) :
    AndroidViewModel(context) {
    // TODO: Implement the ViewModel
    val allVideosList: LiveData<ArrayList<Media>> = liveData {
        emit(repository.getAllVideos())
    }

      @RequiresApi(Build.VERSION_CODES.Q)
      val allMusicList: LiveData<ArrayList<Media>> = liveData {
        emit(repository.getAllMusic())
    }

    val videoFoldersList: LiveData<ArrayList<Folder>> = liveData {
        emit(repository.getAllVideoFolders())
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    val musicFoldersList: LiveData<ArrayList<Folder>> = liveData {
        emit(repository.getAllMusicFolders())
    }

    fun getSpecificFolderVideos(folderId: String) = liveData<ArrayList<Media>> {
        emit(repository.getSpecificFolderVideos(folderId))
    }

     @RequiresApi(Build.VERSION_CODES.Q)
     fun getSpecificFolderMusic(folderId: String) = liveData<ArrayList<Media>> {
        emit(repository.getSpecificFolderMusic(folderId))
    }



}