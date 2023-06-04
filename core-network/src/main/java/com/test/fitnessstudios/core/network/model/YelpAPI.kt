package com.test.fitnessstudios.core.network.model

import com.test.fitnessstudios.core.model.BusinessInfo

interface YelpAPI {
    suspend fun getBusinesses(
        latitude: Double,
        longitude: Double,
        radius: Double,
        sort_by: String,
        categories: String
    ): List<BusinessInfo?>?
}
