package com.demetriusjr.stepsequencer.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demetriusjr.stepsequencer.databinding.SavedSongListItemBinding

class SavedSongsAdapter(private val functions: SavedSongsFunctions): RecyclerView.Adapter<SavedSongsAdapter.ViewHolder>() {

    lateinit var songs: List<Song>

    class ViewHolder( val root:SavedSongListItemBinding ) : RecyclerView.ViewHolder(root.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SavedSongListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var root = holder.root
        var song = songs[position]

        root.txtvSongName.text = song.name
        root.btnEditSong.setOnClickListener { functions.load(song) }
        root.btnDeleteSong.setOnClickListener { functions.delete(song) }

    }

    override fun getItemCount(): Int = songs.size

    interface SavedSongsFunctions {
        fun load( song:Song )
        fun delete( song:Song )
    }

}