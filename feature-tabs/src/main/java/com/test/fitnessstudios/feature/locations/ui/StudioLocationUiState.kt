package com.test.fitnessstudios.feature.locations.ui

import com.test.fitnessstudios.core.model.BusinessInfo

sealed interface StudioLocationUiState {
    object Loading : StudioLocationUiState
    data class Error(val throwable: Throwable) : StudioLocationUiState
    data class Success(val launchList: List<BusinessInfo?>?) : StudioLocationUiState
}