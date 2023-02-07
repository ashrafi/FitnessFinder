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

package com.test.fitnessstudios.core.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity
data class FitnessStudio(
    val name: String
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}

@Dao
interface FitnessStudioDao {
    @Query("SELECT * FROM fitnessstudio ORDER BY uid DESC LIMIT 10")
    fun getFitnessStudios(): Flow<List<FitnessStudio>>

    @Insert
    suspend fun insertFitnessStudio(item: FitnessStudio)
}
