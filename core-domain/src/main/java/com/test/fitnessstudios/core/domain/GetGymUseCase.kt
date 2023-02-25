package com.test.fitnessstudios.core.domain

import com.test.fitnessstudios.core.data.repository.YelpGraphQLRepository
import javax.inject.Inject

class GetGymUseCase @Inject constructor(
    private val gymRepository: YelpGraphQLRepository,
    //private val userDataRepository: UserDataRepository,
) {
    //apolloClient(context).query(LaunchListQuery())
    // ApolloCall<LaunchListQuery.Data>
    operator fun invoke(
        latitude: Float = 33.524155F,
        longitude: Float = -111.905792F,
        radius: Float = 1000.0F,
        sort_by: String = "distance",
        categories: String = "fitness"
    ): YelpGraphQLRepository {
        return gymRepository
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