package com.test.fitnessstudios.core.domain

import com.google.android.gms.maps.model.LatLng
import com.test.fitnessstudios.core.data.repository.YelpRepo
import com.test.fitnessstudios.core.model.BusinessInfo
import javax.inject.Inject

class YelpCallUseCase @Inject constructor(
    private val yelpRepo: YelpRepo,
) {
    // Combine gym info with favorite info
    suspend operator fun invoke(
        category: String,
        local: LatLng,
    ): List<BusinessInfo?>? {
        return yelpRepo(
            categories = category,
            latitude = local.latitude,
            longitude = local.longitude
        )
    }
}