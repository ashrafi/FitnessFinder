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
        latitude: Float,
        longitude: Float,
        radius: Float,
        sort_by: String,
        categories: String
    ): ApolloCall<SearchYelpQuery.Data> {


        //ApolloCall<LaunchListQuery.Data>
        val response = apolloClient(context).query(
            SearchYelpQuery(
                latitude = 33.524155,
                longitude = -111.905792,
                radius = 1000.0,
                sort_by = "distance",
                categories = "fitness"
            )
        )

        return response
    }
}