package com.meridiane.lection3.presentation.di

import com.meridiane.lection3.data.repository.MockRepository
import com.meridiane.lection3.domain.useCaseAuthorization.UserAuthorization
import com.meridiane.lection3.domain.useCaseProducts.GetProducts
import com.meridiane.lection3.domain.useCaseProfile.GetProfile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetProducts(mockRepository: MockRepository): GetProducts =
        GetProducts(interfaceGetProductsRepository = mockRepository)

    @Provides
    fun provideGetProfile(mockRepository: MockRepository): GetProfile =
        GetProfile(interfaceGetProfileRepository = mockRepository)

    @Provides
    fun provideUserAuthorization(mockRepository: MockRepository): UserAuthorization =
        UserAuthorization(interfaceGetLoginRepository = mockRepository)
}