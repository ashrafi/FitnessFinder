package com.test.fitnessstudios.core.domain

import com.google.android.gms.maps.model.LatLng
import com.test.fitnessstudios.core.database.repository.DataStoreRepo
import com.test.fitnessstudios.core.model.LatLngModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataStoreUseCase @Inject constructor(
    private val DSRepoy: DataStoreRepo
) {

    suspend fun saveCategory(cat: String) {
        DSRepoy.saveCategory(cat)
    }

    fun getCategory(): Flow<String> {
        return DSRepoy.getCategoryFlow
    }

    suspend fun saveLatLng(place: LatLng) {
        DSRepoy.saveLatLng(place)
    }

    fun readLatLng(): Flow<Set<LatLngModel>> {
        return DSRepoy.readSavedLatLng
    }

    fun readTab(): Flow<Int> {
        return DSRepoy.readSavedTabPosition
    }

    suspend fun saveTab(tabIndx: Int) {
        DSRepoy.saveTabPosition(tabIndx)
    }
}
