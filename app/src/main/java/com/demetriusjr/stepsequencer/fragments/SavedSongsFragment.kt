package com.demetriusjr.stepsequencer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.demetriusjr.stepsequencer.R

/**
 * A simple [Fragment] subclass.
 * Use the [SavedSongsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedSongsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_songs, container, false)
    }

}