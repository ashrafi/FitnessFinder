package com.test.fitnessstudios.feature.details.ui

import com.test.fitnessstudios.core.database.FitnessStudio

sealed interface LocationDetailsUiState {
    object Loading : LocationDetailsUiState
    data class Error(val throwable: Throwable) : LocationDetailsUiState

    //data class Success(val launchList: List<BusinessInfo?>?) : LocationDetailsUiState
    data class SuccessFitness(val data: List<FitnessStudio>) : LocationDetailsUiState
}