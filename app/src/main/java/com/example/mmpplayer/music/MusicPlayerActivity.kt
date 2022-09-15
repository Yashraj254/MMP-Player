package com.example.mmpplayer.music

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.example.mmpplayer.R
import com.example.mmpplayer.databinding.ActivityMusicPlayerBinding
import com.example.mmpplayer.model.Media
import java.io.IOException
import java.util.concurrent.TimeUnit

class MusicPlayerActivity : AppCompatActivity(), ServiceConnection {
    private lateinit var binding: ActivityMusicPlayerBinding
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler(Looper.getMainLooper())
    private lateinit var currentSong: Media

    companion object {
        var musicService: MusicService? = null
        lateinit var songsList: ArrayList<Media>
        var position: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intents = Intent(this, MusicService::class.java)
        bindService(intents, this, BIND_AUTO_CREATE)
        startService(intents)
        binding.tvSongTitle.isSelected = true
        songsList = intent.getSerializableExtra("songs_list") as ArrayList<Media>
        position = intent.getIntExtra("music_position", 0)

    }

    private fun changeProgress() {
        this.runOnUiThread(object : Runnable {
            override fun run() {
                binding.apply {
                    sbMusic.progress = musicService!!.mediaPlayer!!.currentPosition
                    binding.tvStart.text =
                        convertToMMSS(musicService!!.mediaPlayer!!.currentPosition.toString())
                    if (musicService!!.mediaPlayer!!.isPlaying)
                        ivPlayPause.setImageResource(R.drawable.ic_pause)
                    else
                        ivPlayPause.setImageResource(R.drawable.ic_play)
                }
                Handler(Looper.getMainLooper()).postDelayed(this, 1000)
            }
        })
        runnable = Runnable {
            binding.apply {
                sbMusic.progress = musicService!!.mediaPlayer!!.currentPosition
                binding.tvStart.text =
                    convertToMMSS(musicService!!.mediaPlayer!!.currentPosition.toString())
                if (musicService!!.mediaPlayer!!.isPlaying)
                    ivPlayPause.setImageResource(R.drawable.ic_pause)
                else
                    ivPlayPause.setImageResource(R.drawable.ic_play)
                handler.postDelayed(runnable, 1000)
            }
            handler.postDelayed(runnable, 1000)
        }

        binding.sbMusic.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                if (p2)
                    musicService!!.mediaPlayer!!.seekTo(progress)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
    }

    private fun setMusicPlayer() {
        if (musicService!!.mediaPlayer == null)
            musicService!!.mediaPlayer = MediaPlayer()
        currentSong = songsList[position]

        binding.apply {
            tvSongTitle.text = currentSong.title
            tvTotalTime.text = convertToMMSS(currentSong.duration.toString())
            ivNext.setOnClickListener { nextMusic() }
            ivPrev.setOnClickListener { prevMusic() }
            ivPlayPause.setOnClickListener { pausePlayMusic() }
        }
        playMusic()
        changeProgress()
    }

    private fun playMusic() {
        musicService!!.mediaPlayer!!.reset()
        try {
            musicService!!.mediaPlayer!!.setDataSource(currentSong.path)
            musicService!!.mediaPlayer!!.prepare()
            musicService!!.mediaPlayer!!.start()
            binding.apply {
                sbMusic.progress = 0
                sbMusic.max = musicService!!.mediaPlayer!!.duration
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun pausePlayMusic() {
        if (musicService!!.mediaPlayer!!.isPlaying) {
            musicService!!.mediaPlayer!!.pause()
            binding.ivPlayPause.setImageResource(R.drawable.ic_pause)
        } else {
            musicService!!.mediaPlayer!!.start()
            binding.ivPlayPause.setImageResource(R.drawable.ic_play)
        }
    }

    private fun prevMusic() {
        if (position == 0)
            return
        position--
        musicService!!.mediaPlayer!!.reset()
        setMusicPlayer()
    }

    private fun nextMusic() {
        if (position == songsList.size - 1)
            return
        position++
        musicService!!.mediaPlayer!!.reset()
        setMusicPlayer()
    }


    private fun convertToMMSS(duration: String): String {
        val milliseconds = duration.toLong()

        // long minutes = (milliseconds / 1000) / 60;
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % TimeUnit.HOURS.toMinutes(1)

        // long seconds = (milliseconds / 1000);
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % TimeUnit.MINUTES.toSeconds(1)
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currentService()
        setMusicPlayer()
        musicService!!.showNotification()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicService = null
    }

}