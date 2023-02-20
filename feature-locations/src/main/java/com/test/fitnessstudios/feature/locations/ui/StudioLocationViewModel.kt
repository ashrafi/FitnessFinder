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

package com.test.fitnessstudios.feature.locations.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.fitnessstudios.core.data.FitnessStudioRepository
import com.test.fitnessstudios.core.data.repository.YelpGraphQLRepository
import com.test.fitnessstudios.core.domain.GetGymUseCase
import com.test.fitnessstudios.core.network.SearchYelpQuery
import com.test.fitnessstudios.core.network.service.apolloClient
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudioLocationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gyms : GetGymUseCase
) : ViewModel() {

    fun callQL() {
        viewModelScope.launch {
            gyms.getGyms()
        }
    }
}
