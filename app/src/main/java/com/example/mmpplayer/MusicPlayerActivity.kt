package com.example.mmpplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mmpplayer.databinding.ActivityMusicPlayerBinding
import com.example.mmpplayer.model.Media
import java.io.IOException
import java.util.concurrent.TimeUnit

class MusicPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusicPlayerBinding
    private lateinit var songsList: ArrayList<Media>
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler(Looper.getMainLooper())
    private val mediaPlayer: MediaPlayer = MyMediaPlayer.getInstance()
    private lateinit var currentSong: Media

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvSongTitle.isSelected = true
        songsList = intent.getSerializableExtra("songs_list") as ArrayList<Media>
        mediaPlayer.reset()
        setResourcesWithMusic()

        this.runOnUiThread(object : Runnable {
            override fun run() {
                binding.apply {
                    sbMusic.progress = mediaPlayer.currentPosition
                    binding.tvStart.text = convertToMMSS(mediaPlayer.currentPosition.toString())
                    if (mediaPlayer.isPlaying)
                        ivPlayPause.setImageResource(R.drawable.ic_pause)
                    else
                        ivPlayPause.setImageResource(R.drawable.ic_play)
                }
                Handler(Looper.getMainLooper()).postDelayed(this, 1000)
            }
        })
        runnable = Runnable {
            binding.apply {
                sbMusic.progress = mediaPlayer.currentPosition
                binding.tvStart.text = convertToMMSS(mediaPlayer.currentPosition.toString())
                if (mediaPlayer.isPlaying)
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
                    mediaPlayer.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
    }

    private fun setResourcesWithMusic() {
        currentSong = songsList.get(MyMediaPlayer.currentIndex)

        binding.apply {
            tvSongTitle.text = currentSong.title
            tvTotalTime.text = convertToMMSS(currentSong.duration.toString())
            ivNext.setOnClickListener { nextMusic() }
            ivPrev.setOnClickListener { prevMusic() }
            ivPlayPause.setOnClickListener { pausePlayMusic() }
        }
        playMusic()

    }

    private fun playMusic() {
        mediaPlayer.reset()
        try {
            mediaPlayer.setDataSource(currentSong.path)
            mediaPlayer.prepare()
            mediaPlayer.start()
            binding.apply {
                sbMusic.progress = 0
                sbMusic.max = mediaPlayer.duration
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun pausePlayMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            binding.ivPlayPause.setImageResource(R.drawable.ic_pause)
        } else {
            mediaPlayer.start()
            binding.ivPlayPause.setImageResource(R.drawable.ic_play)
        }
    }

    private fun prevMusic() {
        if (MyMediaPlayer.currentIndex == 0)
            return
        MyMediaPlayer.currentIndex--
        mediaPlayer.reset()
        setResourcesWithMusic()
    }

    private fun nextMusic() {
        if (MyMediaPlayer.currentIndex == songsList.size - 1)
            return
        MyMediaPlayer.currentIndex++
        mediaPlayer.reset()
        setResourcesWithMusic()
    }


    private fun convertToMMSS(duration: String): String {
        val milliseconds = duration.toLong()
        // long minutes = (milliseconds / 1000) / 60;
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % TimeUnit.HOURS.toMinutes(1)

        // long seconds = (milliseconds / 1000);
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % TimeUnit.MINUTES.toSeconds(1)
        return String.format("%02d:%02d", minutes, seconds)
    }
}