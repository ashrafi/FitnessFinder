/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.test.fitnessstudios.feature.favorites.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.test.fitnessstudios.core.domain.DataStoreUseCase
import com.test.fitnessstudios.core.domain.FitnessUseCase
import com.test.fitnessstudios.feature.favorites.ui.FavoritesCardUiState.Error
import com.test.fitnessstudios.feature.favorites.ui.FavoritesCardUiState.Loading
import com.test.fitnessstudios.feature.favorites.ui.FavoritesCardUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesCardViewModel @Inject constructor(
    private val fitness: FitnessUseCase,
    private val dataStoreUseCase: DataStoreUseCase
) : ViewModel() {

    val uiState: StateFlow<FavoritesCardUiState> = fitness
        .fitnessStudios.map { Success(data = it) }
        .catch { Error(it) }
        // TODO: Replace with lifecycle aware and remove 5000
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun nuke() {
        viewModelScope.launch {
            fitness.nuke()
        }
    }

    fun del(ui: String) {
        viewModelScope.launch {
            fitness.deleteItemById(ui)
        }
    }

    fun saveLatLngs(place: LatLng) {
        viewModelScope.launch {
            dataStoreUseCase.saveLatLng(place)
        }
    }
}
