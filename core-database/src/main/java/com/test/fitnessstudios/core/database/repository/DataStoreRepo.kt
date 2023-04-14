package com.test.fitnessstudios.core.database.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject

class DataStoreRepo @Inject constructor(
    private val dataStore: DataStore<Preferences> // TODO: Move to UseCase
) {
    // from DI =  private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    val STORED_CURRENT_CATAGORY = stringPreferencesKey("YelpCategory")
    val STORED_CURRENT_MAPLIST = intPreferencesKey("ListMap")


    suspend fun saveStoredCurrentCategory(cat: String) {
        dataStore.edit { settings ->
            settings[STORED_CURRENT_CATAGORY] = cat
        }
    }

    /*val currentCategoryFlow: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[STORED_CURRENT_CATAGORY] ?: YelpCategory.fitness.name
        }

    fun saveMapListStart(startInx : Int) {
        viewModelScope.launch {
            dataStore.edit { settings ->
                settings[STORED_CURRENT_MAPLIST] = startInx
            }
            Log.d(TAG, "saveMapListStart: Save this to the map index $startInx")



            val test_write = dataStore.data.map { preferences ->
                preferences[STORED_CURRENT_MAPLIST] ?: 5
            }.first()
            Log.d(TAG, "saveMapListStart: Inside -- Save this to the map index $test_write")
        }
    }*/

}