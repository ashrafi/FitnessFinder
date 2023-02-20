package com.test.fitnessstudios.core.data.repository

interface YelpGraphQLRepository {
    /**
     * Gets the available topics as a stream
     */
    suspend fun getGyms()
}