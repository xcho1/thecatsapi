package com.thecatapi.cats.di

import android.content.Context
import com.thecatapi.cats.network.CatsApi
import com.thecatapi.cats.repository.CatsRepository
import com.thecatapi.cats.repository.UserStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserStorage(@ApplicationContext context: Context) = UserStorage(context)

    @Provides
    @Singleton
    fun provideCatsRepository(catsApi: CatsApi, userStorage: UserStorage) = CatsRepository(catsApi, userStorage)
}