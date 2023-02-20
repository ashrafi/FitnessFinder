package com.test.fitnessstudios.core.domain

import com.test.fitnessstudios.core.data.repository.YelpGraphQLRepository
import javax.inject.Inject

class GetGymUseCase @Inject constructor(
    private val gymRepository: YelpGraphQLRepository,
    //private val userDataRepository: UserDataRepository,
) {
    suspend operator fun invoke() = gymRepository.getGyms()

    suspend fun getGyms() {
        gymRepository.getGyms()
    }

}