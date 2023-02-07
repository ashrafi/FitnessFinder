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

package com.test.fitnessstudios.feature.fitnessstudio.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.fitnessstudios.core.data.FitnessStudioRepository
import com.test.fitnessstudios.feature.fitnessstudio.ui.FitnessStudioUiState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FitnessStudioViewModel @Inject constructor(
    private val fitnessStudioRepository: FitnessStudioRepository
) : ViewModel() {

    val uiState: StateFlow<FitnessStudioUiState> = fitnessStudioRepository
        .fitnessStudios.map { Success(data = it) }
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addFitnessStudio(name: String) {
        viewModelScope.launch {
            fitnessStudioRepository.add(name)
        }
    }
}

sealed interface FitnessStudioUiState {
    object Loading : FitnessStudioUiState
    data class Error(val throwable: Throwable) : FitnessStudioUiState
    data class Success(val data: List<String>) : FitnessStudioUiState
}
