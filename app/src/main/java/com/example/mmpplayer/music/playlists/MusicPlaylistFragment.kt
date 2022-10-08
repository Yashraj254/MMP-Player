package com.example.mmpplayer.music.playlists

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mmpplayer.R
import com.example.mmpplayer.TAG
import com.example.mmpplayer.adapters.FoldersAdapter
import com.example.mmpplayer.adapters.PlaylistAdapter
import com.example.mmpplayer.databinding.FragmentMusicFoldersBinding
import com.example.mmpplayer.databinding.FragmentMusicPlaylistBinding
import com.example.mmpplayer.model.Media
import com.example.mmpplayer.model.MediaViewModel
import com.example.mmpplayer.model.MusicPlayerViewModel
import com.example.mmpplayer.model.Playlist
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MusicPlaylistFragment : Fragment(), MenuProvider {
    private var _binding: FragmentMusicPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PlaylistAdapter
    private val musicList = ArrayList<Media>()
    private var menu: Menu? = null
    private var delIconVisible = false

    //    private lateinit var viewModel: MediaViewModel
    private val viewModel by viewModels<MusicPlayerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        requireActivity().title = "Playlists"
        _binding = FragmentMusicPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PlaylistAdapter { isVisible -> showDeleteMenu(isVisible) }

        binding.apply {
            fabCreatePlaylist.setOnClickListener {
                val input = EditText(context)
                val alert = AlertDialog.Builder(requireContext())
                    .setTitle("Playlist")
                    .setView(input)
                    .setMessage("Please enter your playlist name.")
                    .setPositiveButton("Save") { dialog, _ ->
                        // do some thing with input.text
                        if (input.text.isNotBlank()) {
                            val playlistName = input.text.toString()
                            val playlist = Playlist(0, playlistName, musicList)
                            viewModel.createPlaylist(playlist)
                        } else {
                            Toast.makeText(context, "Enter playlist name", Toast.LENGTH_SHORT)
                                .show()
                        }
                        dialog.cancel()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.cancel()
                    }.create()

                alert.show()
            }
        }
        getAllPlaylists()
        activity?.addMenuProvider(this, viewLifecycleOwner,Lifecycle.State.RESUMED)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getAllPlaylists() {
        setAdapter()
        viewModel.getAllPlaylists().observe(viewLifecycleOwner, Observer {
            adapter.differ.submitList(it)
        })
    }

    private fun setAdapter() {
        binding.rvAllPlaylists.layoutManager = LinearLayoutManager(context)
        binding.rvAllPlaylists.adapter = adapter
    }

    private fun showDeleteMenu(isVisible: Boolean) {
        menu?.findItem(R.id.delete)?.isVisible = isVisible
        delIconVisible = isVisible
    }

    private fun deletePlaylists() {
        val getSelectedPlaylists = adapter.getSelectedItemsList()
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Delete")
        alertDialog.setMessage("Do you want to delete the selected items?")
        alertDialog.setPositiveButton("Delete", DialogInterface.OnClickListener { _, _ ->
            for (playlist in getSelectedPlaylists) {
                viewModel.deletePlaylists(playlist)
            }
        }).setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
            for (playlist in adapter.differ.currentList) {
                playlist.isSelected = false
            }
            adapter.notifyDataSetChanged()
        })
        alertDialog.setCancelable(false).create().show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        this.menu = menu
        menuInflater.inflate(R.menu.delete_menu, menu)
        showDeleteMenu(false)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.delete -> {
                deletePlaylists()
                showDeleteMenu(false)
            }
        }
        return false
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}