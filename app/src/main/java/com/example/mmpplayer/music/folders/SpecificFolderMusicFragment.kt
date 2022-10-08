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
import com.example.mmpplayer.adapters.MediaListAdapter
import com.example.mmpplayer.databinding.FragmentSpecificFolderMusicBinding
import com.example.mmpplayer.model.MediaViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpecificFolderMusicFragment : Fragment() {

    private var _binding: FragmentSpecificFolderMusicBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MediaListAdapter

    //    private lateinit var viewModel: MediaViewModel
    private val viewModel by viewModels<MediaViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSpecificFolderMusicBinding.inflate(layoutInflater, container, false)

        val navBar = requireActivity().findViewById<BottomNavigationView>(com.example.mmpplayer.R.id.music_nav_view)
        navBar.visibility = View.GONE
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val folderId = requireArguments().getString("folder_id")!!
        val folderName = requireArguments().getString("folder_name")!!
        requireActivity().title = folderName
        adapter = MediaListAdapter(requireActivity(), requireContext(), "Music", folderId)
        getMusic(folderId)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getMusic(folderId: String) {
        if (viewModel.getSpecificFolderMusic(folderId).value?.isEmpty() == true)
            binding.tvNoFolderVideos.visibility = View.VISIBLE
        else {
            setAdapter()
        }
        viewModel.getSpecificFolderMusic(folderId)
            .observe(viewLifecycleOwner, Observer {
                adapter.differ.submitList(it)
            })
    }

    private fun setAdapter() {
        binding.rvFolderMusic.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFolderMusic.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val navBar = requireActivity().findViewById<BottomNavigationView>(com.example.mmpplayer.R.id.music_nav_view)
        navBar.visibility = View.VISIBLE
        _binding = null
    }
}
