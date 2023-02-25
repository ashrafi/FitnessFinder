package com.test.fitnessstudios.core.data.repository

import com.apollographql.apollo3.ApolloCall
import com.test.fitnessstudios.core.network.SearchYelpQuery

interface YelpRepo {
    /**
     * Gets the available topics as a stream
     */
    suspend operator fun invoke(
        latitude: Double = 33.524155,
        longitude: Double = -111.905792,
        radius: Double = 1000.0,
        sort_by: String = "distance",
        categories: String = "fitness"
    ): List<SearchYelpQuery.Business>?
}