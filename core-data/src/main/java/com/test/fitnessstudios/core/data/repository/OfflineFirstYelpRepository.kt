/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.test.fitnessstudios.core.data.repository

import com.apollographql.apollo3.ApolloCall
import com.test.fitnessstudios.core.network.SearchYelpQuery
import com.test.fitnessstudios.core.network.YelpNetworkDataSource
import javax.inject.Inject

/**
 * Disk storage backed implementation of the [TopicsRepository].
 * Reads are exclusively from local storage to support offline access.
 */
class OfflineFirstYelpRepository @Inject constructor(
    //private val topicDao: TopicDao,
    private val network: YelpNetworkDataSource,
) : YelpGraphQLRepository {

    //  List<SearchYelpQuery.Business>?.toFlow().map {
    override suspend fun invoke(
        latitude: Double,
        longitude: Double,
        radius: Double,
        sort_by: String,
        categories: String
    ): ApolloCall<SearchYelpQuery.Data> {
        return network.getFitnessClubs(
            latitude,
            longitude,
            radius,
            sort_by,
            categories
        )
    }
}
