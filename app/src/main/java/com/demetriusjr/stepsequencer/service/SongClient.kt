package com.demetriusjr.stepsequencer.service

import com.demetriusjr.stepsequencer.model.Song
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SongClient {

    private val db = Firebase.firestore
    private val songs = db.collection("songs")
    private val format = Json { encodeDefaults = true }

    fun getAll() = songs.get()
    fun getByID( id:String ) = songs.document(id).get()
    fun new( song:Song ) = songs.add(makeMap(song))
    fun update( song:Song ) = songs.document(song.id!!).update(makeMap(song))
    fun delete( id:String ): Task<Void> = songs.document(id).delete()

    private fun makeMap ( song:Song ) = mapOf(
        "name" to song.name,
        "tempo" to song.tempo,
        "barsJSON" to format.encodeToString(song.bars)
    )

}