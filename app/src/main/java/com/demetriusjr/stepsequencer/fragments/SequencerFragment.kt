package com.demetriusjr.stepsequencer.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.demetriusjr.stepsequencer.R
import com.demetriusjr.stepsequencer.databinding.SequencerFragmentBinding
import com.demetriusjr.stepsequencer.model.Song
import com.demetriusjr.stepsequencer.model.SongAdapter
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.time.*

class SequencerFragment : SongAdapter.BarFunctions, Fragment() {

    private lateinit var _binding:SequencerFragmentBinding
    private val binding
        get() = _binding

    private lateinit var viewModel: SequencerViewModel
    private lateinit var kickSample: MediaPlayer
    private lateinit var closedHiHatSample: MediaPlayer
    private lateinit var openHiHatSample: MediaPlayer
    private lateinit var rideSample: MediaPlayer
    private lateinit var clapSample: MediaPlayer
    private lateinit var crashSample: MediaPlayer

    override fun onCreateView( inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle? ):View {

        kickSample = MediaPlayer.create(this.context, R.raw.kick)
        closedHiHatSample = MediaPlayer.create(this.context, R.raw.hihat_c)
        openHiHatSample = MediaPlayer.create(this.context, R.raw.hihat_o)
        rideSample = MediaPlayer.create(this.context, R.raw.ride)
        clapSample = MediaPlayer.create(this.context, R.raw.clap)
        crashSample = MediaPlayer.create(this.context, R.raw.crash)

        _binding = SequencerFragmentBinding.inflate(layoutInflater, container, false)

        return binding.root

    }

    @ExperimentalTime
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(SequencerViewModel::class.java)
        viewModel.currentSong = Song("Test Song", 4).apply {
            addBar(Song.Bar(true, true, false, false, false, false))
            addBar(Song.Bar(false, true, false, false, false, false))
            addBar(Song.Bar(false, true, false, false, true, false))
            addBar(Song.Bar(false, true, false, false, false, false))
        }

        binding.apply {

            songName.apply {
                setText(viewModel.currentSong!!.name)
                isEnabled = false
            }

            songTempo.setSelection(viewModel.currentSong!!.tempo - 1)

            btnPlaySong.setOnClickListener { playSong(it) }
            btnSaveSong.setOnClickListener { saveSong(it) }
            btnEditSongName.setOnClickListener { editSongName(it) }
            btnSaveNewSongName.setOnClickListener { saveNewSongName(it) }
            btnCancelEditSongName.setOnClickListener { cancelEditSongName(it) }

            val manager = LinearLayoutManager(context)
            val songAdapter = SongAdapter(viewModel, this@SequencerFragment)

            bars.apply {
                setHasFixedSize(true)
                layoutManager = manager
                adapter = songAdapter
            }

        }

    }

    fun playSong(v:View ){

        Log.i("StepSequencer", "Rodando")

        val interval = 1000 / viewModel.currentSong!!.tempo
        var currentBar = 0
        var song = viewModel.currentSong!!.bars

        object : CountDownTimer((song.size * interval).toLong(), interval.toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                song[currentBar].apply {
                    if (kick) kickSample.apply { if (isPlaying) seekTo(0); start() }
                    if (closedHiHat) closedHiHatSample.apply { if (isPlaying) seekTo(0); start() }
                    if (openHiHat) openHiHatSample.apply { if (isPlaying) seekTo(0); start() }
                    if (ride) rideSample.apply { if (isPlaying) seekTo(0); start() }
                    if (clap) clapSample.apply { if (isPlaying) seekTo(0); start() }
                    if (crash) crashSample.apply { if (isPlaying) seekTo(0); start() }
                }
                currentBar++
            }
            override fun onFinish() {
            }
        }.start()

    }

    fun saveSong( v:View ){
        Log.i("StepSequencer", Json.encodeToString(viewModel.currentSong!!))
    }

    fun editSongName( v:View ){
        binding.apply {
            songName.isEnabled = true
            btnEditSongName.visibility = View.GONE
            btnSaveNewSongName.visibility = View.VISIBLE
            btnCancelEditSongName.visibility = View.VISIBLE
        }
    }

    fun cancelEditSongName ( v:View ){
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

    fun saveNewSongName ( v:View ){
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

    companion object {
        fun newInstance() = SequencerFragment()
    }

}