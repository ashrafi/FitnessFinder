package com.test.fitnessstudios.data.data.repository


interface DrivingPtsRepository {
    suspend fun getDrivingPts(org: String, des: String): String
}