package com.example.mmpplayer.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mmpplayer.R
import com.example.mmpplayer.databinding.VideoItemBinding
import com.example.mmpplayer.model.Media
import com.example.mmpplayer.music.MusicPlayerActivity
import com.example.mmpplayer.video.VideoPlayerActivity
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class FavouritesAdapter(
    private val showDeleteMenu: (Boolean) -> Unit,
    private val activity: Activity
) :
    RecyclerView.Adapter<FavouritesAdapter.VideoViewHolder>() {
    private var selectedItemsList = ArrayList<Media>()
    private var isEnable = false
    private var canNavigate = true
    private val callback = object : DiffUtil.ItemCallback<Media>() {
        override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding: VideoItemBinding =
            VideoItemBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bind(currentItem)
        if(!currentItem.isSelected)
            holder.btnCheck.visibility = View.GONE
        holder.itemView.setOnClickListener {
            val mediaList: ArrayList<Media> = ArrayList(differ.currentList)

            if (canNavigate) {
                val intent = Intent(activity, MusicPlayerActivity::class.java)
                intent.putParcelableArrayListExtra("songs_list", mediaList)
                intent.putExtra("music_position", position)
                activity.startActivity(intent)
            }

            if (selectedItemsList.contains(currentItem)) {
                holder.btnCheck.visibility = View.GONE
                selectedItemsList.remove(currentItem)
                holder.itemView.isSelected = false
                if (selectedItemsList.isEmpty()) {
                    showDeleteMenu(false)
                    isEnable = false
                    canNavigate = true
                }
            } else if (isEnable)
                selectItem(holder, position, currentItem)
        }
        holder.itemView.setOnLongClickListener {
            selectItem(holder, position, currentItem)
            canNavigate = false
            true
        }
    }

    private fun selectItem(
        holder: VideoViewHolder,
        position: Int,
        favourties: Media,
    ) {
        isEnable = true
        selectedItemsList.add(favourties)
        favourties.isSelected = true
        holder.btnCheck.visibility = View.VISIBLE
        showDeleteMenu(true)
    }

    fun getSelectedItemsList() = selectedItemsList

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class VideoViewHolder(private val binding: VideoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var btnCheck = binding.ivSelectedItem
        fun bind(model: Media) {
            binding.apply {
                tvVideoTitle.text = model.title
                tvVideoDate.text = getDate(model.dateAdded)
                tvVideoSize.text = formatFileSize(model.size)
                ivVideoThumb.setImageResource(R.drawable.music_image)
            }
        }

        private fun getDate(timestamp: Long): String {
            val calendar = Calendar.getInstance(Locale.ENGLISH)
            calendar.timeInMillis = timestamp * 1000L
            return DateFormat.format("dd-MM-yyyy", calendar).toString()
        }
    }

    fun formatFileSize(size: Long): String {
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