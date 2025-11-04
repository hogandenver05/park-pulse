package com.denverhogan.parkpulse.di

import com.denverhogan.parkpulse.repository.RidesListRepository
import com.denverhogan.parkpulse.repository.InMemoryRidesListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
interface RepositoryModule {
    @Binds
    fun bindRidesListRepository(
        inMemoryRidesListRepository: InMemoryRidesListRepository
    ): RidesListRepository
}
