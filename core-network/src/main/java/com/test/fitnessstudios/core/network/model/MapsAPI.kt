package com.test.fitnessstudios.core.network.model

interface MapsAPI {
    fun getMapDirections(org: String, des: String): String
}