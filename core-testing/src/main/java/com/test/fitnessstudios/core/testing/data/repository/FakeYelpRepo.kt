package com.test.fitnessstudios.core.testing.data.repository

import com.test.fitnessstudios.core.data.repository.YelpRepo
import com.test.fitnessstudios.core.model.model.BusinessInfo
import com.test.fitnessstudios.core.model.model.Coordinates

class FakeYelpRepo : YelpRepo {

    val yelpCall = listOf(
        BusinessInfo(
            id = "No ID",
            name = "No Name",
            url = "No web address",
            rating = 4.5,
            photos = listOf("https://example.com/photo1.jpg"),
            price = "No Price",
            coordinates = Coordinates(0.0, 0.0),
            categories = null,
        ),
        BusinessInfo(
            id = "No ID",
            name = "No Name",
            url = "No web address",
            rating = 4.5,
            photos = listOf("https://example.com/photo1.jpg"),
            price = "No Price",
            coordinates = Coordinates(0.0, 0.0),
            categories = null,
        )
    )

    override suspend fun invoke(
        latitude: Double,
        longitude: Double,
        radius: Double,
        sort_by: String,
        categories: String
    ): List<BusinessInfo?>? {
        return yelpCall
    }

    override suspend fun getYelpBusList(): List<BusinessInfo?>? {
        return yelpCall
    }
}