package com.meridiane.lection3.presentation.di

import com.meridiane.lection3.data.repository.MockRepository
import com.meridiane.lection3.domain.useCaseAuthorization.UserAuthorization
import com.meridiane.lection3.domain.useCaseAuthorization.UserAuthorizationUseCaseImpl
import com.meridiane.lection3.domain.useCaseProducts.GetProductDetailsInterface
import com.meridiane.lection3.domain.useCaseProducts.GetProductDetailsUseCaseImpl
import com.meridiane.lection3.domain.useCaseProducts.GetProductsUseCaseImpl
import com.meridiane.lection3.domain.useCaseProducts.GetProductsInterface
import com.meridiane.lection3.domain.useCaseProfile.GetAllOrderInterface
import com.meridiane.lection3.domain.useCaseProfile.GetAllOrderImpl
import com.meridiane.lection3.domain.useCaseProfile.GetProfile
import com.meridiane.lection3.domain.useCaseProfile.GetProfileUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

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

    @Provides
    fun provideGetProductDetails(mockRepository: MockRepository): GetProductDetailsInterface =
        GetProductDetailsUseCaseImpl(interfaceGetProductDetailsRepository = mockRepository)

    @Provides
    fun provideGetAllOrders(mockRepository: MockRepository): GetAllOrderInterface =
        GetAllOrderImpl(interfaceGetAllOrdersRepository = mockRepository)
}