package com.example.mmpplayer.music

import android.app.Activity
import android.bluetooth.BluetoothClass
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.request.RequestOptions
import com.example.mmpplayer.R
import com.example.mmpplayer.database.MyApplication
import com.google.android.exoplayer2.Player
import java.io.IOException
import kotlin.system.exitProcess

class NotificationMusicReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            MyApplication.PREVIOUS -> prevNextSong(false)
            MyApplication.PLAY -> if (MusicPlayerActivity.isPlaying) pauseMusic() else playMusic()
            MyApplication.NEXT -> prevNextSong(true)
            MyApplication.EXIT -> {
                MusicPlayerActivity.musicService!!.stopForeground(true)
                MusicPlayerActivity.musicService = null
                exitProcess(1)
            }
        }
    }

    private fun playMusic() {
        MusicPlayerActivity.isPlaying = true
        MusicPlayerActivity.musicService!!.mediaPlayer!!.start()
        MusicPlayerActivity.musicService!!.showNotification(R.drawable.ic_pause)
        MusicPlayerActivity.binding.ivPlayPause.setImageResource(R.drawable.ic_pause)
    }

    private fun pauseMusic() {
        MusicPlayerActivity.isPlaying = false
        MusicPlayerActivity.musicService!!.mediaPlayer!!.pause()
        MusicPlayerActivity.musicService!!.showNotification(R.drawable.ic_play)
        MusicPlayerActivity.binding.ivPlayPause.setImageResource(R.drawable.ic_play)
    }

    private fun prevNextSong(increment: Boolean) {
        setSongPosition(increment = increment)
        createMusicPlayer()
        MusicPlayerActivity.musicService!!.showNotification(R.drawable.ic_pause)
        MusicPlayerActivity.binding.ivPlayPause.setImageResource(R.drawable.ic_pause)
        MusicPlayerActivity.binding.tvSongTitle.text =
            MusicPlayerActivity.songsList[MusicPlayerActivity.position].title
        playMusic()
    }

    private fun createMusicPlayer() {
        MusicPlayerActivity.musicService!!.mediaPlayer!!.reset()
        try {
            MusicPlayerActivity.musicService!!.mediaPlayer!!.setDataSource(MusicPlayerActivity.songsList[MusicPlayerActivity.position].path)
            MusicPlayerActivity.musicService!!.mediaPlayer!!.prepare()
            MusicPlayerActivity.binding.apply {
                sbMusic.progress = 0
                sbMusic.max = MusicPlayerActivity.musicService!!.mediaPlayer!!.duration
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}