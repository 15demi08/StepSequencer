package com.demetriusjr.stepsequencer

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.demetriusjr.stepsequencer.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var _binding:ActivityMainBinding
    private val binding
        get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

    }

    override fun onOptionsItemSelected( item: MenuItem ): Boolean {
        if( item.itemId == R.id.menuBtnSaveSong ){
            showSnack(R.string.mainScreen_msgNewSuccess)
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onCreateOptionsMenu( menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    private fun showSnack( msg:Int ){
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT ).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
    }

}