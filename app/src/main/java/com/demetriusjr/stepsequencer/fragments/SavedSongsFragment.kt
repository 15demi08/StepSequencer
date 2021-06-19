package com.demetriusjr.stepsequencer.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.demetriusjr.stepsequencer.R
import com.demetriusjr.stepsequencer.databinding.FragmentSavedSongsBinding
import com.demetriusjr.stepsequencer.model.SavedSongsAdapter
import com.demetriusjr.stepsequencer.model.Song
import com.demetriusjr.stepsequencer.service.SongClient
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * A simple [Fragment] subclass.
 * Use the [SavedSongsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedSongsFragment : SavedSongsAdapter.SavedSongsFunctions, Fragment() {

    private lateinit var _binding: FragmentSavedSongsBinding
    private val binding
        get() = _binding
    private lateinit var sc:SongClient

    private val viewModel:SequencerViewModel by navGraphViewModels(R.id.sequencerFragment)

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {

        _binding = FragmentSavedSongsBinding.inflate(layoutInflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sc = SongClient()

        sc.getAll()
            .addOnSuccessListener {

                val manager = LinearLayoutManager(context)
                val savedSongsAdapter = SavedSongsAdapter(this@SavedSongsFragment)

                var tempSongs = it.toObjects(Song::class.java)
                tempSongs.forEach {
                    it.bars = Json.decodeFromString(it.barsJSON!!)
                }

                savedSongsAdapter.songs = tempSongs

                binding.savedSongs.apply {
                    setHasFixedSize(true)
                    layoutManager = manager
                    adapter = savedSongsAdapter
                }

            }

    }

    override fun load( song:Song ) {
        viewModel.currentSong = song
        findNavController().popBackStack()
    }

    override fun delete( song:Song ) {
        sc.delete(song.id!!)
        reloadItems()
    }

    private fun reloadItems(){

        sc.getAll()
            .addOnSuccessListener {

                var tempSongs = it.toObjects(Song::class.java)
                tempSongs.forEach {
                    it.bars = Json.decodeFromString(it.barsJSON!!)
                }

                (binding.savedSongs.adapter as SavedSongsAdapter).apply {
                    songs = tempSongs
                    notifyDataSetChanged()
                }

            }

    }

}