package com.example.firebasegooglesignin.presentation.sign_in.utils

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.example.firebasegooglesignin.R
import com.example.firebasegooglesignin.usecase.model.SignInResult
import com.example.firebasegooglesignin.usecase.model.User
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.concurrent.CancellationException


suspend fun SignInClient?.prepareIntentForSignIn(context: Context): IntentSender? {
    val result = try {
        this?.beginSignIn(
            buildSignInRequest(context)
        )?.await()
    } catch (e: Exception) {
        e.printStackTrace()
        if (e is CancellationException) throw e
        null
    }
    return result?.pendingIntent?.intentSender
}

suspend fun SignInClient?.signIn(intent: Intent, auth: FirebaseAuth): SignInResult {
    val credential = this?.getSignInCredentialFromIntent(intent)
    val googleIdToken = credential?.googleIdToken
    val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
    return try {
        val user = auth.signInWithCredential(googleCredentials).await().user
        SignInResult(
            data = user?.run {
                User(
                    userId = uid,
                    userName = displayName,
                    profilePicture = photoUrl?.toString()
                )
            },
            errorMessage = null
        )
    } catch (e: Exception) {
        e.printStackTrace()
        if (e is CancellationException) throw e
        SignInResult(
            data = null,
            errorMessage = e.message
        )
    }
}

suspend fun SignInClient?.signOut(auth: FirebaseAuth) {
    try {
        this?.signOut()?.await()
        auth.signOut()
    } catch (e: Exception) {
        e.printStackTrace()
        if (e is CancellationException) throw e
    }
}

fun getSignedInUser(auth: FirebaseAuth): User? {
    return auth.currentUser?.run {
        User(
            userId = uid,
            userName = displayName,
            profilePicture = photoUrl?.toString()
        )
    }
}

private fun buildSignInRequest(context: Context): BeginSignInRequest {
    return BeginSignInRequest.Builder()
        .setGoogleIdTokenRequestOptions(
            GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.web_client_id))
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()
}
