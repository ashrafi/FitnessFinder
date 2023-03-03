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

package com.test.fitnessstudios.core.data.repository

import com.test.fitnessstudios.core.database.FitnessStudio
import com.test.fitnessstudios.core.database.FitnessStudioDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface FitnessStudioRepository {
    val fitnessStudios: Flow<List<String>>

    suspend fun add(name: String)
    suspend fun exists(name: String): Boolean
    suspend fun nuke()
}

class DefaultFitnessStudioRepository @Inject constructor(
    private val fitnessStudioDao: FitnessStudioDao
) : FitnessStudioRepository {

    override val fitnessStudios: Flow<List<String>> =
        fitnessStudioDao.getFitnessStudios().map { items -> items.map { it.name } }

    override suspend fun add(name: String) {
        fitnessStudioDao.insertFitnessStudio(FitnessStudio(name = name))
    }

    override suspend fun exists(name: String): Boolean {
        return fitnessStudioDao.exists(name)
    }

    override suspend fun nuke() {
        fitnessStudioDao.nuke()
    }
}
