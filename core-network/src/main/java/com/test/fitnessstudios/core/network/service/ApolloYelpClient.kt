package com.test.fitnessstudios.core.network.service

import com.apollographql.apollo3.ApolloClient
import com.test.fitnessstudios.core.network.SearchYelpQuery
import com.test.fitnessstudios.core.network.YelpNetworkDataSource
import com.test.fitnessstudios.core.network.model.YelpAPI
import com.test.fitnessstudios.core.network.type.Business
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ApolloYelpClient @Inject constructor (
    private val apolloClient: ApolloClient
    ) : YelpAPI {

    var business : List<SearchYelpQuery.Business>? = null

    override suspend fun getBusinesses(
        latitude: Double,
        longitude: Double,
        radius: Double,
        sort_by: String,
        categories: String
    ): List<SearchYelpQuery.Business>? {
        apolloClient.query(
            SearchYelpQuery(
                latitude = latitude,
                longitude = longitude,
                radius = radius,
                sort_by = sort_by,
                categories = categories
            )
        ).toFlow().map { it ->
            business = it.data
                ?.search
                ?.business
                ?.filterNotNull()
        }.collect()
        return business // if null it is an error
    }
}