package com.example.mmpplayer.music.playlists

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mmpplayer.R
import com.example.mmpplayer.adapters.FoldersAdapter
import com.example.mmpplayer.adapters.MediaListAdapter
import com.example.mmpplayer.databinding.FragmentMusicPlaylistBinding
import com.example.mmpplayer.databinding.FragmentPlaylistsAllMusicBinding
import com.example.mmpplayer.model.Media
import com.example.mmpplayer.model.MediaViewModel
import com.example.mmpplayer.model.MusicPlayerViewModel
import com.example.mmpplayer.music.MusicFragmentHolder
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaylistsAllMusicFragment : Fragment() {
    private var _binding: FragmentPlaylistsAllMusicBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MediaListAdapter
    private  var musicList = ArrayList<Media>()
    //    private lateinit var viewModel: MediaViewModel
    private  val viewModel by viewModels<MusicPlayerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPlaylistsAllMusicBinding.inflate(inflater, container, false)
        val navBar = requireActivity().findViewById<BottomNavigationView>(com.example.mmpplayer.R.id.music_nav_view)
        navBar.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlistName = arguments!!.getString("playlist_name")
        val playlistId = arguments!!.getLong("playlist_id")
        requireActivity().title = playlistName

        adapter = MediaListAdapter(requireActivity(),requireContext(),"Music",playlistId.toString())
        setAdapter()
        viewModel.getAllPlaylists().observe(viewLifecycleOwner, Observer {
            for(i in it){
                if(playlistName == i.playlistName){
                    adapter.differ.submitList(i.playlistMusic)
                    break
                }
            }
        })
    }
    private fun setAdapter() {
        binding.rvAllPlaylistsMusic.layoutManager = LinearLayoutManager(context)
        binding.rvAllPlaylistsMusic.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val navBar = requireActivity().findViewById<BottomNavigationView>(com.example.mmpplayer.R.id.music_nav_view)
        navBar.visibility = View.VISIBLE
        _binding = null
    }
}