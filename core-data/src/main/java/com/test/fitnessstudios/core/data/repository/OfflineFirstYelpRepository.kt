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

import android.util.Log
import com.test.fitnessstudios.core.network.SearchYelpQuery
import com.test.fitnessstudios.core.network.YelpNetworkDataSource
import kotlinx.coroutines.flow.map
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
    override fun getGyms() {

        network.getFitnessClubs().toFlow().map {
            val launchList: List<SearchYelpQuery.Business>? = it
                .data
                ?.search
                ?.business
                ?.filterNotNull()
            if (launchList == null) {
                // There were some error
                // TODO: do something with response.errors
                Log.d("GraphQL", "Bad")
            } else {
                Log.d("GraphQL", "Good ${launchList.count()}")
            }
        }
        Log.d("GraphQL", "Done")

    }
}
