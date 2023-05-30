package com.meridiane.lection3.domain.repository

import android.graphics.Bitmap
import com.meridiane.lection3.domain.entity.Profile

interface InterfaceGetProfileRepository {
    suspend fun getProfile(): Profile
    suspend fun setPhoto(photo: String)
    suspend fun getPhoto(id: String): Bitmap?
}