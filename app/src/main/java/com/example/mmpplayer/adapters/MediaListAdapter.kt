package com.example.mmpplayer.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import android.text.format.Formatter.formatFileSize
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mmpplayer.music.MusicPlayerActivity
import com.example.mmpplayer.R
import com.example.mmpplayer.video.VideoPlayerActivity
import com.example.mmpplayer.databinding.VideoItemBinding
import com.example.mmpplayer.model.Media
import com.example.mmpplayer.model.Playlist
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class MediaListAdapter(
    private val activity: Activity,
    private val context: Context,
    private val mediaType: String,
    private val folderId: String,
) :
    RecyclerView.Adapter<MediaListAdapter.VideoViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<Media>() {
        override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MediaListAdapter.VideoViewHolder {
        val binding: VideoItemBinding =
            VideoItemBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaListAdapter.VideoViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bind(currentItem)

        holder.itemView.setOnClickListener {
            val mediaList: ArrayList<Media> = ArrayList(differ.currentList)
            if (mediaType == "Videos") {
                val intent = Intent(activity, VideoPlayerActivity::class.java)
                intent.putParcelableArrayListExtra("videos_list", mediaList)
                intent.putExtra("video_position", position)
                intent.putExtra("folder_id", folderId)
                activity.startActivity(intent)

            }
            if (mediaType == "Music") {
                val intent = Intent(activity, MusicPlayerActivity::class.java)
                intent.putParcelableArrayListExtra("songs_list", mediaList)
                intent.putExtra("music_position", position)
                activity.startActivity(intent)
            }
        }
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class VideoViewHolder(private val binding: VideoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: Media) {
            binding.apply {
                tvVideoTitle.text = model.title
                tvVideoDate.text = getDate(model.dateAdded)
                tvVideoSize.text = formatFileSize(model.size)
                if (mediaType == "Videos")
                    Glide.with(context)
                        .asBitmap()
                        .load(model.uri)
                        .into(ivVideoThumb);
                else
                    ivVideoThumb.setImageResource(R.drawable.music_image)

            }
        }

        private fun getDate(timestamp: Long): String {
            val calendar = Calendar.getInstance(Locale.ENGLISH)
            calendar.timeInMillis = timestamp * 1000L
            return DateFormat.format("dd-MM-yyyy", calendar).toString()
        }


        private fun formatFileSize(size: Long): String {
            var hrSize: String? = null
            val b = size.toDouble()
            val k = size / 1024.0
            val m = size / 1024.0 / 1024.0
            val g = size / 1024.0 / 1024.0 / 1024.0
            val dec = DecimalFormat("0.00")
            hrSize = if (g > 1) {
                dec.format(g).plus(" GB")
            } else if (m > 1) {
                dec.format(m).plus(" MB")
            } else if (k > 1) {
                dec.format(k).plus(" KB")
            } else {
                dec.format(b).plus(" Bytes")
            }
            return hrSize
        }
    }
}