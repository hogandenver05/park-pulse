package com.denverhogan.parkpulse.di

import com.denverhogan.parkpulse.repository.Repository
import com.denverhogan.parkpulse.repository.InMemoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
interface RepositoryModule {
    @Binds
    fun bindRepository(
        inMemoryRepository: InMemoryRepository
    ): Repository
}
