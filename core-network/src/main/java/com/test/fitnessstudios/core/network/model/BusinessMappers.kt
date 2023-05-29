package com.test.fitnessstudios.core.network.model

import com.test.fitnessstudios.core.model.BusinessInfo
import com.test.fitnessstudios.core.model.Category
import com.test.fitnessstudios.core.model.Coordinates
import com.test.fitnessstudios.data.network.SearchYelpQuery


fun SearchYelpQuery.Business.toBusinessInfo(): BusinessInfo {

    return BusinessInfo(
        id = id ?: "No ID",
        name = name ?: "No Name",
        url = url ?: "No web address",
        rating = rating,
        photos = photos?.mapNotNull { it },
        price = price ?: "No Price",
        coordinates = Coordinates(coordinates?.latitude, coordinates?.longitude),
        categories = categories?.mapNotNull { Category(it?.title) },
    )

}