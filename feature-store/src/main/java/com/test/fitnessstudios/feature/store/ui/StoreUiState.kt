package com.test.fitnessstudios.feature.store.ui

import com.test.fitnessstudios.core.network.SearchYelpQuery

sealed class UiState {
    object Loading : UiState()
    object Error : UiState()
    data class Success(val launchList: List<SearchYelpQuery.Business>) : UiState()
}