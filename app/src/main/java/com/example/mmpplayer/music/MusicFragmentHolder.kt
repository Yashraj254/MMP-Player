package com.example.mmpplayer.music

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.mmpplayer.R
import com.example.mmpplayer.databinding.FragmentMusicHolderBinding


class MusicFragmentHolder : Fragment() {
    private var _binding: FragmentMusicHolderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMusicHolderBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = requireActivity().findNavController(R.id.music_container_fragment)
        NavigationUI.setupWithNavController(binding.musicNavView,navController)
    }

    fun setBottomNavigationVisibility(visibility: Int) {
        // get the reference of the bottomNavigationView and set the visibility.
        binding.musicNavView.visibility = visibility
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}