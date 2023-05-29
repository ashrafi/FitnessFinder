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

package com.test.fitnessstudios.testdi

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.test.fitnessstudios.data.data.di.DataModule
import com.test.fitnessstudios.data.data.repository.DrivingPtsRepository
import com.test.fitnessstudios.data.data.repository.FitnessStudioRepository
import com.test.fitnessstudios.data.data.repository.LocationClientRepo
import com.test.fitnessstudios.data.data.repository.YelpRepo
import com.test.fitnessstudios.core.database.di.DataStoreModule
import com.test.fitnessstudios.core.testing.data.FakeDataStore
import com.test.fitnessstudios.core.testing.data.repository.FakeDrivingPtsRepository
import com.test.fitnessstudios.core.testing.data.repository.FakeFitnessStudioRepository
import com.test.fitnessstudios.core.testing.data.repository.FakeLocationClientRepo
import com.test.fitnessstudios.core.testing.data.repository.FakeYelpRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class, DataStoreModule::class],
)

interface FakeDataModule {
    @Singleton
    @Binds
    abstract fun bindFitnessStudioRepository(
        fitnessStudioRepository: FakeFitnessStudioRepository
    ): FitnessStudioRepository

    @Singleton
    @Binds
    abstract fun bindYelpRepository(
        fakeYelpRepo: FakeYelpRepo
    ): YelpRepo

    @Singleton
    @Binds
    abstract fun bindDrivingPtsRepository(
        fakeDrivingPtsRepository: FakeDrivingPtsRepository
    ): DrivingPtsRepository

    @Singleton
    @Binds
    abstract fun bindLocationClientRepo(
        fakeLocationClientRepo: FakeLocationClientRepo
    ): LocationClientRepo

    @Singleton
    @Binds
    fun provideFakeDataStore(
        fakeDataStoreManager: FakeDataStore
    ): DataStore<Preferences>

}
