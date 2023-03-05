package com.test.fitnessstudios.core.domain

import com.test.fitnessstudios.core.data.repository.FitnessStudioRepository
import com.test.fitnessstudios.core.database.FitnessStudio
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate


import javax.inject.Inject


class FitnessUseCase @Inject constructor(
    private val fitnessStudioRepository: FitnessStudioRepository,
) {
    val fitnessStudios: Flow<List<FitnessStudio>> = fitnessStudioRepository.fitnessStudios

    suspend fun add(id: String, name: String, date: LocalDate) {
        fitnessStudioRepository.add(id, name, date)
    }

    suspend fun add(gym: FitnessStudio) {
        fitnessStudioRepository.add(gym)
    }

    suspend fun exists(name: String): Boolean {
        return fitnessStudioRepository.exists(name)
    }

    suspend fun nuke() {
        fitnessStudioRepository.nuke()
    }
}