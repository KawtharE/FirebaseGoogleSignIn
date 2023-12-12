package com.example.firebasegooglesignin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firebasegooglesignin.presentation.sign_in.SignInScreen
import com.example.firebasegooglesignin.presentation.sign_in.SignInViewModel
import com.example.firebasegooglesignin.presentation.sign_in.utils.prepareIntentForSignIn
import com.example.firebasegooglesignin.presentation.sign_in.utils.signIn
import com.example.firebasegooglesignin.ui.theme.FirebaseGoogleSignInTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val googleSignInClient by lazy {
        Identity.getSignInClient(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseGoogleSignInTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "sign_in") {
                        composable("sign_in") {
                            val viewModel = viewModel<SignInViewModel>()
                            val state = viewModel.signInUiState.collectAsStateWithLifecycle()

                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult()
                            ) { activityResult ->
                                if (activityResult.resultCode == RESULT_OK) {
                                    lifecycleScope.launch {
                                        val signInResult = googleSignInClient.signIn(
                                            intent = activityResult.data ?: return@launch
                                        )
                                        viewModel.onSignInCallback(signInResult)
                                    }
                                }
                            }

                            LaunchedEffect(key1 = state.value.isSuccessful) {
                                if (state.value.isSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Sign in success!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                            SignInScreen(state = state.value) {
                                lifecycleScope.launch {
                                    val signInIntentSender =
                                        googleSignInClient.prepareIntentForSignIn(applicationContext)
                                    launcher.launch(
                                        IntentSenderRequest.Builder(
                                            signInIntentSender ?: return@launch
                                        ).build()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FirebaseGoogleSignInTheme {
        Greeting("Android")
    }
}