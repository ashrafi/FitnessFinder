package com.test.fitnessstudios.feature.fitnessstudio.ui

import com.test.fitnessstudios.core.network.SearchYelpQuery

sealed class UiState {
    object Loading : UiState()
    object Error : UiState()
    data class Success(val launchList: List<SearchYelpQuery.Business>) : UiState()
}

sealed interface FitnessStudioUiState {
    object Loading : FitnessStudioUiState
    data class Error(val throwable: Throwable) : FitnessStudioUiState
    data class Success(val data: List<String>) : FitnessStudioUiState
}

/*
@Immutable
@Parcelize
data class NourishUiState(
    val isLoading: Boolean = false,
    val nourishList: List<NourishDisplayable> = emptyList(),
    val isError: Boolean = false
) : Parcelable {
    sealed class PartialState {
        object Loading : PartialState() // for simplicity: initial loading & refreshing
        data class Fetched(val list: List<NourishDisplayable>) : PartialState()
        data class Error(val throwable: Throwable) : PartialState()
    }
}
 */