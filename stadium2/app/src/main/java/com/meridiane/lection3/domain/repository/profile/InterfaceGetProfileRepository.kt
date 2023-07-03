package com.meridiane.lection3.domain.repository.profile

import android.graphics.Bitmap
import com.meridiane.lection3.domain.entity.profile.ChangeUserProfileDomain
import com.meridiane.lection3.domain.entity.profile.Profile
import okhttp3.MultipartBody

interface InterfaceGetProfileRepository {
    suspend fun getProfile(): Profile
    suspend fun setPhoto(photo: MultipartBody.Part)
    suspend fun getPhoto(id: String): Bitmap?
    suspend fun changeUserProfile(userData: List<ChangeUserProfileDomain>): ChangeUserProfileDomain?
}