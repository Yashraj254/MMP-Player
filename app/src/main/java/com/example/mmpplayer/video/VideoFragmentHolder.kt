package com.example.mmpplayer.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.mmpplayer.R
import com.example.mmpplayer.databinding.FragmentVideosHolderBinding


class VideoFragmentHolder : Fragment() {

    private var _binding: FragmentVideosHolderBinding? = null
    private val binding get() = _binding!!
    lateinit var mediaType: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVideosHolderBinding.inflate(inflater, container, false)
//        val args = requireArguments().getString("show_folder_fragment")
//        val openVideoFolderFragment =
//            requireArguments().getBoolean("open_video_folder_fragment", false)
//        val openMusicFolderFragment =
//            requireArguments().getBoolean("open_music_folder_fragment", false)
//        mediaType = requireArguments().getSerializable("media_type").toString()
//
//        if (openVideoFolderFragment) {
//            mediaType = "Videos"
//            setFragment(FoldersFragment())
//            binding.bottomNavigationView.selectedItemId = R.id.all_video_folders
//        } else
//            if (openMusicFolderFragment) {
//                mediaType = "Music"
//                setFragment(FoldersFragment())
//                binding.bottomNavigationView.selectedItemId = R.id.all_video_folders
//            } else
//                when (args) {
//                    "show_video_folder_fragment" -> {
//                        mediaType = "Videos"
//                        setFragment(FoldersFragment())
//                        binding.bottomNavigationView.selectedItemId = R.id.all_video_folders
//                    }
//                    "show_music_folder_fragment" -> {
//                        mediaType = "Music"
//                        setFragment(FoldersFragment())
//                        binding.bottomNavigationView.selectedItemId = R.id.all_video_folders
//                    }
//                    else -> setFragment(AllMediaFragment())
//                }
//        binding.bottomNavigationView.setOnItemSelectedListener {
//            when (it.itemId) {
//                R.id.all_videos -> setFragment(AllMediaFragment())
//                R.id.all_video_folders -> setFragment(FoldersFragment())
//            }
//            return@setOnItemSelectedListener true
//        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = requireActivity().findNavController(R.id.video_container_fragment)
        NavigationUI.setupWithNavController(binding.videoNavView,navController)
//        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
//            object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//
//                    Navigation.findNavController(view)
//                        .navigate(R.id.action_mediaFragmentHolder_to_homeFragment)
//                }
//            })
    }

//    private fun setFragment(fragment: Fragment) {
//        val bundle = Bundle()
//        bundle.putString("video_music_item", mediaType)
//        fragment.arguments = bundle
//        val transaction = activity?.supportFragmentManager!!.beginTransaction()
//        transaction.replace(R.id.videos_container, fragment)
//        transaction.disallowAddToBackStack()
//        transaction
//            .commit()
//
//    }


}