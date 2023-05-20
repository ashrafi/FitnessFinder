package com.test.fitnessstudios.core.database.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.google.android.gms.maps.model.LatLng
import com.test.fitnessstudios.core.model.model.LatLngModel
import com.test.fitnessstudios.core.model.model.YelpCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class DataStoreRepo @Inject constructor(
    private val dataStore: DataStore<Preferences> // TODO: Move to UseCase
) {

    // from DI =  private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    val STORED_CURRENT_CATAGORY = stringPreferencesKey("YelpCategory")
    val STORED_CURRENT_MAPLIST = intPreferencesKey("ListMap")
    val LAT_LNG_SET_KEY = stringSetPreferencesKey("lat_lng_set_key")
    val DEFAULT_LAT_LNG_SET = setOf("33.524155, -111.905792")


    // CAT
    suspend fun saveCategory(cat: String) {
        dataStore.edit { settings ->
            settings[STORED_CURRENT_CATAGORY] = cat
        }
    }

    val getCategoryFlow: Flow<String>
        get() = dataStore.data.map { preferences ->
            preferences[STORED_CURRENT_CATAGORY] ?: YelpCategory.fitness.name
        }

    // LAT LNG

    suspend fun saveLatLng(place: LatLng) {
        val mapSet = setOf(LatLngModel(place.latitude, place.longitude))
        val serializedSet = mapSet.map { it.toSerializedString() }.toSet()
        dataStore.edit { preferences ->
            preferences[LAT_LNG_SET_KEY] = serializedSet
        }
    }

    suspend fun saveLatLngs(latLngs: Set<LatLngModel>) {
        val serializedSet = latLngs.map { it.toSerializedString() }.toSet()
        dataStore.edit { preferences ->
            preferences[LAT_LNG_SET_KEY] = serializedSet
        }
    }

    val readSavedLatLng: Flow<Set<LatLngModel>>
        get() = dataStore.data.map { preferences ->
            val serializedSet = preferences[LAT_LNG_SET_KEY] ?: DEFAULT_LAT_LNG_SET
            serializedSet.map { LatLngModel.fromSerializedString(it) }.toSet()
        }

    // TAB
    suspend fun saveTabPosition(tabIndx: Int) {
        dataStore.edit { settings ->
            settings[STORED_CURRENT_MAPLIST] = tabIndx
        }
    }

    val readSavedTabPosition: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[STORED_CURRENT_MAPLIST] ?: 0
        }


}