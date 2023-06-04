package com.test.fitnessstudios.core.testing.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class FakeDataStore @Inject constructor() : DataStore<Preferences> {

    private val preferencesFlow = MutableStateFlow(emptyPreferences())

    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
        val currentPreferences = preferencesFlow.value
        val newPreferences = transform(currentPreferences)
        preferencesFlow.value = newPreferences
        return newPreferences
    }

    override val data: Flow<Preferences>
        get() = preferencesFlow

    companion object {
        fun createFakeDataStore(preferences: Preferences): FakeDataStore {
            val fakeDataStore = FakeDataStore()
            fakeDataStore.preferencesFlow.value = preferences
            return fakeDataStore
        }
    }
}
