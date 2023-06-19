package com.meridiane.lection3.presentation.di

import android.content.Context
import com.meridiane.lection3.data.repository.MockRepository
import com.meridiane.lection3.data.storage.SaveToken
import com.meridiane.lection3.data.storage.TokenStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideMockRepository(tokenStorage: TokenStorage): MockRepository =
        MockRepository(tokenStorage)

    @Provides
    @Singleton
    fun provideSaveToken(@ApplicationContext context: Context): TokenStorage {
        return SaveToken(context = context)
    }
}