package com.example.mmpplayer

import android.media.MediaPlayer
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance

object MyMediaPlayer {
    private lateinit var instance: MediaPlayer
     fun  getInstance(): MediaPlayer {

             instance = MediaPlayer()
         return instance
    }
    var currentIndex = -1
}