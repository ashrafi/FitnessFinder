package com.test.fitnessstudios.core.network.service.yelp

import com.apollographql.apollo3.ApolloClient
import com.test.fitnessstudios.core.model.BusinessInfo
import com.test.fitnessstudios.core.network.SearchYelpQuery
import com.test.fitnessstudios.core.network.model.YelpAPI
import com.test.fitnessstudios.core.network.model.toBusinessInfo
import javax.inject.Inject

class ApolloYelpClient @Inject constructor(
    private val apolloClient: ApolloClient
) : YelpAPI {

    override suspend fun getBusinesses(
        latitude: Double,
        longitude: Double,
        radius: Double,
        sort_by: String,
        categories: String
    ): List<BusinessInfo?>? {
        return apolloClient.query(
            SearchYelpQuery(
                latitude = latitude,
                longitude = longitude,
                radius = radius,
                sort_by = sort_by,
                categories = categories
            )
        ).execute()
            .data
            ?.search
            ?.business
            ?.map { it?.toBusinessInfo() }
            ?: emptyList<BusinessInfo>()
    }
}
