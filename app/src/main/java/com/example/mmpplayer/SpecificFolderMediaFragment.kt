package com.example.mmpplayer

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mmpplayer.databinding.FragmentSpecificFolderMediaBinding
import com.example.mmpplayer.video.MediaViewModel
import com.example.mmpplayer.video.MediaViewModelFactory
import com.example.mmpplayer.adapters.MediaListAdapter


class SpecificFolderMediaFragment : Fragment() {

    private var _binding: FragmentSpecificFolderMediaBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MediaListAdapter
    private lateinit var viewModel: MediaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSpecificFolderMediaBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = MediaViewModelFactory(requireActivity().application)
        viewModel =
            ViewModelProvider(this, factory).get(MediaViewModel::class.java)

        val mediaType = requireArguments().getString("media_type")!!
        val folderId = requireArguments().getString("folder_id")!!

        adapter = MediaListAdapter(requireActivity(), requireContext(), mediaType, folderId)

        if (mediaType == "Videos")
            getVideos( folderId)
        else
            getMusic(folderId)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val bundle = Bundle()
                if(mediaType == "Videos")
                bundle.putBoolean("open_video_folder_fragment",true)
                else
                    bundle.putBoolean("open_music_folder_fragment",true)
                Navigation.findNavController(view).navigate(R.id.action_specificFolderMediaFragment_to_mediaFragmentHolder,bundle)
            }
        })

    }

    private fun getVideos(folderId: String) {

        if (viewModel.getSpecificFolderVideos(folderId).value?.isEmpty() == true)
            binding.tvNoFolderVideos.visibility = View.VISIBLE
        else {
            setAdapter()
        }
        viewModel.getSpecificFolderVideos(folderId)
            .observe(viewLifecycleOwner, Observer {
                adapter.differ.submitList(it)
            })
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
        binding.rvFolderVideos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFolderVideos.adapter = adapter
    }
}
