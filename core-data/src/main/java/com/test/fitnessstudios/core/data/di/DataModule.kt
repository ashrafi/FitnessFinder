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

package com.test.fitnessstudios.core.data.di

import com.test.fitnessstudios.core.data.YelpRepoImp
import com.test.fitnessstudios.core.data.repository.DefaultFitnessStudioRepository
import com.test.fitnessstudios.core.data.repository.FitnessStudioRepository
import com.test.fitnessstudios.core.data.repository.YelpRepo
import com.test.fitnessstudios.core.database.FitnessStudio
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsFitnessStudioRepository(
        fitnessStudioRepository: DefaultFitnessStudioRepository
    ): FitnessStudioRepository


    @Singleton
    @Binds
    fun bindsYelpRepo(
        YelpRepo: YelpRepoImp
    ): YelpRepo

}

class FakeFitnessStudioRepository @Inject constructor() : FitnessStudioRepository {
    override val fitnessStudios: Flow<List<FitnessStudio>> = flowOf(fakeFitnessStudios)

    override suspend fun add(gym: FitnessStudio) {
        throw NotImplementedError()
    }

    override suspend fun add(id: String, name: String, wkDate: LocalDate) {
        throw NotImplementedError()
    }

    override suspend fun exists(name: String): Boolean {
        throw NotImplementedError()
    }

    override suspend fun nuke() {
        throw NotImplementedError()
    }
}

val fakeFitnessStudios = listOf(FitnessStudio("0", "none", LocalDate.parse("3/4/2023")))
