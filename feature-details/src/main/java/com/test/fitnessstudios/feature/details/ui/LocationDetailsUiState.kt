package com.test.fitnessstudios.feature.details.ui

import com.test.fitnessstudios.core.model.model.BusinessInfo

sealed interface LocationDetailsUiState {
    object Loading : LocationDetailsUiState
    data class Error(val throwable: Throwable) : LocationDetailsUiState
    object NOTLoading : LocationDetailsUiState
    data class Success(val launchList: List<BusinessInfo?>?) : LocationDetailsUiState
}