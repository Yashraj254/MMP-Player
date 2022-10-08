package com.example.mmpplayer.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mmpplayer.R
import com.example.mmpplayer.databinding.MusicItemBinding
import com.example.mmpplayer.model.Playlist
import java.lang.Compiler.enable

class PlaylistAdapter(private val showDeleteMenu: (Boolean) -> Unit) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {
    private var selectedItemsList = ArrayList<Playlist>()
    private var isEnable = false
    private var canNavigate = true
    private val callback = object : DiffUtil.ItemCallback<Playlist>() {
        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PlaylistAdapter.PlaylistViewHolder {
        val binding: MusicItemBinding =
            MusicItemBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistAdapter.PlaylistViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bind(currentItem)
        if(!currentItem.isSelected)
            holder.btnCheck.visibility = View.GONE
        holder.itemView.setOnClickListener {

            val foldersList = ArrayList(differ.currentList)
            val bundle = Bundle().apply {
                putLong("playlist_id", foldersList[position].id)
                putString("playlist_name", foldersList[position].playlistName)
            }
            if(canNavigate)
            Navigation.findNavController(it)
                .navigate(R.id.action_navigation_playlist_to_playlistsAllMusicFragment,
                    bundle)

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
        holder: PlaylistAdapter.PlaylistViewHolder,
        position: Int,
        playlist: Playlist,
    ) {
        isEnable = true
        selectedItemsList.add(playlist)
        playlist.isSelected = true
        holder.btnCheck.visibility = View.VISIBLE
        showDeleteMenu(true)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun getSelectedItemsList() = selectedItemsList

    inner class PlaylistViewHolder(private val binding: MusicItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var btnCheck = binding.btnCheck
        fun bind(model: Playlist) {
            binding.apply {
                tvMusicName.text = model.playlistName
                ivMusic.setImageResource(R.drawable.ic_folders)

            }
        }
    }
}