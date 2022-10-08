package com.example.mmpplayer.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.mmpplayer.R
import com.example.mmpplayer.databinding.FragmentVideosHolderBinding


class VideoFragmentHolder : Fragment() {

    private var _binding: FragmentVideosHolderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentVideosHolderBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = requireActivity().findNavController(R.id.video_container_fragment)
        NavigationUI.setupWithNavController(binding.videoNavView,navController)
    }
    fun setBottomNavigationVisibility(visibility: Int) {
        // get the reference of the bottomNavigationView and set the visibility.
        binding.videoNavView.visibility = visibility
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}