package com.test.fitnessstudios.core.domain


import javax.inject.Inject

class GetGymUseCase @Inject constructor(
    //private val yelpRepo: YelpRepo,
    //private val fitnessStudioRepository: FitnessStudioRepository,
) {
    //Combine gym info with favorite info
    //apolloClient(context).query(LaunchListQuery())
    // ApolloCall<LaunchListQuery.Data>
    /*suspend operator fun invoke(
    ): List<BusinessInfo>? {
        return yelpRepo.invoke()
    }*/
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