package com.test.fitnessstudios.feature.store.ui

sealed class UiState {
    object Loading : UiState()
    object Error : UiState()
    data class Success(val launchList: List<String>) : UiState()
}
