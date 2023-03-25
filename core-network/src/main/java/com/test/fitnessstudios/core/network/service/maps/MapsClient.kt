package com.test.fitnessstudios.core.network.service.maps

import android.util.Log
import com.test.fitnessstudios.core.network.BuildConfig.MAPS_API_KEY
import com.test.fitnessstudios.core.network.model.MapsAPI
import java.net.URL

class MapsClient : MapsAPI {

    //"origin=${(37.7749 + Math.random()/100 )},${-122.4194  + Math.random()/100 }"
    //"destination=${(37.7749 + Math.random()/100 )},${-122.4194  + Math.random()/100 }"
    override fun getMapDirections(org: String, des: String): String {
        // https://maps.googleapis.com/maps/api/directions/json?origin=10.3181466,123.9029382&destination=10.311795,123.915864&key=<YOUR_API_KEY>
        val base = "https://maps.googleapis.com/maps/api/directions/json"
        //37.7749° N, -122.4194°
        val org = "origin=${org}"
        val des = "destination=${des}"
        val call = "$base?$org&$des&key=$MAPS_API_KEY"
        val directions = URL(call).readText()
        Log.d("GraphQL", directions)
        return directions
    }
}