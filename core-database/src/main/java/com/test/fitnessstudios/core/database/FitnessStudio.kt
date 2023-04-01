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
import kotlinx.datetime.LocalDate

@Entity
data class FitnessStudio(
    @PrimaryKey
    @ColumnInfo(name = "uid") val uid: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "photo") val photo: String?,
    @ColumnInfo(name = "lat") val lat: Double,
    @ColumnInfo(name = "lng") val lng: Double,
    @ColumnInfo(name = "fav") val fav: Boolean = false,
    @ColumnInfo(name = "date") val workOutDate: LocalDate
)


@Dao
interface FitnessStudioDao {
    @Query("SELECT * FROM fitnessstudio ORDER BY uid DESC LIMIT 10")
    fun getFitnessStudios(): Flow<List<FitnessStudio>>

    @Query("SELECT * FROM fitnessstudio WHERE uid=:uid")
    fun getFitnessStudio(uid: String): Flow<FitnessStudio>

    // Just do nothing if it already in the DB
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFitnessStudio(item: FitnessStudio)

    @Query("SELECT EXISTS (SELECT 1 FROM fitnessstudio WHERE name = :name)")
    suspend fun exists(name: String): Boolean

    @Query("DELETE FROM fitnessstudio")
    suspend fun nuke()

}
