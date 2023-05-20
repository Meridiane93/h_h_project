package com.meridiane.lection3.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meridiane.lection3.ApiState
import com.meridiane.lection3.domain.entity.Profile
import com.meridiane.lection3.domain.useCaseProfile.GetProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val getProfile: GetProfile) : ViewModel() {

    private val _profileState: MutableStateFlow<ApiState<Result<Profile>>> =
        MutableStateFlow(ApiState.Loading)
    val profileState: StateFlow<ApiState<Result<Profile>>> = _profileState

    fun getProfile() {
        viewModelScope.launch {
            try {
                val getProf = getProfile.authorization().getOrNull()!!
                _profileState.emit(ApiState.Success(getProf))
            } catch (e: Exception) {
                _profileState.emit(ApiState.Error(e.message.toString()))
            }
        }
    }

    override fun onCleared() {
        viewModelScope.cancel()
    }
}



