package com.test.fitnessstudios.feature.locations.ui

import com.test.fitnessstudios.core.database.FitnessStudio
import com.test.fitnessstudios.core.model.model.BusinessInfo

sealed interface UiState {
    object Loading : UiState
    data class Error(val throwable: Throwable) : UiState
    data class Success(val launchList: List<BusinessInfo?>?) : UiState
    data class SuccessFitness(val data: List<FitnessStudio>) : UiState
}