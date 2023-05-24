package com.meridiane.lection3.presentation.viewModel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meridiane.lection3.domain.useCaseAuthorization.GetToken
import com.meridiane.lection3.domain.useCaseAuthorization.SaveToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val getToken: GetToken,
    private val saveToken: SaveToken) :
    ViewModel() {

    val liveData = MutableLiveData<String>()

    fun getTokenBd(): String = getToken.getTokenBd()

    fun getTokenServer(login: String, password: String){
        viewModelScope.launch {
            try {
                val token = getToken.getTokenServer(login,password)
                Log.d("MyTag", "checkLogin $token")
                liveData.value = token.toString()

            } catch (e: Exception) {
                Log.d("MyTag", "Except $e")
                liveData.value = e.message.toString()
            }
        }
    }

    fun saveTokenVM(token: String) = saveToken.saveToken(token)

    fun checkLoginAndPassword(login: String, password: String): Int {
        return when {
            !isEmailValid(login) && password.length <= 6 -> 1

            isEmailValid(login) && password.length <= 6 -> 2

            !isEmailValid(login) && password.length > 6 -> 3

            else -> 4
        }
    }

    private fun isEmailValid(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    override fun onCleared() {
        viewModelScope.cancel()
    }
}