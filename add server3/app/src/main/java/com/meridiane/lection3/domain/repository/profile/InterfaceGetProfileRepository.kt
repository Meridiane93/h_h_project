package com.meridiane.lection3.domain.repository.profile

import android.graphics.Bitmap
import com.meridiane.lection3.domain.entity.profile.Profile

interface InterfaceGetProfileRepository {
    suspend fun getProfile(): Profile
    suspend fun setPhoto(photo: String)
    suspend fun getPhoto(id: String): Bitmap?
}