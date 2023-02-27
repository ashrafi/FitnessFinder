package com.test.fitnessstudios.core.domain

import com.test.fitnessstudios.core.data.repository.FitnessStudioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class FitnessUseCase @Inject constructor(
    private val fitnessStudioRepository: FitnessStudioRepository,
) {
    val fitnessStudios: Flow<List<String>> = fitnessStudioRepository.fitnessStudios

    suspend fun add(name: String) {
        fitnessStudioRepository.add(name = name)
    }
}