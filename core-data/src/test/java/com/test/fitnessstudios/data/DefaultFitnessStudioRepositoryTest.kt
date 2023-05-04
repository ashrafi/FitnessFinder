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

package com.test.fitnessstudios.data

import com.test.fitnessstudios.core.data.repository.DefaultFitnessStudioRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * Unit tests for [DefaultFitnessStudioRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DefaultFitnessStudioRepositoryTest {

    @Test
    fun fitnessStudios_newItemSaved_itemIsReturned() = runTest {
        // val repository = DefaultFitnessStudioRepository(FakeFitnessStudioDao())

        // repository.add("Repository")

        // assertEquals(repository.fitnessStudios.first().size, 1)
    }

}
