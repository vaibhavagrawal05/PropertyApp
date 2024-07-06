package eu.tutorials.propertyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

sealed class AuthState {
    object Authenticated : AuthState()
    data class Error(val message: String?) : AuthState()
    object Loading : AuthState()
    object Unauthenticated : AuthState()
}

class AuthViewModel : ViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>(AuthState.Unauthenticated)
    val authState: LiveData<AuthState> get() = _authState

    init {
        val currentUser = firebaseAuth.currentUser
        _authState.value = if (currentUser != null) AuthState.Authenticated else AuthState.Unauthenticated
    }

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message)
                }
            }
    }

    fun signup(email: String, password: String, firstname: String, lastname: String) {
        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message)
                }
            }
    }

    fun signout() {
        firebaseAuth.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}
