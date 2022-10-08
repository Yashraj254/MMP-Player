package com.example.mmpplayer.music.folders

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mmpplayer.adapters.FoldersAdapter
import com.example.mmpplayer.databinding.FragmentMusicFoldersBinding
import com.example.mmpplayer.model.MediaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MusicFoldersFragment : Fragment() {
    private var _binding: FragmentMusicFoldersBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FoldersAdapter

    //    private lateinit var viewModel: MediaViewModel
    private val viewModel by viewModels<MediaViewModel>()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        // Inflate the layout for this fragment
        _binding = FragmentMusicFoldersBinding.inflate(inflater, container, false)
        val view = binding.root
        requireActivity().title = "Music Folders"

        adapter = FoldersAdapter("Music", requireActivity())
        getMusicFolders()

        return view
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getMusicFolders() {
        if (viewModel.musicFoldersList.value?.isEmpty() == true)
            binding.tvNoFolders.visibility = View.VISIBLE
        else {
            setAdapter()
        }
        viewModel.musicFoldersList.observe(viewLifecycleOwner, Observer {
            adapter.differ.submitList(it)
        })
    }

    private fun setAdapter() {
        binding.rvMusicFolders.layoutManager = LinearLayoutManager(context)
        binding.rvMusicFolders.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}