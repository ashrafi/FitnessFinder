package com.test.fitnessstudios.core.domain


import com.test.fitnessstudios.core.data.repository.FitnessStudioRepository
import com.test.fitnessstudios.core.database.FitnessStudio
import com.test.fitnessstudios.core.model.model.BusinessInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject


class FitnessUseCase @Inject constructor(
    private val fitnessStudioRepository: FitnessStudioRepository,
) {
    val fitnessStudios: Flow<List<FitnessStudio>> = fitnessStudioRepository.fitnessStudios

    suspend fun add(
        id: String,
        name: String,
        photo: String?,
        lat: Double,
        lng: Double,
        stars: Double,
        fav: Boolean,
        wkDate: LocalDateTime
    ) {
        fitnessStudioRepository.add(id, name, photo, lat, lng, stars, fav, wkDate)
    }

    suspend fun add(gym: FitnessStudio) {
        fitnessStudioRepository.add(gym)
    }

    suspend fun add(gym: BusinessInfo) {

        val now = Clock.System.now()


        val fitGyms = FitnessStudio(
            uid = gym.id,
            name = gym.name ?: "no name",
            photo = gym.photos?.first(),
            lat = gym.coordinates?.latitude,
            lng = gym.coordinates?.longitude,
            stars = gym?.rating ?: 0.0,
            fav = true,
            workOutDate = now.toLocalDateTime(TimeZone.currentSystemDefault())
        )

        fitnessStudioRepository.add(fitGyms)
    }

    suspend fun del(gym: FitnessStudio) {
        fitnessStudioRepository.del(gym)
    }

    suspend fun deleteItemById(itemId: String) {
        fitnessStudioRepository.deleteById(itemId)
    }

    suspend fun deleteItemById(busInfo: BusinessInfo) {
        fitnessStudioRepository.deleteById(busInfo.id)
    }

    suspend fun getFitnessStudio(id: String): Flow<FitnessStudio> {
        return fitnessStudioRepository.get(id)
    }

    suspend fun exists(name: String): Boolean {
        return fitnessStudioRepository.itemExistsByName(name)
    }

    fun itemExistsById(id: String): Flow<Boolean> {
        return fitnessStudioRepository.itemExistsById(id)
    }

    suspend fun nuke() {
        fitnessStudioRepository.nuke()
    }
}