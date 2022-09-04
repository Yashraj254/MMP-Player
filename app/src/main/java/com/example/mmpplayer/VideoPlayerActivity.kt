package com.example.mmpplayer

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.mmpplayer.databinding.ActivityVideoPlayerBinding
import com.example.mmpplayer.model.Media
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout

class VideoPlayerActivity : AppCompatActivity() {

    private var _binding: ActivityVideoPlayerBinding? = null
    private val binding get() = _binding!!
    private lateinit var player: ExoPlayer
    private lateinit var videosList: ArrayList<Media>
    private lateinit var folderId: String
    private var position = 0
    private var isFullScreen = false

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        _binding = ActivityVideoPlayerBinding.inflate(layoutInflater)

        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setTheme(R.style.Theme)
        setContentView(binding.root)

        intent.extras
        videosList = intent.getParcelableArrayListExtra<Media>("videos_list") as ArrayList<Media>
        position = intent.getIntExtra("video_position", 0)
        folderId = intent.getStringExtra("folder_id").toString()
        player = ExoPlayer.Builder(this).build()
        binding.videoPlayer.player = player
        createPlayer()
        binding.apply {
            playPauseButton.setOnClickListener { if (player.isPlaying) pauseVideo() else playVideo() }
            nextButton.setOnClickListener { nextVideo() }
            prevButton.setOnClickListener { prevVideo() }
            backButton.setOnClickListener { }
            videoPlayer.setControllerVisibilityListener { visibility ->
                changeVisibility(visibility)
            }
            fullScreenButton.setOnClickListener {
                if (isFullScreen) {
                    isFullScreen = false
                    enterFullScreen(false)
                } else {
                    isFullScreen = true
                    enterFullScreen(true)
                }
            }
        }
    }

    private fun createPlayer() {
        binding.tvVideoName.isSelected = true
        binding.tvVideoName.text = videosList[position].title
        val mediaItem = MediaItem.fromUri(videosList[position].path)
        player.setMediaItem(mediaItem)
        player.prepare()
        playVideo()

    }


    private fun changeVisibility(visibility: Int) {

        binding.apply {

            if (visibility == View.VISIBLE){
                WindowInsetsControllerCompat(window,
                    binding.root).show(WindowInsetsCompat.Type.systemBars())
}
            else{

                WindowInsetsControllerCompat(window,
                    binding.root).hide(WindowInsetsCompat.Type.systemBars())}
            topController.visibility = visibility
            bottomController.visibility = visibility
        }
    }


    private fun playVideo() {
        player.play()
        binding.playPauseButton.setImageResource(R.drawable.ic_video_pause)
    }

    private fun pauseVideo() {
        player.pause()
        binding.playPauseButton.setImageResource(R.drawable.ic_video_play)
    }

    private fun nextVideo() {
        if (videosList.size - 1 > position) {
            position++
            val mediaItem = MediaItem.fromUri(videosList[position].path)
            player.setMediaItem(mediaItem)
            player.prepare()
            playVideo()
        }
    }

    private fun prevVideo() {
        if (position != 0) {
            position--
            val mediaItem = MediaItem.fromUri(videosList[position].path)
            player.setMediaItem(mediaItem)
            player.prepare()
            playVideo()
        }
    }

    private fun enterFullScreen(enable: Boolean) {
        if (enable) {
            binding.videoPlayer.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            binding.fullScreenButton.setImageResource(R.drawable.ic_exit_fullscreen)
        } else {
            binding.videoPlayer.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            binding.fullScreenButton.setImageResource(R.drawable.ic_video_fullscreen)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        supportActionBar?.show()
        player.release()
    }
}

