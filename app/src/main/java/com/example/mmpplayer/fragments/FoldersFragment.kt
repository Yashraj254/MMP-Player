package com.example.mmpplayer.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mmpplayer.databinding.FragmentMediaAllFoldersBinding
import com.example.mmpplayer.video.MediaViewModel
import com.example.mmpplayer.video.MediaViewModelFactory
import com.example.mmpplayer.adapters.FoldersAdapter


class FoldersFragment : Fragment() {
    private var _binding: FragmentMediaAllFoldersBinding? = null
    private val binding get() = _binding!!
    private var goToHome = false
    private lateinit var adapter: FoldersAdapter
    private lateinit var viewModel: MediaViewModel

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentMediaAllFoldersBinding.inflate(inflater, container, false)
        val view = binding.root

        val factory = MediaViewModelFactory(requireActivity().application)
        viewModel =
            ViewModelProvider(requireActivity(), factory).get(MediaViewModel::class.java)
        val mediaType = arguments?.getString("video_music_item")
        adapter = FoldersAdapter(mediaType!!,requireActivity())
        if (mediaType == "Videos")
            getVideoFolders()
        else
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

    private fun getVideoFolders() {
        if (viewModel.videoFoldersList.value?.isEmpty() == true)
            binding.tvNoFolders.visibility = View.VISIBLE
        else {
            setAdapter()
        }
        viewModel.videoFoldersList.observe(viewLifecycleOwner, Observer {
            adapter.differ.submitList(it)
        })
    }

    override fun onResume() {
        super.onResume()
        Log.i("Fragments", "onResume: Folders Fragment ")

    }
    override fun onStop() {
        super.onStop()
        Log.i("Fragments", "onStop: Folders Fragment ")
        goToHome = false
    }
    private fun setAdapter() {
        binding.rvMediaFolders.layoutManager = LinearLayoutManager(context)
        binding.rvMediaFolders.adapter = adapter
    }
}