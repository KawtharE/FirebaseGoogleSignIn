package com.example.firebasegooglesignin.presentation.utils

import com.example.firebasegooglesignin.domain.model.User

sealed class UiState {
    data class SignInState(val isSuccessful: Boolean, val errorMsg: String?) : UiState()
    data class SignOutState(val isSuccessful: Boolean, val errorMsg: String?) : UiState()
    data class AlreadyConnected(val user: User) : UiState()
}