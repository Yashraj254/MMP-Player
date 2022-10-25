package com.example.mmpplayer.video

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
import com.example.mmpplayer.databinding.FragmentVideoFoldersBinding
import com.example.mmpplayer.model.MediaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoFoldersFragment : Fragment() {
    private var _binding: FragmentVideoFoldersBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FoldersAdapter
    private  val viewModel by viewModels<MediaViewModel>()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        // Inflate the layout for this fragment
        _binding = FragmentVideoFoldersBinding.inflate(inflater, container, false)
        val view = binding.root
        requireActivity().title = "Video Folders"

        adapter = FoldersAdapter("Videos", requireActivity())
        getVideoFolders()

        return view
    }

    @RequiresApi(Build.VERSION_CODES.Q)
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

    private fun setAdapter() {
        binding.rvVideoFolders.layoutManager = LinearLayoutManager(context)
        binding.rvVideoFolders.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}