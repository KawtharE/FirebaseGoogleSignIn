package com.example.firebasegooglesignin.domain.model

data class SignInResult(
    val data: User?,
    val errorMessage: String?
)
