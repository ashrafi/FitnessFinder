package com.test.fitnessstudios.core.data

import com.test.fitnessstudios.core.data.repository.YelpRepo
import com.test.fitnessstudios.core.model.model.BusinessInfo
import com.test.fitnessstudios.core.network.model.YelpAPI
import javax.inject.Inject

class YelpRepoImp @Inject constructor(
    //private val topicDao: TopicDao,
    private val network: YelpAPI,
) : YelpRepo {
    data class yelpAPIParms(
        val latitude: Double,
        val longitude: Double,
        val radius: Double,
        val sort_by: String,
        val categories: String
    )

    var currYelpCall: yelpAPIParms? = null
    var busList: List<BusinessInfo?>? = null

    //  List<SearchYelpQuery.Business>?.toFlow().map {
    override suspend fun invoke(
        latitude: Double,
        longitude: Double,
        radius: Double,
        sort_by: String,
        categories: String
    ): List<BusinessInfo?>? {

        currYelpCall = yelpAPIParms(latitude, longitude, radius, sort_by, categories)

        busList = network.getBusinesses(
            latitude,
            longitude,
            radius,
            sort_by,
            categories
        )

        return busList

    }

    override suspend fun getYelpBusList(): List<BusinessInfo?>? {

        // if it is empty and we can get the previous call try to get it
        if (!busList.isNullOrEmpty()) {
            currYelpCall?.let {
                busList = network.getBusinesses(
                    it.latitude,
                    it.longitude,
                    it.radius,
                    it.sort_by,
                    it.categories
                )
            }
        }
        return busList
    }


}