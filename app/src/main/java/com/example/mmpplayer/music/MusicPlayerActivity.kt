package com.example.mmpplayer.music

import android.content.ComponentName
import android.content.DialogInterface
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.mmpplayer.R
import com.example.mmpplayer.databinding.ActivityMusicPlayerBinding
import com.example.mmpplayer.model.Media
import com.example.mmpplayer.model.MusicPlayerViewModel
import com.example.mmpplayer.model.Playlist
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


@AndroidEntryPoint
class MusicPlayerActivity : AppCompatActivity(), ServiceConnection {
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler(Looper.getMainLooper())
    private lateinit var currentSong: Media
    private lateinit var allPlaylists: ArrayList<Playlist>
    private val viewModel by viewModels<MusicPlayerViewModel>()
    private val playlistMusic = ArrayList<Media>()

    companion object {
        var musicService: MusicService? = null
        lateinit var songsList: ArrayList<Media>
        var position: Int = 0
        var isPlaying = false
        private var _binding: ActivityMusicPlayerBinding? = null

        val binding get() = _binding!!

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intents = Intent(this, MusicService::class.java)
        bindService(intents, this, BIND_AUTO_CREATE)
        startService(intents)
        binding.tvSongTitle.isSelected = true
        songsList = intent.getSerializableExtra("songs_list") as ArrayList<Media>
        position = intent.getIntExtra("music_position", 0)
        currentSong = songsList[position]

        playlistMusic.add(currentSong)
        viewModel.getAllPlaylists().observe(this, Observer {
            allPlaylists = it as ArrayList<Playlist>
        })
        binding.apply {
            viewModel.getAllFavourites().observe(this@MusicPlayerActivity, Observer {
                for(favourites in it){
                    if (favourites.title == currentSong.title) {
                        ivFavorites.visibility = View.VISIBLE
                        ivAddToFavorite.visibility = View.GONE

                    } else {
                        ivAddToFavorite.visibility = View.VISIBLE
                        ivFavorites.visibility = View.GONE

                    }
                }})

            ivAddToFavorite.setOnClickListener {
                ivAddToFavorite.visibility = View.GONE
                ivFavorites.visibility = View.VISIBLE
                viewModel.addToFavourites(songsList[position])
            }
            ivFavorites.setOnClickListener {
                ivAddToFavorite.visibility = View.VISIBLE
                ivFavorites.visibility = View.GONE
                viewModel.removeFromFavourites(songsList[position])
            }
            ivAddToPlaylist.setOnClickListener {
                showAlertDialog()
            }
        }
    }

    private fun changeProgress() {
        this.runOnUiThread(object : Runnable {
            override fun run() {
                binding.apply {
                    sbMusic.progress = musicService!!.mediaPlayer!!.currentPosition
                    binding.tvStart.text =
                        convertToMMSS(musicService!!.mediaPlayer!!.currentPosition.toString())
                    if (musicService!!.mediaPlayer!!.isPlaying) {
                        ivPlayPause.setImageResource(R.drawable.ic_pause)
                        musicService!!.showNotification(R.drawable.ic_pause)
                    }
                    else {
                        ivPlayPause.setImageResource(R.drawable.ic_play)
                        musicService!!.showNotification(R.drawable.ic_play)
                    }
                }
                Handler(Looper.getMainLooper()).postDelayed(this, 1000)
            }
        })
        runnable = Runnable {
            binding.apply {
                sbMusic.progress = musicService!!.mediaPlayer!!.currentPosition
                binding.tvStart.text =
                    convertToMMSS(musicService!!.mediaPlayer!!.currentPosition.toString())
                if (musicService!!.mediaPlayer!!.isPlaying) {
                    ivPlayPause.setImageResource(R.drawable.ic_pause)
                    musicService!!.showNotification(R.drawable.ic_pause)
                }
                else {
                    ivPlayPause.setImageResource(R.drawable.ic_play)
                    musicService!!.showNotification(R.drawable.ic_play)

                }
                handler.postDelayed(runnable, 1000)
            }
            handler.postDelayed(runnable, 1000)
        }

        binding.sbMusic.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                if (p2) {
                    musicService!!.mediaPlayer!!.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
    }

    private fun setMusicPlayer() {
        binding.apply {
            viewModel.getAllFavourites().observe(this@MusicPlayerActivity, Observer {
                for(favourites in it){
                    if (favourites.title == currentSong.title) {
                        ivFavorites.visibility = View.VISIBLE
                        ivAddToFavorite.visibility = View.GONE

                    } else {
                        ivAddToFavorite.visibility = View.VISIBLE
                        ivFavorites.visibility = View.GONE

                    }
                }

            })
        }

        if (musicService!!.mediaPlayer == null)
            musicService!!.mediaPlayer = MediaPlayer()

        binding.apply {
            tvSongTitle.text = songsList[position].title
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
        isPlaying = if (musicService!!.mediaPlayer!!.isPlaying) {
            musicService!!.mediaPlayer!!.pause()
            musicService!!.showNotification(R.drawable.ic_play)
            true
        } else {
            musicService!!.mediaPlayer!!.start()
            musicService!!.showNotification(R.drawable.ic_pause)
            false
        }
    }

    private fun prevMusic() {

        if (position == 0)
            return
        position--
        currentSong = songsList[position]
        musicService!!.mediaPlayer!!.reset()
        setMusicPlayer()
        musicService!!.showNotification(R.drawable.ic_pause)

    }

    private fun nextMusic() {

        if (position == songsList.size - 1)
            return
        position++
        currentSong = songsList[position]
        musicService!!.mediaPlayer!!.reset()
        setMusicPlayer()
        musicService!!.showNotification(R.drawable.ic_pause)
    }

    private fun showAlertDialog() {
        val alertDialog = AlertDialog.Builder(this)

        Log.d("MyTag", allPlaylists.toString())
        alertDialog.setTitle("All Playlists")

        if (allPlaylists.isNotEmpty()) {
            val playlistsName = ArrayList<String>()
            for (i in allPlaylists) {
                playlistsName.add(i.playlistName)
            }
            val items = playlistsName.toTypedArray()
            val checkedItemsList = ArrayList<Playlist>()
            val checkedItems = booleanArrayOf(false, false, false, false, false, false)
            alertDialog.setMultiChoiceItems(items, checkedItems,
                OnMultiChoiceClickListener { dialog, which, isChecked ->
                    if (isChecked) {
                        allPlaylists[which].playlistMusic.add(currentSong)
                        checkedItemsList.add(allPlaylists[which])
                    }

                }).setPositiveButton("Save") { dialogInterface, i ->
                for (playlist in checkedItemsList)
                    viewModel.addToPlaylist(playlist)

            }.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->

            })
        } else {
            alertDialog.setMessage("No playlists available!!")
        }
        alertDialog.create().show()

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
        musicService!!.showNotification(R.drawable.ic_pause)
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicService = null
    }
}

fun setSongPosition(increment: Boolean) {
    if (increment) {
        if (MusicPlayerActivity.songsList.size - 1 == MusicPlayerActivity.position)
            MusicPlayerActivity.position = 0
        else ++MusicPlayerActivity.position
    } else {
        if (0 == MusicPlayerActivity.position)
            MusicPlayerActivity.position = MusicPlayerActivity.songsList.size - 1
        else --MusicPlayerActivity.position
    }
}