package com.test.fitnessstudios.core.network.service

import android.content.Context
import com.apollographql.apollo3.ApolloCall
import com.test.fitnessstudios.core.network.SearchYelpQuery
import com.test.fitnessstudios.core.network.YelpNetworkDataSource
import javax.inject.Inject

class YelpGraphQLNetwork @Inject constructor(
    val context: Context
) : YelpNetworkDataSource {

    override fun getFitnessClubs(
        latitude: Double,
        longitude: Double,
        radius: Double,
        sort_by: String,
        categories: String
    ): ApolloCall<SearchYelpQuery.Data> {
        return apolloClient(context = context).query(
            SearchYelpQuery(
                latitude = latitude,
                longitude = longitude,
                radius = radius,
                sort_by = sort_by,
                categories = categories
            )
        )
    }
}