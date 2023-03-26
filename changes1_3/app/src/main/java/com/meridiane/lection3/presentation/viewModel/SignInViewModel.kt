package com.meridiane.lection3.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meridiane.lection3.domain.repository.MockRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {

    private val repository = MockRepository()

    val liveData = MutableLiveData<String>()

    fun getLogin() {
        viewModelScope.launch {
            try {
                val checkLogin = repository.getLogin()
                liveData.value = checkLogin.toString()

            }
            catch (e: Exception) {
                liveData.value = e.message.toString()
            }
        }
    }

    override fun onCleared() {
        viewModelScope.cancel()
    }
}