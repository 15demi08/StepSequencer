package com.demetriusjr.stepsequencer.fragments

import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ToggleButton
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.demetriusjr.stepsequencer.R
import com.demetriusjr.stepsequencer.databinding.SequencerFragmentBinding
import com.demetriusjr.stepsequencer.model.Song
import com.demetriusjr.stepsequencer.model.SongAdapter
import com.demetriusjr.stepsequencer.service.SongClient
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.time.*

class SequencerFragment : SongAdapter.BarFunctions, Fragment() {

    private lateinit var _binding:SequencerFragmentBinding
    private val binding
        get() = _binding

    private val viewModel: SequencerViewModel by navGraphViewModels(R.id.sequencerFragment)
    private lateinit var sc:SongClient

    private var loopSong:Boolean = false
    private lateinit var playingSong:CountDownTimer

    override fun onCreateView( inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle? ):View {

        _binding = SequencerFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if( viewModel.currentSong == null )
            viewModel.currentSong = Song().apply { addBar() }

        binding.apply {

            songName.apply {
                setText( viewModel.currentSong!!.name ?: getString(R.string.mainScreen_unsavedSong) )
                isEnabled = false
            }

            songTempo.apply {
                setSelection(viewModel.currentSong!!.tempo - 1)
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected( parent: AdapterView<*>?,view: View?, position: Int, id: Long ) {
                        viewModel.currentSong!!.tempo = position+1
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        Log.i("StepSequencer", "spinner.onNothingSelected")
                    }

                }
            }

            btnSavedSongs.setOnClickListener { findNavController().navigate(R.id.action_sequencerFragment_to_savedSongsFragment) }
            btnNewSong.setOnClickListener { newSong() }
            btnPlayStop.setOnClickListener { playOrStopSong(it) }
            btnSaveSong.setOnClickListener { saveSong() }
            btnEditSongName.setOnClickListener { editSongName() }
            btnSaveNewSongName.setOnClickListener { saveNewSongName() }
            btnCancelEditSongName.setOnClickListener { cancelEditSongName() }
            loop.setOnClickListener {
                loopSong = (it as SwitchCompat).isChecked
            }

            val manager = LinearLayoutManager(context)
            val songAdapter = SongAdapter(viewModel, this@SequencerFragment)

            bars.apply {
                setHasFixedSize(true)
                layoutManager = manager
                adapter = songAdapter
            }

        }

    }

    private fun playOrStopSong(v:View ) {
        if ((v as ToggleButton).isChecked) {

            Log.i("StepSequencer", "should play, looping: $loopSong")
            playSong()

        } else {

            Log.i("StepSequencer", "should stop")
            stopSong()

        }
    }

    fun newSong(){
        viewModel.currentSong = Song().apply { addBar() }
        binding.bars.adapter?.notifyDataSetChanged()
    }

    private fun playSong() {

        Log.i("StepSequencer", "Rodando")

        val interval = 1000 / viewModel.currentSong!!.tempo
        var currentBar = 0
        val song = viewModel.currentSong!!.bars

        // TODO: FIND A BETTER WAY TO DO THIS
        playingSong = object : CountDownTimer((song.size * interval).toLong(), interval.toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                if(currentBar < song.size) {
                    song[currentBar].apply {
                        if (kick) playSample(R.raw.kick)
                        if (closedHiHat) playSample(R.raw.hihat_c)
                        if (openHiHat) playSample(R.raw.hihat_o)
                        if (ride) playSample(R.raw.ride)
                        if (clap) playSample(R.raw.clap)
                        if (crash) playSample(R.raw.crash)
                    }
                    currentBar++
                }
            }
            override fun onFinish() {
                if(loopSong) {
                    currentBar = 0
                    start()
                } else {
                    binding.btnPlayStop.isChecked = false
                }
            }
        }.start()

    }

    private fun playSample ( id:Int ) =
        MediaPlayer.create(this@SequencerFragment.context, id).apply {
            start()
            setOnCompletionListener {
                it.release()
            }
        }

    private fun stopSong(){
        playingSong.cancel()
    }

    fun saveSong(){

        sc = SongClient()

        if(viewModel.currentSong!!.name == null) {
            showSnack(R.string.mainScreen_msgUnnamedSong)
        } else if (viewModel.currentSong!!.isEmpty()){
            showSnack(R.string.mainScreen_msgSongEmpty)
        } else {
            if(viewModel.currentSong!!.id == null)
                sc.new(viewModel.currentSong!!)
                    .addOnSuccessListener {
                        showSnack(R.string.mainScreen_msgNewSuccess)
                    }
            else
                sc.update(viewModel.currentSong!!)
                    .addOnSuccessListener {
                        showSnack(R.string.mainScreen_msgUpdateSuccess)
                    }
        }

    }

    private fun showSnack( msg:Int ){
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT ).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
    }

    private fun editSongName(){
        binding.apply {
            songName.isEnabled = true
            btnEditSongName.visibility = View.GONE
            btnSaveNewSongName.visibility = View.VISIBLE
            btnCancelEditSongName.visibility = View.VISIBLE
        }
    }

    private fun cancelEditSongName (){
        binding.apply {
            songName.apply {
                setText(viewModel.currentSong!!.name)
                isEnabled = false
            }
            btnEditSongName.visibility = View.VISIBLE
            btnSaveNewSongName.visibility = View.GONE
            btnCancelEditSongName.visibility = View.GONE
        }
    }

    private fun saveNewSongName (){
        binding.apply {
            viewModel.currentSong!!.name = songName.text.toString()
            songName.isEnabled = false
            btnEditSongName.visibility = View.VISIBLE
            btnSaveNewSongName.visibility = View.GONE
            btnCancelEditSongName.visibility = View.GONE
        }
    }

    override fun addBar() {
        viewModel.currentSong!!.addBar()
        binding.bars.adapter?.notifyDataSetChanged()
    }

    override fun removeBar(position: Int) {
        viewModel.currentSong!!.removeBar(position)
        binding.bars.apply {
            adapter?.notifyDataSetChanged()
            getChildAt(adapter!!.itemCount-1).findViewById<Button>(R.id.btnAddBar).visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()

        binding.apply {
            songName.setText(viewModel.currentSong!!.name)
            songTempo.setSelection(viewModel.currentSong!!.tempo - 1)
            bars.adapter?.notifyDataSetChanged()
        }
    }

    companion object {
        fun newInstance() = SequencerFragment()
    }

}