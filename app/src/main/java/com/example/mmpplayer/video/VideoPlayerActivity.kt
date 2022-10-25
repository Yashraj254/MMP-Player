package com.example.mmpplayer.video

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.R
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.mmpplayer.R
import com.example.mmpplayer.databinding.ActivityVideoPlayerBinding
import com.example.mmpplayer.model.Media
import com.github.vkay94.dtpv.youtube.YouTubeOverlay
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import java.io.File
import kotlin.math.abs

class VideoPlayerActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private var _binding: ActivityVideoPlayerBinding? = null
    private val binding get() = _binding!!
    private lateinit var player: ExoPlayer
    private lateinit var videosList: ArrayList<Media>
    private lateinit var folderId: String
    private var position = 0
    private var isFullScreen = false
    private lateinit var gestureDetectorCompat: GestureDetectorCompat
    private var brightness: Int = 0
    private var volume: Int = 0
    private lateinit var audioManager: AudioManager

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        _binding = ActivityVideoPlayerBinding.inflate(layoutInflater)

        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setTheme(com.example.mmpplayer.R.style.Theme)
        setContentView(binding.root)
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager;

        gestureDetectorCompat = GestureDetectorCompat(this, this)
        if (intent.data?.scheme.contentEquals("content")) {
            videosList = ArrayList()
            position = 0
            val cursor = contentResolver.query(intent.data!!,
                arrayOf(MediaStore.Video.Media.DATA),
                null,
                null,
                null)
            cursor?.let {
                it.moveToFirst()
                val path = it.getString(it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                val file = File(path)
                val video = Media(0,id = "",
                    title = file.name,
                    path = path,
                    duration = 0L,
                    folderName = "",
                    size = 0L,
                    uri = Uri.fromFile(file).toString(),
                    dateAdded = 0L)
                videosList.add(video)
                cursor.close()
            }
        } else {
            videosList =
                intent.getParcelableArrayListExtra<Media>("videos_list") as ArrayList<Media>
            position = intent.getIntExtra("video_position", 0)
            folderId = intent.getStringExtra("folder_id").toString()
        }

        player = ExoPlayer.Builder(this).build()
        doubleTapEnable()
        createPlayer()

        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == Player.STATE_ENDED) {
                    binding.playPauseButton.setImageResource(com.example.mmpplayer.R.drawable.ic_video_play)
                }
            }
        })
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
        binding.rotateButton.setOnClickListener {
            requestedOrientation = if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            else
                ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        }
    }
    override fun onResume() {
        super.onResume()
        if (brightness != 0)
            setScreenBrightness(brightness)
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

            if (visibility == View.VISIBLE) {
                WindowInsetsControllerCompat(window,
                    binding.root).show(WindowInsetsCompat.Type.systemBars())
            } else {

                WindowInsetsControllerCompat(window,
                    binding.root).hide(WindowInsetsCompat.Type.systemBars())
            }
            topController.visibility = visibility
            bottomController.visibility = visibility
        }
    }


    private fun playVideo() {
        player.play()
        binding.playPauseButton.setImageResource(com.example.mmpplayer.R.drawable.ic_video_pause)
    }

    private fun pauseVideo() {
        player.pause()
        binding.playPauseButton.setImageResource(com.example.mmpplayer.R.drawable.ic_video_play)
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
            binding.fullScreenButton.setImageResource(com.example.mmpplayer.R.drawable.ic_exit_fullscreen)
        } else {
            binding.videoPlayer.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            binding.fullScreenButton.setImageResource(com.example.mmpplayer.R.drawable.ic_video_fullscreen)

        }
    }


    private fun doubleTapEnable() {
        binding.apply {
            videoPlayer.player = player

            ytOverlay.performListener(object : YouTubeOverlay.PerformListener {
                override fun onAnimationEnd() {
                    ytOverlay.visibility = View.GONE
                }

                override fun onAnimationStart() {
                    ytOverlay.visibility = View.VISIBLE
                }
            })
            ytOverlay.player(player)
            videoPlayer.setOnTouchListener { view, motionEvent ->
                gestureDetectorCompat.onTouchEvent(motionEvent)
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    binding.apply {
                        btnBrightness.visibility = View.GONE
                        btnVolume.visibility = View.GONE
                    }
                }
                return@setOnTouchListener false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        supportActionBar?.show()
        player.release()
    }









    private fun setScreenBrightness(value: Int) {
        val d = 1.0f / 100
        val lp = this.window.attributes
        lp.screenBrightness = d * value
        this.window.attributes = lp
    }

    override fun onDown(p0: MotionEvent): Boolean = false

    override fun onShowPress(p0: MotionEvent) = Unit

    override fun onSingleTapUp(p0: MotionEvent): Boolean = false

    override fun onScroll(event: MotionEvent, p1: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        if (abs(distanceX) < abs(distanceY)) {
            if (event!!.x < screenWidth / 2) {
                binding.apply {
                    btnBrightness.visibility = View.VISIBLE
                    btnVolume.visibility = View.GONE
                    val increase = distanceY > 0
                    val newValue = if (increase) brightness + 1 else brightness - 1
                    if (newValue in 0..100) brightness = newValue
                    btnBrightness.text = brightness.toString()
                    setScreenBrightness(brightness)
                }
            } else {
                binding.apply {
                    btnBrightness.visibility = View.GONE
                    btnVolume.visibility = View.VISIBLE
                    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

                    val increase = distanceY > 0
                    val newValue = if (increase) volume + 1 else volume - 1
                    if (newValue in 0..maxVolume) volume = newValue
                    btnVolume.text = volume.toString()
                    audioManager.setStreamVolume(
                        AudioManager.STREAM_MUSIC,           // or STREAM_ACCESSIBILITY, STREAM_ALARM, STREAM DTMF, STREAM_NOTIFCATION, STREAM_RING, STREAM_SYSTEM, STREAM_VOICE_CALL
                        volume,
                        // or ADJUST_RAISE, ADJUST_SAME
                        0                                    // or FLAG_PLAY_SOUND, FLAG_REMOVE_SOUND_AND_VIBRATE, FLAG_SHOW_UI, FLAG_VIBRATE, FLAG_ALLOW_RINGER_MODES
                    )
                    // setVolume(volume)
                }
            }
        }
        return true
    }

    override fun onLongPress(p0: MotionEvent)= Unit

    override fun onFling(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean = false


}

