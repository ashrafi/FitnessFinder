package com.test.fitnessstudios.core.domain

import com.test.fitnessstudios.core.data.repository.FitnessStudioRepository
import com.test.fitnessstudios.core.data.repository.YelpRepo
import com.test.fitnessstudios.core.database.FitnessStudio
import com.test.fitnessstudios.core.model.model.BusinessInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class YelpCallUseCase @Inject constructor(
    private val yelpRepo: YelpRepo,
    private val fitnessStudioRepository: FitnessStudioRepository,
) {
    // Combine gym info with favorite info
    suspend operator fun invoke(
        category: String
    ): List<BusinessInfo?>? {
        return yelpRepo(categories = category)
    }

    val fitnessStudios: Flow<List<FitnessStudio>> = fitnessStudioRepository.fitnessStudios

    suspend fun add(gym: FitnessStudio) {
        fitnessStudioRepository.add(gym)
    }

    suspend fun del() {
        fitnessStudioRepository.nuke()
    }

    suspend fun isFav(
        name: String
    ): Boolean {
        return fitnessStudioRepository.exists(name)
    }


}

/*
    operator fun invoke(sortBy: TopicSortField = NONE): Flow<List<FollowableTopic>> {
        return combine(
            userDataRepository.userData,
            topicsRepository.getTopics(),
        ) { userData, topics ->
            val followedTopics = topics
                .map { topic ->
                    FollowableTopic(
                        topic = topic,
                        isFollowed = topic.id in userData.followedTopics,
                    )
                }
            when (sortBy) {
                NAME -> followedTopics.sortedBy { it.topic.name }
                else -> followedTopics
            }
        }
    }
 */