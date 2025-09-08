package com.denverhogan.themeparks.di

import com.denverhogan.themeparks.repository.RidesListRepository
import com.denverhogan.themeparks.repository.InMemoryRidesListRepository
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
