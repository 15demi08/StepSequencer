package com.demetriusjr.stepsequencer.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.demetriusjr.stepsequencer.databinding.BarLayoutBinding
import com.demetriusjr.stepsequencer.fragments.SequencerViewModel

class SongAdapter( private val viewModel:SequencerViewModel, private val barFunctions: BarFunctions ):RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    class ViewHolder( val root: BarLayoutBinding ) : RecyclerView.ViewHolder(root.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):ViewHolder {

        val binding = BarLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder( holder:ViewHolder, position:Int ) {

        var root = holder.root
        var bar = viewModel.currentSong!!.bars[position]

        root.kick.apply {
            isChecked = bar.kick
            setOnClickListener { bar.kick = (it as CheckBox).isChecked }
        }
        root.closedHiHat.apply {
            isChecked = bar.closedHiHat
            setOnClickListener { bar.closedHiHat = (it as CheckBox).isChecked }
        }
        root.openHiHat.apply {
            isChecked = bar.openHiHat
            setOnClickListener { bar.openHiHat = (it as CheckBox).isChecked }
        }
        root.ride.apply {
            isChecked = bar.ride
            setOnClickListener { bar.ride = (it as CheckBox).isChecked }
        }
        root.clap.apply {
            isChecked = bar.clap
            setOnClickListener { bar.clap = (it as CheckBox).isChecked }
        }
        root.crash.apply {
            isChecked = bar.crash
            setOnClickListener { bar.crash = (it as CheckBox).isChecked }
        }

        root.btnRemoveBar.setOnClickListener { barFunctions.removeBar( position ) }

        root.btnAddBar.apply {
            if(position != itemCount - 1)
                visibility = View.GONE
            else
                visibility = View.VISIBLE
                setOnClickListener{ barFunctions.addBar() }
        }

    }

    override fun getItemCount(): Int = viewModel.currentSong!!.bars.size

    interface BarFunctions {
        fun addBar()
        fun removeBar( position:Int )
    }

}