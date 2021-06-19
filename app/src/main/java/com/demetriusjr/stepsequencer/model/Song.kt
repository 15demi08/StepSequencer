package com.demetriusjr.stepsequencer.model

import com.google.firebase.firestore.DocumentId
import kotlinx.serialization.Serializable

class Song {

    @DocumentId
    var id:String? = null;
    var tempo:Int = 4
    var name:String? = null
    var bars:ArrayList<Bar> = ArrayList()
    var barsJSON:String? = null

    /**
     * Adds an empty Bar to the Song
     */
    fun addBar(){
        bars.add( Bar() )
    }

    fun isEmpty():Boolean {
        var r = true
        for( b:Bar in bars )
            b.also {
                if( it.kick || it.closedHiHat || it.openHiHat || it.ride || it.clap || it.crash )
                    r = false
            }
        return r
    }

    /**
     * Removes the Bar at position p
     */
    fun removeBar( p:Int ){
        bars.removeAt(p)
    }

    /**
     * Represents a Bar in the Song. A Bar indicates what samples get played at any given point in
     * time throughout the song. Each Bar is "played" at the interval defined by the tempo property.
     */
    @Serializable
    data class Bar(
        var kick:Boolean = false,
        var closedHiHat:Boolean = false,
        var openHiHat:Boolean = false,
        var ride:Boolean = false,
        var clap:Boolean = false,
        var crash:Boolean = false
    )

}