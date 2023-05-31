package com.meridiane.lection3.domain.useCaseProfile

import android.graphics.Bitmap
import com.meridiane.lection3.domain.entity.profile.Profile
import com.meridiane.lection3.domain.repository.profile.InterfaceGetProfileRepository

interface GetProfile{
    suspend fun authorization(): Result<Profile>
    suspend fun setImage(photo:String)
    suspend fun getImage(id:String): Result<Bitmap?>

}

class GetProfileUseCaseImpl(private val interfaceGetProfileRepository: InterfaceGetProfileRepository) : GetProfile {

    override suspend fun authorization(): Result<Profile> {

        return try {
            val answer = interfaceGetProfileRepository.getProfile()
            Result.success(answer)
        }
        catch (e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun setImage(photo: String){
        interfaceGetProfileRepository.setPhoto(photo)
    }

    override suspend fun getImage(id: String): Result<Bitmap?> {
        return try {
            val answer = interfaceGetProfileRepository.getPhoto(id)
            Result.success(answer)
        } catch (e: Exception){
            Result.failure(e)
        }

    }

}