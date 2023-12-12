package com.example.firebasegooglesignin.usecase.model

data class SignInResult(
    val data: User?,
    val errorMessage: String?
)
