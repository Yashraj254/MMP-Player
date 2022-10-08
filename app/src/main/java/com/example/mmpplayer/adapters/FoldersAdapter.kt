package com.example.mmpplayer.adapters

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mmpplayer.R
import com.example.mmpplayer.databinding.MusicItemBinding
import com.example.mmpplayer.model.Folder

class FoldersAdapter(private val mediaType: String, private val activity: Activity) :
    RecyclerView.Adapter<FoldersAdapter.FoldersViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<Folder>() {
        override fun areItemsTheSame(oldItem: Folder, newItem: Folder): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Folder, newItem: Folder): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoldersViewHolder {
        val binding: MusicItemBinding =
            MusicItemBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)
        return FoldersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoldersViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
        holder.itemView.setOnClickListener {


            val foldersList = ArrayList(differ.currentList)
            val bundle = Bundle().apply {

                putString("folder_id", foldersList[position].id)
                putString("folder_name", foldersList[position].folderName)
            }
            when (mediaType) {
                "Videos" -> Navigation.findNavController(it)
                    .navigate(R.id.action_navigation_video_folders_to_specificFolderVideosFragment,
                        bundle)
                "Music" -> Navigation.findNavController(it)
                    .navigate(R.id.action_navigation_folders_to_specificFolderMediaFragment,
                        bundle)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class FoldersViewHolder(private val binding: MusicItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: Folder) {
            binding.apply {
                tvMusicName.text = model.folderName
                ivMusic.setImageResource(R.drawable.ic_folders)

            }
        }
    }
}