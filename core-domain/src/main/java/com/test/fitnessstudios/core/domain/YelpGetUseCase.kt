package com.test.fitnessstudios.core.domain

import com.test.fitnessstudios.core.data.repository.YelpRepo
import com.test.fitnessstudios.core.model.BusinessInfo
import javax.inject.Inject

class YelpGetUseCase @Inject constructor(
    private val yelpRepo: YelpRepo
) {
    // Combine gym info with favorite info
    suspend operator fun invoke(): List<BusinessInfo?>? {
        return yelpRepo.getYelpBusList()
    }
}
