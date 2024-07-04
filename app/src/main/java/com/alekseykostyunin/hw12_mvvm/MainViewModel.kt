package com.alekseykostyunin.hw12_mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Initial)
    val state = _state.asStateFlow()

    private val _isEnabledButtonSearch= Channel<Boolean>()
    val isEnabledButtonSearch = _isEnabledButtonSearch.receiveAsFlow()

    fun onSearchClick(text: String) {
        viewModelScope.launch {
            if (text.isEmpty()) {
                _state.value = State.Error("Поле поиска не может быть пустым")
            } else if (text.length < 3) {
                _state.value = State.Error("В запросе меньше 3 символов")
            } else {
                _state.value = State.Loading
                delay(3000)
                _state.value = State.Success(text)
            }
        }
    }

    fun onTextChanged(text: String){
        viewModelScope.launch {
            _isEnabledButtonSearch.send(text.length >= 3)
        }
    }

}