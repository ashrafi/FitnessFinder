package com.test.fitnessstudios.core.domain

import com.test.fitnessstudios.core.data.repository.FitnessStudioRepository
import com.test.fitnessstudios.core.data.repository.YelpRepo
import com.test.fitnessstudios.core.model.model.BusinessInfo
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

    suspend fun add(name: String) {
        fitnessStudioRepository.add(name)
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