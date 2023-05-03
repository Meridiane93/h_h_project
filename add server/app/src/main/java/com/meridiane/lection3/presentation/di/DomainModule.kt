package com.meridiane.lection3.presentation.di

import com.meridiane.lection3.data.repository.MockRepository
import com.meridiane.lection3.domain.useCaseAuthorization.GetToken
import com.meridiane.lection3.domain.useCaseAuthorization.GetTokenUseCaseImpl
import com.meridiane.lection3.domain.useCaseAuthorization.SaveToken
import com.meridiane.lection3.domain.useCaseAuthorization.SaveTokenUseCaseImpl
import com.meridiane.lection3.domain.useCaseAuthorization.UserAuthorization
import com.meridiane.lection3.domain.useCaseAuthorization.UserAuthorizationUseCaseImpl

import com.meridiane.lection3.domain.useCaseProducts.GetProductsUseCaseImpl
import com.meridiane.lection3.domain.useCaseProducts.GetProductsInterface
import com.meridiane.lection3.domain.useCaseProfile.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetProducts(mockRepository: MockRepository): GetProductsInterface =
        GetProductsUseCaseImpl(interfaceGetProductsRepository = mockRepository)

    @Provides
    fun provideGetProfile(mockRepository: MockRepository): GetProfile =
        GetProfileUseCaseImpl(interfaceGetProfileRepository = mockRepository)

    @Provides
    fun provideUserAuthorization(mockRepository: MockRepository): UserAuthorization =
        UserAuthorizationUseCaseImpl(interfaceGetLoginRepository = mockRepository)

//    @Provides
//    fun provideGetProductDetails(mockRepository: MockRepository): GetProductDetailsInterface =
//        GetProductDetailsUseCaseImpl(interfaceGetProductDetailsRepository = mockRepository)

    @Provides
    fun provideGetActiveOrders(mockRepository: MockRepository): GetActiveOrderInterface =
        GetActiveOrderImpl(interfaceGetActiveOrdersRepository = mockRepository)

    @Provides
    fun provideGetToken(mockRepository: MockRepository): GetToken =
        GetTokenUseCaseImpl(interfaceGetToken = mockRepository)

    @Provides
    fun provideSaveToken(mockRepository: MockRepository): SaveToken =
        SaveTokenUseCaseImpl(interfaceSaveToken = mockRepository)
}