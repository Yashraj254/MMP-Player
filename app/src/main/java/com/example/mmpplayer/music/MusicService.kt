package com.example.mmpplayer.music

import android.app.Application
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.bluetooth.BluetoothClass
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import com.example.mmpplayer.R
import com.example.mmpplayer.database.MyApplication


class MusicService : Service() {
    var mediaPlayer : MediaPlayer? = null
    private var myBinder = MyBinder()
    private lateinit var mediaSession : MediaSessionCompat

    override fun onBind(p0: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext,"My Music")
        return myBinder
    }

    inner class MyBinder : Binder(){
        fun currentService() : MusicService {
            return this@MusicService
        }
    }
    fun showNotification(playPauseBtn:Int){
        val prevIntent = Intent (baseContext, NotificationMusicReceiver::class.java).setAction (
            MyApplication.PREVIOUS)
        val prevPendingIntent = PendingIntent.getBroadcast (baseContext,  0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val playIntent = Intent (baseContext, NotificationMusicReceiver::class.java).setAction(MyApplication.PLAY)
        val playPendingIntent = PendingIntent.getBroadcast (baseContext,  0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent = Intent (baseContext, NotificationMusicReceiver::class.java).setAction(MyApplication.NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast (baseContext,  0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val exitIntent = Intent (baseContext, NotificationMusicReceiver::class.java).setAction (MyApplication. EXIT)
        val exitPendingIntent = PendingIntent.getBroadcast (baseContext,  0, exitIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(baseContext, MyApplication.CHANNEL_ID)
            .setContentTitle(MusicPlayerActivity.songsList[MusicPlayerActivity.position].title)
            .setSmallIcon(R.drawable.music_image)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.music_image))
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.ic_video_prev,"Previous",prevPendingIntent)
            .addAction(playPauseBtn,"Play",playPendingIntent)
            .addAction(R.drawable.ic_video_next,"Next",nextPendingIntent)
            .addAction(R.drawable.ic_exit,"Exit",exitPendingIntent)
            .build()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            val playbackSpeed = if(mediaPlayer!!.isPlaying) 1F else 0F
            mediaSession.setMetadata(MediaMetadataCompat.Builder()
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer!!.duration.toLong())
                .build())
            val playBackState = PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, mediaPlayer!!.currentPosition.toLong(), playbackSpeed)
                .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
                .build()
            mediaSession.setPlaybackState(playBackState)
            mediaSession.setCallback(object: MediaSessionCompat.Callback(){
                override fun onSeekTo(pos: Long) {
                    super.onSeekTo(pos)
                    mediaPlayer!!.seekTo(pos.toInt())
                    val playBackStateNew = PlaybackStateCompat.Builder()
                        .setState(PlaybackStateCompat.STATE_PLAYING, mediaPlayer!!.currentPosition.toLong(), playbackSpeed)
                        .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
                        .build()
                    mediaSession.setPlaybackState(playBackStateNew)
                }
            })
        }
        startForeground(13,notification)
    }
}