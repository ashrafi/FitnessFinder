package com.test.fitnessstudios.feature.details.ui

import com.google.android.gms.maps.model.LatLng
import com.test.fitnessstudios.core.database.FitnessStudio

sealed interface LocationDetailsUiState {
    object Loading : LocationDetailsUiState
    data class Error(val throwable: Throwable) : LocationDetailsUiState

    object NOTLoading : LocationDetailsUiState

    //data class Success(val launchList: List<BusinessInfo?>?) : LocationDetailsUiState
    data class SuccessFitness(val data: List<FitnessStudio>) : LocationDetailsUiState

    data class locLatLng(val data: List<LatLng>) : LocationDetailsUiState
}