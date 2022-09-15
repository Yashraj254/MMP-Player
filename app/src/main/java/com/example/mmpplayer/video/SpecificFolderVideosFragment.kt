package com.example.mmpplayer.video

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
import com.example.mmpplayer.adapters.MediaListAdapter
import com.example.mmpplayer.databinding.FragmentSpecificFolderVideosBinding
import com.example.mmpplayer.model.MediaViewModel
import com.example.mmpplayer.model.MediaViewModelFactory


class SpecificFolderVideosFragment : Fragment() {

    private var _binding: FragmentSpecificFolderVideosBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MediaListAdapter
    private lateinit var viewModel: MediaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSpecificFolderVideosBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = MediaViewModelFactory(requireActivity().application)
        viewModel =
            ViewModelProvider(this, factory).get(MediaViewModel::class.java)


        val folderId = requireArguments().getString("folder_id")!!

        adapter = MediaListAdapter(requireActivity(), requireContext(), "Videos", folderId)


        getVideos(folderId)


//        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
//            object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    val bundle = Bundle()
//                    if (mediaType == "Videos")
//                        bundle.putBoolean("open_video_folder_fragment", true)
//                    else
//                        bundle.putBoolean("open_music_folder_fragment", true)
//                    Navigation.findNavController(view)
//                        .navigate(R.id.action_specificFolderMediaFragment_to_mediaFragmentHolder,
//                            bundle)
//                }
//            })

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

    private fun setAdapter() {
        binding.rvFolderVideos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFolderVideos.adapter = adapter
    }
}
