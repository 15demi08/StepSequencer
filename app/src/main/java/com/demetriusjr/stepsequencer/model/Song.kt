package com.demetriusjr.stepsequencer.model

import kotlinx.serialization.Serializable

@Serializable
class Song ( var name:String? = null, var tempo:Int = 4 ) {

    private var bars:ArrayList<Bar> = ArrayList()

    /**
     *  Adds a Bar to the Song
     *
     *  0: Kick,
     *  1: Closed Hi-Hat,
     *  2: Open Hi-Hat,
     *  3: Ride,
     *  4: Clap (Snare),
     *  5: Crash
     */
    fun addBar(instruments: Array<Boolean>){
        bars.add(
            Bar(
                instruments[0],
                instruments[1],
                instruments[2],
                instruments[3],
                instruments[4],
                instruments[5]
            )
        )
    }

    /**
     * Returns a Bar from the Song
     */
    fun getBar( i:Int ):Bar = bars[i]

    /**
     * Removes a Bar from the Song
     */
    fun removeBar( i:Int ) = bars.removeAt(i)

    /**
     * Represents a Bar in the Song. A Bar indicates what samples get played at any given point in
     * time throughout the song. Each Bar is "played" at the interval defined by the tempo property.
     */
    @Serializable
    data class Bar(
        var kick:Boolean,
        var closedHiHat:Boolean,
        var openHiHat:Boolean,
        var ride:Boolean,
        var clap:Boolean,
        var crash:Boolean
    )

}