package com.demetriusjr.stepsequencer.model

import kotlinx.serialization.Serializable

@Serializable
class Song ( var name:String? = null, var tempo:Int ) {

    var bars:ArrayList<Bar> = ArrayList()

    /**
     * Adds an empty Bar to the Song
     */
    fun addBar(){
        bars.add( Bar() )
    }

    fun addBar( b:Bar ){
        bars.add(b)
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