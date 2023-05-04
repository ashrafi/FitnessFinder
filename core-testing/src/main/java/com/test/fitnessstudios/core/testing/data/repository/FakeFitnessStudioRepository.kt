package com.test.fitnessstudios.core.testing.data.repository

import com.test.fitnessstudios.core.data.repository.FitnessStudioRepository
import com.test.fitnessstudios.core.database.FitnessStudio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

class FakeFitnessStudioRepository @Inject constructor() : FitnessStudioRepository {
    override val fitnessStudios: Flow<List<FitnessStudio>> = flowOf(fakeFitnessStudios)


    val fakeDB = mutableMapOf<String, FitnessStudio>()

    override suspend fun add(gym: FitnessStudio) {
        fakeDB[gym.uid] = gym
    }

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
        val gym = FitnessStudio(
            uid = id,
            name = name,
            photo = photo,
            lat = lat,
            lng = lng,
            stars = stars,
            workOutDate = wkDate
        )
        fakeDB[id] = gym
    }

    override suspend fun del(gym: FitnessStudio) {
        fakeDB.remove(gym.uid)
    }

    override suspend fun deleteById(id: String) {
        fakeDB.remove(id)
    }

    override suspend fun itemExistsByName(name: String): Boolean {
        TODO("Not yet implemented")
        throw NotImplementedError()
    }

    override fun itemExistsById(id: String): Flow<Boolean> = flow {
        emit(fakeDB.containsKey(id))
    }

    override suspend fun get(id: String): Flow<FitnessStudio> = flow {
        fakeDB.get(id)?.let {
            emit(it)
        }
    }

    override suspend fun nuke() {
        fakeDB.clear()
    }
}

val fakeFitnessStudios =
    listOf(
        FitnessStudio(
            "0",
            "none",
            "none",
            0.0,
            0.0,
            0.0,
            true,
            "2010-06-01T22:19:44".toLocalDateTime()
        )
    )

