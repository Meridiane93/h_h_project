package com.meridiane.lection3.domain.useCaseProfile

import com.meridiane.lection3.domain.models.Profile
import com.meridiane.lection3.domain.repository.InterfaceGetProfileRepository

class GetProfile(private val interfaceGetProfileRepository: InterfaceGetProfileRepository) {

    suspend fun authorization(): Result<Profile> =
        interfaceGetProfileRepository.getProfile()

}