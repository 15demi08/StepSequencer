package com.demetriusjr.stepsequencer.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.demetriusjr.stepsequencer.R
import com.demetriusjr.stepsequencer.databinding.ActivityMainBinding
import com.demetriusjr.stepsequencer.databinding.SequencerFragmentBinding

class SequencerFragment : Fragment() {

    private lateinit var _binding: SequencerFragmentBinding
    private val binding
        get() = _binding

    companion object {
        fun newInstance() = SequencerFragment()
    }

    private lateinit var viewModel: SequencerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SequencerFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(SequencerViewModel::class.java)

    }

}