package com.example.mmpplayer.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.mmpplayer.R
import com.example.mmpplayer.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val mediaTypes=arrayOf("Music","Videos")

    // creating an Integer type array (fruitImageIds) which
    // contains IDs of different fruits' images
    private val mediaImageIds=arrayOf(R.drawable.music_image, R.drawable.video_image)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        val list=ArrayList<HashMap<String,Any>>()

        for(i in mediaTypes.indices){
            val map=HashMap<String,Any>()

            // Data entry in HashMap
            map["mediaType"] = mediaTypes[i]
            map["mediaImageId"]=mediaImageIds[i]

            // adding the HashMap to the ArrayList
            list.add(map)
        }
        val from=arrayOf("mediaType","mediaImageId")
        val to= intArrayOf(R.id.textView, R.id.imageView)
        val simpleAdapter= SimpleAdapter(requireContext(),list, R.layout.home_item,from,to)
        binding.lvMediaItems.adapter = simpleAdapter

        binding.lvMediaItems.setOnItemClickListener { adapterView, view, i, l ->
            val bundle = Bundle()
            if(i==0){
                bundle.putString("media_type","Music")
            }
            if(i==1){
                bundle.putString("media_type","Videos")
            }
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_mediaFragmentHolder,bundle)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
               requireActivity().finishAffinity()
            }
        })
    }
}

