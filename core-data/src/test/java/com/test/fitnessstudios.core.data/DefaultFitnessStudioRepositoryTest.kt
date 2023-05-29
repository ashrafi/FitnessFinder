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

package com.test.fitnessstudios.core.data

import com.test.fitnessstudios.core.data.repository.DefaultFitnessStudioRepository
import com.test.fitnessstudios.core.database.FitnessStudio
import com.test.fitnessstudios.core.testing.data.dao.FakeFitnessStuioDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Unit tests for [DefaultFitnessStudioRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DefaultFitnessStudioRepositoryTest {

    @Test
    fun fitnessStudios_newItemSaved_itemIsReturned() = runTest {
        val repository = DefaultFitnessStudioRepository(FakeFitnessStuioDao())

        val testFit = FitnessStudio(
            "0",
            "none",
            "none",
            0.0,
            0.0,
            0.0,
            true,
            "2010-06-01T22:19:44".toLocalDateTime()
        )

        repository.add(testFit)

        assertEquals(repository.fitnessStudios.first().size, 1)
    }

}
