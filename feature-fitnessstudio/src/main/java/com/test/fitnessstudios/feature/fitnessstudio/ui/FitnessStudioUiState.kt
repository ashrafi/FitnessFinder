package com.test.fitnessstudios.feature.fitnessstudio.ui

sealed interface FitnessStudioUiState {
    object Loading : FitnessStudioUiState
    data class Error(val throwable: Throwable) : FitnessStudioUiState
    data class Success(val data: List<String>) : FitnessStudioUiState
}