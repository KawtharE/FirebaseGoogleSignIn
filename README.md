# FirebaseGoogleSignIn
Android Kotlin mini-project where i applied Clean architecture to implement Firebase Google Sign-In.

## Steps to set set Firebase 
1. Connect Android studio to Firebase app in Firebase console:
   this can be done after selecting Tools -> firebase -> Authentication -> Authenticate using Google
   
![Screenshot 2023-12-27 at 09 51 48](https://github.com/KawtharE/FirebaseGoogleSignIn/assets/19794865/f59e619a-7878-424b-b3d8-89127a587436)

![Screenshot 2023-12-27 at 09 52 09](https://github.com/KawtharE/FirebaseGoogleSignIn/assets/19794865/f57b1784-10f5-483c-bbbb-609f2a3fdbb2)

![Screenshot 2023-12-27 at 09 52 22](https://github.com/KawtharE/FirebaseGoogleSignIn/assets/19794865/1d8460dc-810e-48a8-ba6c-b305cff8a682)

<img width="1200" alt="Screenshot 2023-12-27 at 09 53 03" src="https://github.com/KawtharE/FirebaseGoogleSignIn/assets/19794865/a6b31c7d-4992-46ce-908f-77517e0ce4f0">


2. Set the SHA certificate fingerprints key in Firebase console:
   this key you can get it from ``signingReport`` gradle task in the android studio project

   ![Screenshot 2023-12-27 at 10 02 24](https://github.com/KawtharE/FirebaseGoogleSignIn/assets/19794865/db065510-f07a-4b2b-b5ec-5066f390ac74)

3. After connection is successful, add the `google-services.json` file to the project

4. Add Gradle dependencies:
   ```
   implementation("com.google.firebase:firebase-auth-ktx:22.3.0")
   implementation("com.google.android.gms:play-services-auth:20.7.0")
   ```
   
5. Have an instance of `SignInClient`:
   ```
   private val googleSignInClient by lazy {
        Identity.getSignInClient(applicationContext)
    }
   ```

6. Prepare the SingIn intent:
   ```
   SignInClient.beginSignIn(
            BeginSignInRequest.Builder()
              .setGoogleIdTokenRequestOptions(
                  GoogleIdTokenRequestOptions.builder()
                  .setSupported(true)
                  .setFilterByAuthorizedAccounts(false)
                  .setServerClientId(context.getString(R.string.web_client_id))
                .build()
              )
              .setAutoSelectEnabled(true)
              .build()
        )
   ```
   where the ``web_client_id`` is set under your firebase project -> Authentication -> Sign-in method -> google -> Web SDK configuration

7. Once we receive intent as response, we actually signin:
   ```
    val credential = SignInClient?.getSignInCredentialFromIntent(intent)
    val googleIdToken = credential?.googleIdToken
    val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
    ...
    val user = auth.signInWithCredential(googleCredentials).await().user
    ...
   ```

No user should be connected using google account, bear in mind that at least you should have a connected google account in the device.
