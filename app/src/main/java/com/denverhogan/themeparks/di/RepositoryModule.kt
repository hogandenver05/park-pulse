package com.denverhogan.themeparks.di

import com.denverhogan.themeparks.repository.DestinationsListRepository
import com.denverhogan.themeparks.repository.InMemoryDestinationsListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
interface RepositoryModule {
    @Binds
    fun bindDestinationsListRepository(
        inMemoryDestinationsListRepository: InMemoryDestinationsListRepository
    ): DestinationsListRepository
}
