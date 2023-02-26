package com.test.fitnessstudios.core.network.model

import com.test.fitnessstudios.core.domain.BusinessInfo
import com.test.fitnessstudios.core.domain.Category
import com.test.fitnessstudios.core.domain.Coordinates
import com.test.fitnessstudios.core.network.SearchYelpQuery


fun SearchYelpQuery.Business.toBusinessInfo(): BusinessInfo? {

    return BusinessInfo(
        id = id ?: "No ID",
        name = name ?: "No Name",
        rating = rating,
        photos = photos?.mapNotNull { it },
        price = price ?: "No Price",
        coordinates = Coordinates(coordinates?.latitude, coordinates?.longitude),
        categories = categories?.mapNotNull { Category(it?.title) },
    ) ?: null

}