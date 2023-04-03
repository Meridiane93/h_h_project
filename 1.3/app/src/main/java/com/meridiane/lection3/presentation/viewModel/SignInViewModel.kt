package com.meridiane.lection3.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meridiane.lection3.domain.models.Authorization
import com.meridiane.lection3.domain.useCaseAuthorization.UserAuthorization
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val userAuthorization: UserAuthorization) :
    ViewModel() {

    val liveData = MutableLiveData<String>()

    fun getLogin(authorization: Authorization) {
        viewModelScope.launch {
            try {
                val checkLogin = userAuthorization.authorization(authorization)
                liveData.value = checkLogin.toString()

            } catch (e: Exception) {
                liveData.value = e.message.toString()
            }
        }
    }

    override fun onCleared() {
        viewModelScope.cancel()
    }
}