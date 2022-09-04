package com.example.mmpplayer.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mmpplayer.R
import com.example.mmpplayer.databinding.FragmentAllMediaBinding
import com.example.mmpplayer.video.MediaViewModel
import com.example.mmpplayer.video.MediaViewModelFactory
import com.example.mmpplayer.adapters.MediaListAdapter

class AllMediaFragment : Fragment() {

    private var _binding: FragmentAllMediaBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MediaListAdapter
    private lateinit var viewModel: MediaViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAllMediaBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = MediaViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), factory).get(MediaViewModel::class.java)

        val mediaType = arguments?.getString("video_music_item").toString()
        adapter = MediaListAdapter(requireActivity(), requireContext(),mediaType,"")
        if(mediaType == "Videos")
            getAllVideos()
        else
            getAllMusic()

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_mediaFragmentHolder_to_homeFragment)
                }
            }
            )
    }

    private fun getAllVideos() {
        if (viewModel.allVideosList.value?.isEmpty() == true)
            binding.tvNoMedia.apply {
                visibility = View.VISIBLE
                text = "No videos found..."
            }
        else {
            setAdapter()
        }
        viewModel.allVideosList.observe(viewLifecycleOwner, Observer {
            adapter.differ.submitList(it)
        })
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getAllMusic() {
        if (viewModel.allMusicList.value?.isEmpty() == true)
            binding.tvNoMedia.apply {
                visibility = View.VISIBLE
                text = "No music found..."
            }
        else {
            setAdapter()
        }
        viewModel.allMusicList.observe(viewLifecycleOwner, Observer {
            adapter.differ.submitList(it)
        })
    }

    private fun setAdapter() {
        binding.rvMedia.layoutManager = LinearLayoutManager(context)
        binding.rvMedia.adapter = adapter
    }

}