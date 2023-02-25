package com.test.fitnessstudios.core.data

import com.apollographql.apollo3.ApolloCall
import com.test.fitnessstudios.core.data.repository.YelpGraphQLRepository
import com.test.fitnessstudios.core.data.repository.YelpRepo
import com.test.fitnessstudios.core.network.SearchYelpQuery
import com.test.fitnessstudios.core.network.YelpNetworkDataSource
import com.test.fitnessstudios.core.network.model.YelpAPI
import javax.inject.Inject

class YelpRepoImp @Inject constructor(
    //private val topicDao: TopicDao,
    private val network: YelpAPI,
) : YelpRepo {

    //  List<SearchYelpQuery.Business>?.toFlow().map {
    override suspend fun invoke(
        latitude: Double,
        longitude: Double,
        radius: Double,
        sort_by: String,
        categories: String
    ): List<SearchYelpQuery.Business>? {
        return network.getBusinesses(
            latitude,
            longitude,
            radius,
            sort_by,
            categories
        )
    }
}