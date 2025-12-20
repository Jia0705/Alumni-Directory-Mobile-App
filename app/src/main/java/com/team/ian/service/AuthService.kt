package com.team.ian.service

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.team.ian.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthService @Inject constructor() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    init {
        firebaseAuth.currentUser?.let { updateUser(it) }
    }

    private fun updateUser(firebaseUser: FirebaseUser) {
        _user.update {
            User(
                id = firebaseUser.uid,
                name = firebaseUser.displayName ?: "Unknown",
                email = firebaseUser.email ?: "Unknown",
                photoURL = firebaseUser.photoUrl?.toString() ?: ""
            )
        }
    }

    suspend fun signIn(context: Context) {
        try {
            val token = getGoogleCredentialToken(context) ?: return
            val credential = GoogleAuthProvider.getCredential(token, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            result.user?.let { updateUser(it) }

        } catch (e: GetCredentialCancellationException) {
            Log.d("AuthService", "Sign in cancelled", e)
        } catch (e: GetCredentialException) {
            Log.d("AuthService", "Google sign in failed", e)
        } catch (e: Exception) {
            Log.d("AuthService", "Sign in failed", e)
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
        _user.value = null
    }

    fun getCurrentUser(): User? = _user.value

    private suspend fun getGoogleCredentialToken(context: Context): String? {
        val credentialManager = CredentialManager.create(context)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("757886253550-bcafc6as2kbpfvcjcsn4m724uq043l60.apps.googleusercontent.com")
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(context, request)
            result.credential.data.getString(
                "com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID_TOKEN"
            )
        } catch (e: Exception) {
            Log.d("AuthService", "Token failed", e)
            null
        }
    }

    companion object {
        private var instance: AuthService?= null

        fun getInstance(): AuthService {
            if (instance == null) {
                instance = AuthService()
            }
            return instance!!
        }
    }
}