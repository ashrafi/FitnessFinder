package com.test.fitnessstudios.feature.fitnessstudio.ui

import com.test.fitnessstudios.core.network.SearchYelpQuery

sealed interface FitnessStudioUiState {
    object Loading : FitnessStudioUiState
    data class Error(val throwable: Throwable) : FitnessStudioUiState
    data class Success(val data: List<String>) : FitnessStudioUiState
}