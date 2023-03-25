package com.test.fitnessstudios.core.data.repository


interface MapsRepository {
    suspend fun getDrivingPts(org: String, des: String): String
}