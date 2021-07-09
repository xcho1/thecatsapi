package com.thecatapi.cats.di

import com.thecatapi.cats.network.CatsApi
import com.thecatapi.cats.repository.CatsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCatsRepository(catsApi: CatsApi) = CatsRepository(catsApi)
}