package com.meridiane.lection3.domain.useCaseProfile

import com.meridiane.lection3.domain.entity.Profile
import com.meridiane.lection3.domain.repository.InterfaceGetProfileRepository

interface GetProfile{
    suspend fun authorization(): Result<Profile>
}

class GetProfileUseCaseImpl(private val interfaceGetProfileRepository: InterfaceGetProfileRepository) : GetProfile {

    override suspend fun authorization(): Result<Profile> {
       val answer = interfaceGetProfileRepository.getProfile()
        return try {
            Result.success(answer)
        }
        catch (e:Exception){
            Result.failure(e)
        }
    }

}