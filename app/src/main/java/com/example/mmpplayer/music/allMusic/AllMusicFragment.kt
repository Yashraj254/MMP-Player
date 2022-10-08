package com.example.mmpplayer.music.allMusic

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
import com.example.mmpplayer.adapters.MediaListAdapter
import com.example.mmpplayer.databinding.FragmentAllMusicBinding
import com.example.mmpplayer.model.MediaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllMusicFragment : Fragment() {

    private var _binding: FragmentAllMusicBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MediaListAdapter
//    private lateinit var viewModel: MediaViewModel
private  val viewModel by viewModels<MediaViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAllMusicBinding.inflate(inflater, container, false)
        requireActivity().title = "All Music"

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MediaListAdapter(requireActivity(), requireContext(),"Music","")
            getAllMusic()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}