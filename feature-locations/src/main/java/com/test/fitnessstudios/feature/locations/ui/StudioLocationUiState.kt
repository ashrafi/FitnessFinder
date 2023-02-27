package com.test.fitnessstudios.feature.locations.ui

import com.test.fitnessstudios.core.model.model.BusinessInfo

sealed class UiState {
    object Loading : UiState()
    object Error : UiState()
    data class Success(val launchList: List<BusinessInfo?>?) : UiState()
}