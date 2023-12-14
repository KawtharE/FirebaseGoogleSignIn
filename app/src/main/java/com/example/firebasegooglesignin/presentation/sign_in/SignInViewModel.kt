package com.example.firebasegooglesignin.presentation.sign_in

import androidx.lifecycle.ViewModel
import com.example.firebasegooglesignin.presentation.utils.UiState
import com.example.firebasegooglesignin.domain.model.SignInResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel : ViewModel() {

    private val _signInUiState =
        MutableStateFlow(UiState.SignInState(isSuccessful = false, errorMsg = null))
    val signInUiState = _signInUiState.asStateFlow()

    fun onSignInCallback(result: SignInResult) {
        _signInUiState.update {
            it.copy(
                isSuccessful = result.data != null,
                errorMsg = result.errorMessage
            )
        }
    }

    fun resetUiState() {
        _signInUiState.update { UiState.SignInState(false, null) }
    }

}