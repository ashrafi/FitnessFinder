package com.test.fitnessstudios.feature.favorites.ui

import com.test.fitnessstudios.core.database.FitnessStudio

sealed interface FavoritesCardUiState {
    object Loading : FavoritesCardUiState
    data class Error(val throwable: Throwable) : FavoritesCardUiState
    data class Success(val data: List<FitnessStudio>) : FavoritesCardUiState
}