package com.example.mmpplayer.video

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mmpplayer.adapters.MediaListAdapter
import com.example.mmpplayer.databinding.FragmentAllVideosBinding
import com.example.mmpplayer.model.MediaViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AllVideosFragment : Fragment() {

    private var _binding: FragmentAllVideosBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MediaListAdapter
//    private lateinit var viewModel: MediaViewModel
    private  val viewModel by viewModels<MediaViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAllVideosBinding.inflate(inflater, container, false)
        requireActivity().title = "All Videos"

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = MediaListAdapter( requireActivity(), requireContext(), "Videos", "")
        getAllVideos()

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
        viewModel.allVideosList.observe(viewLifecycleOwner) {
            adapter.differ.submitList(it)
        }
    }

    private fun setAdapter() {
        binding.rvMedia.layoutManager = LinearLayoutManager(context)
        binding.rvMedia.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}