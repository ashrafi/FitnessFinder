/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.test.fitnessstudios.core.network.di

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.test.fitnessstudios.core.network.YelpNetworkDataSource
import com.test.fitnessstudios.core.network.model.YelpAPI
import com.test.fitnessstudios.core.network.service.ApolloYelpClient
import com.test.fitnessstudios.core.network.service.YelpGraphQLNetwork
import com.test.fitnessstudios.core.network.service.apolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideApolloClient(
        @ApplicationContext context: Context,
    ): ApolloClient {
        return apolloClient(context)
    }

    @Provides
    @Singleton
    fun bindsYelpGraphQLNetwork(
        @ApplicationContext context: Context,
    ): YelpNetworkDataSource {
        return YelpGraphQLNetwork(context)
    }

    @Provides
    @Singleton
    fun bindsYelpAPI(
        apolloClient: ApolloClient
    ): YelpAPI {
        return ApolloYelpClient(apolloClient)
    }
}
