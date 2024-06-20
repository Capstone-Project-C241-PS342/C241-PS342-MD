package com.bangkit.capstone.gestura.utils

sealed class getFeedback {
    data class Success<out T>(val data: T) : getFeedback()
    data class Error(val exception: Throwable) : getFeedback()
    data class Loading(val isLoading: Boolean) : getFeedback()
}