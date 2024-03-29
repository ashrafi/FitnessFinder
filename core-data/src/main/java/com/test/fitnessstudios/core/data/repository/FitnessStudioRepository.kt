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
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

interface FitnessStudioRepository {
    val fitnessStudios: Flow<List<FitnessStudio>>

    suspend fun add(gym: FitnessStudio)

    suspend fun del(gym: FitnessStudio)

    suspend fun deleteById(id: String)

    suspend fun add(
        id: String,
        name: String,
        photo: String?,
        lat: Double,
        lng: Double,
        stars: Double,
        fav: Boolean,
        wkDate: LocalDateTime
    )

    suspend fun itemExistsByName(name: String): Boolean

    fun itemExistsById(id: String): Flow<Boolean>

    suspend fun get(id: String): Flow<FitnessStudio>
    suspend fun nuke()
}

class DefaultFitnessStudioRepository @Inject constructor(
    private val fitnessStudioDao: FitnessStudioDao
) : FitnessStudioRepository {

    override val fitnessStudios: Flow<List<FitnessStudio>> =
        fitnessStudioDao.getFitnessStudios() // .map { items -> items.map { it. } }

    override suspend fun get(id: String): Flow<FitnessStudio> =
        fitnessStudioDao.getFitnessStudio(uid = id) // .map { items -> items.map { it. } }

    override suspend fun add(
        id: String,
        name: String,
        photo: String?,
        lat: Double,
        lng: Double,
        stars: Double,
        fav: Boolean,
        wkDate: LocalDateTime
    ) {
        fitnessStudioDao.insertFitnessStudio(
            FitnessStudio(
                uid = id,
                name = name,
                photo = photo,
                lat = lat,
                lng = lng,
                stars = stars,
                workOutDate = wkDate
            )
        )
    }

    override suspend fun add(gym: FitnessStudio) {
        fitnessStudioDao.insertFitnessStudio(gym)
    }

    override suspend fun del(gym: FitnessStudio) {
        fitnessStudioDao.deleteFitnessStudio(gym)
    }

    override suspend fun deleteById(id: String) {
        fitnessStudioDao.deleteById(id)
    }

    override suspend fun itemExistsByName(name: String): Boolean {
        return fitnessStudioDao.itemExistsByName(name)
    }

    override fun itemExistsById(id: String): Flow<Boolean> {
        return fitnessStudioDao.itemExistsById(id)
    }

    override suspend fun nuke() {
        fitnessStudioDao.nuke()
    }
}
