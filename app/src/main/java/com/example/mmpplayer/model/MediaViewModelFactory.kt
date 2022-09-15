package com.example.mmpplayer.model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mmpplayer.database.MyMediaDataSource
import com.example.mmpplayer.database.MyMediaRepository
import kotlinx.coroutines.Dispatchers

class MediaViewModelFactory(private val app: Application) : ViewModelProvider.AndroidViewModelFactory(app){

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MediaViewModel::class.java)) {
            val source = MyMediaDataSource(app.contentResolver)
            return MediaViewModel(app,
                MyMediaRepository(source,Dispatchers.IO)) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}

