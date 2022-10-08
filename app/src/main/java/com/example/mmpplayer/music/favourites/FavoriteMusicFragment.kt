package com.example.mmpplayer.music.favourites

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mmpplayer.R
import com.example.mmpplayer.TAG
import com.example.mmpplayer.adapters.FavouritesAdapter
import com.example.mmpplayer.adapters.MediaListAdapter
import com.example.mmpplayer.databinding.FragmentAllMusicBinding
import com.example.mmpplayer.databinding.FragmentFavoriteMusicBinding
import com.example.mmpplayer.model.MediaViewModel
import com.example.mmpplayer.model.MusicPlayerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteMusicFragment : Fragment(), MenuProvider {

    private var _binding: FragmentFavoriteMusicBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FavouritesAdapter
    private var menu: Menu? = null
    private var delIconVisible = false
    private val viewModel by viewModels<MusicPlayerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoriteMusicBinding.inflate(inflater, container, false)
        requireActivity().title = "Favourites"

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavouritesAdapter({ isVisible -> showDeleteMenu(isVisible) }, requireActivity())
        getAllMusic()

        activity?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getAllMusic() {
        if (viewModel.getAllFavourites().value?.isEmpty() == true)
            binding.tvNoFavourites.apply {
                visibility = View.VISIBLE
            }
        else {
            setAdapter()
        }
        viewModel.getAllFavourites().observe(viewLifecycleOwner, Observer {
            adapter.differ.submitList(it)
        })
    }

    private fun setAdapter() {
        binding.rvFavouritesMusic.layoutManager = LinearLayoutManager(context)
        binding.rvFavouritesMusic.adapter = adapter
    }

    private fun deleteFavourites() {
        val getSelectedFavourites = adapter.getSelectedItemsList()
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Delete")
        alertDialog.setMessage("Do you want to delete the selected items?")
        alertDialog.setPositiveButton("Delete", DialogInterface.OnClickListener { _, _ ->
            for (favourites in getSelectedFavourites) {
                viewModel.removeFromFavourites(favourites)
            }
        }).setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
            for (favourites in adapter.differ.currentList) {
                favourites.isSelected = false
            }
            adapter.notifyDataSetChanged()
        })
        alertDialog.setCancelable(false).create().show()
    }

    private fun showDeleteMenu(isVisible: Boolean) {
        menu?.findItem(R.id.delete)?.isVisible = isVisible
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        this.menu = menu
        menuInflater.inflate(R.menu.delete_menu, menu)
        showDeleteMenu(false)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.delete -> {
                deleteFavourites()
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