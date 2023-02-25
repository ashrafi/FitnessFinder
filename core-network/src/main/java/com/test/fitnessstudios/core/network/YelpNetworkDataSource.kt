package com.test.fitnessstudios.core.network

import com.apollographql.apollo3.ApolloCall

/**
 * Interface representing network calls to the NIA backend
 */
interface YelpNetworkDataSource {
    fun getFitnessClubs(
        latitude: Double,
        longitude: Double,
        radius: Double,
        sort_by: String,
        categories: String
    ): ApolloCall<SearchYelpQuery.Data>

}

/*

        apolloClient(context).query(
            SearchYelpQuery(
                latitude = 33.524155,
                longitude = -111.905792,
                radius = 1000.0,
                sort_by = "distance",
                categories = "fitness"
            )
        ).toFlow()

latitude = 33.524155,
                longitude = -111.905792,
                radius = 1000.0,
                sort_by = "distance",
                categories = "fitness"
 */
