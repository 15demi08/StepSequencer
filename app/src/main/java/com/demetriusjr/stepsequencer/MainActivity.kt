package com.demetriusjr.stepsequencer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.demetriusjr.stepsequencer.databinding.ActivityMainBinding
import com.demetriusjr.stepsequencer.model.Song
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MainActivity : AppCompatActivity() {

    private lateinit var _binding:ActivityMainBinding
    private val binding
        get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}