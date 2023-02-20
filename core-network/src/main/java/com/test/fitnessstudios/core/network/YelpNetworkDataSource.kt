package com.test.fitnessstudios.core.network

import com.apollographql.apollo3.ApolloCall

/**
 * Interface representing network calls to the NIA backend
 */
interface YelpNetworkDataSource {
    fun getFitnessClubs(
        latitude: Float = 33.524155F,
        longitude: Float = -111.905792F,
        radius: Float = 1000.0F,
        sort_by: String = "distance",
        categories: String = "fitness"
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
