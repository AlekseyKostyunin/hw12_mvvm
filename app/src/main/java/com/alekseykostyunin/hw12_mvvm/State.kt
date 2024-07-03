package com.alekseykostyunin.hw12_mvvm

sealed class State {
    data object Initial : State()
    data object Loading : State()
    data class Success(val textSearch: String?) : State()
    data class Error(val textError: String?) : State()
}