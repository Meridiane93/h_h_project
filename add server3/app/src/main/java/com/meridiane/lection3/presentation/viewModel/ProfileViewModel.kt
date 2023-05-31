package com.meridiane.lection3.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meridiane.lection3.ApiState
import com.meridiane.lection3.domain.entity.profile.Photo
import com.meridiane.lection3.domain.entity.profile.Profile
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
    val photoState: MutableStateFlow<Photo> = MutableStateFlow(Photo())

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



    fun setPhoto(photo: String){
        viewModelScope.launch {
            try {
                getProfile.setImage(photo)

            }catch (e:Exception){
                _profileState.emit(ApiState.Error(e.message.toString()))
            }
        }
    }

    fun getPhoto(id: String){

        viewModelScope.launch {
            try {
                val getPhoto = getProfile.getImage(id).getOrNull()!!
                photoState.value = Photo(getPhoto)
            }catch (e: Exception) {
                _profileState.emit(ApiState.Error(e.message.toString()))
            }
        }
    }

    override fun onCleared() {
        viewModelScope.cancel()
    }
}



