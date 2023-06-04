package com.test.fitnessstudios.core.data.util

sealed class LocationResult<out T> {
    data class Success<out T>(val data: T) : LocationResult<T>()
    data class Error(val exception: Throwable) : LocationResult<Nothing>()
    object Loading : LocationResult<Nothing>()
}
